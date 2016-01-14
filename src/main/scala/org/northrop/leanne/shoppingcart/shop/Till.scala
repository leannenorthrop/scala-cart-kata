package org.northrop.leanne.shoppingcart.shop

import scala.collection.immutable._

case class TillState(val seenProducts: List[Product], 
                     val seenNonOfferProducts: List[Product],
                     val errors: List[String],
                     val totalInPence: Int) {

  def apply(offer: Offer) : TillState = {
    copy(seenNonOfferProducts = 
      offer.conditions.foldLeft(List.empty[Product]){ 
        (seenNonOfferProducts, offerCondition) =>  
        val (thisOfferProducts, nonOfferProducts) = seenNonOfferProducts.partition(_ == offerCondition._1)
        thisOfferProducts.drop(offerCondition._2) ++ nonOfferProducts ++ seenNonOfferProducts
      })
  }
}

case class Till(val prices: List[ProductPrice], val offers: List[Offer]) {
  def lookupPrice(product: Product) : Option[Int] = prices.find(_.product.name == product.name).map(_.priceInPence)
  
  def findOffers(product: Product) : List[Offer] = offers.filter(_.conditions contains product)
  
  def lookupOfferDiscount(state: TillState, product: Product) : Option[(TillState,Int)] = {
    findOffers(product).find(_.isApplicable(state)).map(offer => ( state(offer), offer.discountInPence) )
  }
}


object Till {
  def scan(till: Till)(cart: Cart) : (List[String], Int) = {
    val cartContents = cart.contents.filter(_!=None).map(_.get)

    val initialRunningState = TillState(List.empty[Product], List.empty[Product], List.empty[String], 0)

    val finalState = cartContents.foldLeft( initialRunningState ) { 
      (runningState,product) =>

      val nextRunningOfferState = runningState.copy(seenProducts = product :: runningState.seenProducts,
                                                    seenNonOfferProducts = product :: runningState.seenNonOfferProducts)

      val (offerAppliedRunningState,discountInPence) = till.lookupOfferDiscount(nextRunningOfferState, product).getOrElse(nextRunningOfferState, 0)
      val productPriceOption = till.lookupPrice(product).map(_ + discountInPence)

      offerAppliedRunningState.copy(totalInPence = runningState.totalInPence + productPriceOption.getOrElse(0),
                                    errors = productPriceOption.map(_ => runningState.errors).getOrElse(s"No price for product $product." :: runningState.errors))
    }

    (finalState.errors, finalState.totalInPence)
  }
}
