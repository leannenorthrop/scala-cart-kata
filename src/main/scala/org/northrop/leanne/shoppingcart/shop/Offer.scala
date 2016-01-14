package org.northrop.leanne.shoppingcart.shop

import scala.collection.immutable._

case class Offer(val name: String, val conditions: Map[Product,Int], val discountInPence: Int)