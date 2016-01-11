package org.northrop.leanne.shoppingcart.shop

import scala.collection.immutable._

case class ProductPrice(val product : Product, val priceInPence : Int)
case class Offer(val name: String, val conditions:Map[Product,Int])

case class Till(val prices : List[ProductPrice]) {
  def lookupPrice(product : Product) : Option[Int] = prices.find(_.product.name == product.name).map(_.priceInPence)
}

object Till {
  def scan(till:Till)(cart:Cart) : Int = {
    cart.contents.map(product => product.flatMap(till.lookupPrice(_))).filter(_!=None).map(_.getOrElse(0)).foldLeft(0)(_ + _)
  }
}