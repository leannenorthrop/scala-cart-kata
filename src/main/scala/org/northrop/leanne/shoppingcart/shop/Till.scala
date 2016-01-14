package org.northrop.leanne.shoppingcart.shop

import scala.collection.immutable._

case class Till(val prices: Map[Product,Int], val offers: List[Offer]) {
  def lookupPrice(product: Product) : Option[Int] = prices.get(product)
  def findOffers(product: Product) : List[Offer] = offers.filter(_.conditions contains product)
}

object Till {
  def scan(till: Till)(cart: Cart) : (List[String], Int) = {
    def init() : TillScannerState = TillScannerState(List.empty[Product], List.empty[Product], List.empty[String], 0)

    val finalState = cart.items().foldLeft( init() )( _.scan(till)(_) )
    
    (finalState.errors, finalState.totalInPence)
  }
}