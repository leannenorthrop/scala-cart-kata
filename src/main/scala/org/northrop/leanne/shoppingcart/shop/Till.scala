package org.northrop.leanne.shoppingcart.shop

import scala.collection.immutable._

case class ProductPrice(val product : Product, val priceInPence : Int)

case class Offer(val name: String, val conditions:Map[Product,Int], val discountInPence : Int)

case class TillState(val seenProducts: List[Product], 
                     val seenNonOfferProducts: List[Product],
                     val totalInPence: Int)


case class Till(val prices : List[ProductPrice], val offers: List[Offer]) {
  def lookupPrice(product : Product) : Option[Int] = prices.find(_.product.name == product.name).map(_.priceInPence)
  
  def findOffers(product : Product) : List[Offer] = offers.filter(_.conditions contains product)
  
  def lookupOfferDiscount(state : TillState, product : Product) : Option[(TillState,Int)] = {
    val allSeenNonOfferProducts = (product :: state.seenNonOfferProducts).groupBy(_.name)

    def doesOfferApply(offer:Offer) : Boolean = {
      allSeenNonOfferProducts.filterKeys(offer.conditions contains Product(_)).forall( (entry) => 
        (entry._2.length / (offer.conditions(Product(entry._1)))) >= 1 )
    }

    def applyOfferToState(offer:Offer, state: TillState) : TillState = {
      state.copy(seenNonOfferProducts = offer.conditions.foldLeft(List.empty[Product]){ 
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
  def scan(till:Till)(cart:Cart) : Int = {
    val mapOfProducts = cart.contents.filter(_!=None).map(_.get)

    val initialRunningState = TillState(List.empty[Product], List.empty[Product], 0)
    val total = mapOfProducts.foldLeft( initialRunningState ) { 
      (runningState,product) =>
      val (newRunningState,discountInPence) = till.lookupOfferDiscount(runningState, product).getOrElse((runningState.copy(seenNonOfferProducts = product :: runningState.seenNonOfferProducts), 0))
      val newTotal = runningState.totalInPence + till.lookupPrice(product).map(_ + discountInPence).getOrElse(0)

      newRunningState.copy(seenProducts = (product :: runningState.seenProducts),
                           totalInPence = newTotal)
    }.totalInPence

    total
  }
}
