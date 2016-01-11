package org.northrop.leanne.shoppingcart.shop

import scala.collection.immutable._

case class ProductPrice(val product : Product, val priceInPence : Int)
case class Offer(val name: String, val conditions:Map[Product,Int], val discountInPence : Int)

case class Till(val prices : List[ProductPrice], val offers: List[Offer]) {
  def lookupPrice(product : Product) : Option[Int] = prices.find(_.product.name == product.name).map(_.priceInPence)
  
  def findOffers(product : Product) : List[Offer] = offers.filter(_.conditions contains product)
  
  def lookupOfferDiscount(state: List[Product], product : Product) : Option[(List[Product],Int)] = {
    val allSeen = (product :: state).groupBy(_.name)

    def doesApply(offer:Offer) : Boolean = {
      allSeen.filterKeys(offer.conditions contains Product(_)).forall( (entry) => (entry._2.length / (offer.conditions(Product(entry._1)))) >= 1 )
    }

    def applyOfferToState(offer:Offer, state: List[Product]) : List[Product] = {
      val newState : List[Product] = List[Product]()
      offer.conditions.foldLeft(newState)( (l, entry) => 
        state.partition(_ == entry._1)._1.drop(entry._2) ++ state.partition(_ == entry._1)._2 ++ l
      )
    }

    findOffers(product).find(doesApply(_)).map( (offer) => (applyOfferToState(offer, state), offer.discountInPence) )
  }
}

object Till {
  def scan(till:Till)(cart:Cart) : Int = {
    val mapOfProducts = cart.contents.filter(_!=None).map(_.get)

    val initialRunningState : Tuple2[List[Product],Int] = Tuple2(List[Product](), 0)
    val (_, total) = mapOfProducts.foldLeft( initialRunningState ) { 
      (runningState,product) =>

      val (newRunningState, discountInPence) = till.lookupOfferDiscount(runningState._1, product).getOrElse(runningState._1, 0)

      (newRunningState, runningState._2 + till.lookupPrice(product).map(_ + discountInPence).getOrElse(0))
    }

    total
  }
}
