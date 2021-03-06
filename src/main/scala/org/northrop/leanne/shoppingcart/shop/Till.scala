package org.northrop.leanne.shoppingcart.shop

import scala.collection.immutable._

case class Till(val prices: Map[Product,Int], val offers: List[Offer]) {
  def price(product: Product) : Option[Int] = prices.get(product)
  def purchase(cart: Cart) : (List[String], Int) = {
    val finalState = cart.contents.foldLeft( TillScannerState() )( _.scan(this)(_) )
    (finalState.errors, finalState.totalInPence)
  }
}

object Till {
  def scan(till: Till)(cart: Cart) : (List[String], Int) = till.purchase(cart)
}
