package org.northrop.leanne.shoppingcart.shop

import scala.collection.immutable._

// Todo: Convert conditions and discountInPence to a 'business rule' function returning discount price in pence
case class Offer(val name: String, val conditions:Map[Product,Int], val discountInPence : Int) {
  def isApplicable(till : TillState) : Boolean = {
    till.seenNonOfferProducts.groupBy(_.name).filterKeys(conditions contains Product(_)).forall{ 
      (productMapEntry) => 
      val (productName, list) = productMapEntry.x
      (list.length / (conditions(Product(productName)))) >= 1
    }
  }
}