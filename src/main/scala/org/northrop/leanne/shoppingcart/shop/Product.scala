package org.northrop.leanne.shoppingcart.shop

case class Product(val name : String) {
  require(Product.isKnown(name), "Bad product name: "+ name)
}

object Product {
  val validProducts = Array("apple", "orange", "strawberry")

  def isKnown(name : String) : Boolean = validProducts.contains(name.toLowerCase)
}

case class ProductPrice(val product : Product, val priceInPence : Int)
