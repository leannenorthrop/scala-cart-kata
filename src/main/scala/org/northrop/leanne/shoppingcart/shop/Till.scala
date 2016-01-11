package org.northrop.leanne.shoppingcart.shop

import scala.collection.immutable._

case class ProductPrice(val product : Product, val priceInPence : Int)

case class Till(val prices : List[ProductPrice]) {
  def lookupPrice(product : Product) : Option[Int] = prices.find(_.product.name == product.name).map(_.priceInPence)
}
