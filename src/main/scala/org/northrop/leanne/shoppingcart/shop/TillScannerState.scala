package org.northrop.leanne.shoppingcart.shop

import scala.collection.immutable._

case class TillScannerState(val itemsSeen: List[Product], 
                            val itemsSeenNotInOffers: List[Product],
                            val errors: List[String],
                            val totalInPence: Int) {

  def purchase(till: Till)(product: Product) : TillScannerState = {
    val me = this
    val offerOption = till.offers.find(RuleOffer(_).isApplicable(me)).map(RuleOffer(_))
    val discountInPence = offerOption.map(_.discount(me).getOrElse(0)).getOrElse(0)
    val discountedPriceOption = till.price(product).map(_ + discountInPence)
    offerOption.map(_.update(me)).getOrElse(me).copy(totalInPence = totalInPence + discountedPriceOption.getOrElse(0), errors = discountedPriceOption.map(_ => errors).getOrElse(s"No price for product $product." :: errors))
  }

  def scan(till: Till)(product: Product) : TillScannerState = {
    copy(itemsSeen = product :: itemsSeen, itemsSeenNotInOffers = product :: itemsSeenNotInOffers).purchase(till)(product)
  }
}