package org.northrop.leanne.shoppingcart.shop

import scala.collection.immutable._
import scala.util._

case class Cart(val contents : List[Option[Product]]) {
  def items():List[Product] = contents.filter(_!=None).map(_.get)
}

object Cart {

  // Todo: Error list of unknown products
  def apply(productsCSV:String):Cart = new Cart(productsCSV.split(",").map((name) => Try[Product](Product(name.trim)).toOption).toList)
}