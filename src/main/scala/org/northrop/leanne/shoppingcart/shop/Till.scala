package org.northrop.leanne.shoppingcart.shop

import scala.collection.immutable._

case class ProductPrice(val product : Product, val priceInPence : Int)

// Todo: Convert conditions and discountInPence to a 'business rule' function returning discount price in pence
case class Offer(val name: String, val conditions:Map[Product,Int], val discountInPence : Int)

case class TillState(val seenProducts: List[Product], 
                     val seenNonOfferProducts: List[Product],
                     val errors: List[String],
                     val totalInPence: Int)


case class Till(val prices : List[ProductPrice], val offers: List[Offer]) {
  def lookupPrice(product : Product) : Option[Int] = prices.find(_.product.name == product.name).map(_.priceInPence)
  
  def findOffers(product : Product) : List[Offer] = offers.filter(_.conditions contains product)
  
  def lookupOfferDiscount(state : TillState, product : Product) : Option[(TillState,Int)] = {

    val allSeenNonOfferProducts = (product :: state.seenNonOfferProducts).groupBy(_.name)

    def doesOfferApply(offer:Offer) : Boolean = {
      allSeenNonOfferProducts.filterKeys(offer.conditions contains Product(_)).forall{ 
        (productMapEntry) => 
        val (productName, list) = productMapEntry.x
        (list.length / (offer.conditions(Product(productName)))) >= 1
      }
    }

    def applyOfferToState(offer:Offer, state: TillState) : TillState = {
      state.copy(seenNonOfferProducts = 
        offer.conditions.foldLeft(List.empty[Product]){ 
          (seenNonOfferProducts, offerCondition) =>  
          val (thisOfferProducts, nonOfferProducts) = state.seenNonOfferProducts.partition(_ == offerCondition._1)
          thisOfferProducts.drop(offerCondition._2) ++ nonOfferProducts ++ seenNonOfferProducts
        })
    }

    findOffers(product).find(doesOfferApply(_)).map { 
      (offer) => 
      (state.copy(seenNonOfferProducts = applyOfferToState(offer, state).seenNonOfferProducts), offer.discountInPence) 
    }
  }
}


object Till {
  def scan(till:Till)(cart:Cart) : (List[String], Int) = {
    val cartContents = cart.contents.filter(_!=None).map(_.get)

    val initialRunningState = TillState(List.empty[Product], List.empty[Product], List.empty[String], 0)

    val finalState = cartContents.foldLeft( initialRunningState ) { 
      (runningState,product) =>

      val (newRunningState,discountInPence) = till.lookupOfferDiscount(runningState, product).getOrElse((runningState.copy(seenNonOfferProducts = product :: runningState.seenNonOfferProducts), 0))
      val productPriceOption = till.lookupPrice(product).map(_ + discountInPence)
      val updatedErrors = if (productPriceOption == None) s"No price for product $product." :: runningState.errors else runningState.errors
      val newTotal = runningState.totalInPence + productPriceOption.getOrElse(0)

      newRunningState.copy(seenProducts = (product :: runningState.seenProducts),
                           totalInPence = newTotal,
                           errors = updatedErrors)
    }

    (finalState.errors, finalState.totalInPence)
  }
}
