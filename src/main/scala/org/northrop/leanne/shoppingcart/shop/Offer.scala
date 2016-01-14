package org.northrop.leanne.shoppingcart.shop

import scala.collection.immutable._

// Todo: Convert conditions and discountInPence to a 'business rule' function returning discount price in pence
case class Offer(val name: String, val conditions:Map[Product,Int], val discountInPence : Int) {
  def isApplicable(scannerState:TillScannerState) : Boolean = scannerState.isApplicable(this)
}