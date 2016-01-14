package org.northrop.leanne.shoppingcart.shop

import scala.collection.immutable._
import scala.util._

case class Cart(val contents: List[Product])

object Cart {
  def apply(productsCSV: String) : (List[Throwable], Cart) = {
    val (knownProducts, badProducts) = productsCSV.split(",").map((name) => Try[Product](Product(name.trim))).partition(_.isSuccess)
    (badProducts.toList.map(_.failed.get), Cart(knownProducts.map(_.get).toList))
  }
}