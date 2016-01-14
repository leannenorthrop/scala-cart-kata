package org.northrop.leanne.shoppingcart.shop

import scala.collection.immutable._

case class TillScannerState(val itemsSeen: List[Product], 
                            val itemsSeenNotInOffers: List[Product],
                            val errors: List[String],
                            val totalInPence: Int) {

  def isApplicable(offer: Offer) : Boolean = {
    val offerItemsInCart = itemsSeenNotInOffers.groupBy(_.name).filterKeys(offer.conditions contains Product(_))
    if (offerItemsInCart.isEmpty) false
    else offerItemsInCart.forall { 
      (productMapEntry) => 
      val (productName, list) = productMapEntry
      (list.length / (offer.conditions(Product(productName)))) >= 1
    }
  }

  def apply(offer: Offer) : TillScannerState = {
    def removeOfferItems() = offer.conditions.foldLeft(List.empty[Product]){ 
        (seenNonOfferProducts, offerCondition) =>  
        val (thisOfferProducts, nonOfferProducts) = itemsSeenNotInOffers.partition(_ == offerCondition._1)
        thisOfferProducts.drop(offerCondition._2) ++ nonOfferProducts ++ seenNonOfferProducts
      }
    val newItemsSeenNotInOffers = removeOfferItems()
    if (newItemsSeenNotInOffers == itemsSeenNotInOffers) this else copy(itemsSeenNotInOffers = newItemsSeenNotInOffers)
  }

  def purchase(till: Till)(product: Product) : TillScannerState = {
    val offerOption = till.offers.find(isApplicable(_))
    val discountedPriceOption = till.price(product).map(_ + offerOption.map(_.discountInPence).getOrElse(0))
    offerOption.map(apply(_)).getOrElse(this).copy(totalInPence = totalInPence + discountedPriceOption.getOrElse(0), errors = discountedPriceOption.map(_ => errors).getOrElse(s"No price for product $product." :: errors))
  }

  def scan(till: Till)(product: Product) : TillScannerState = {
    copy(itemsSeen = product :: itemsSeen, itemsSeenNotInOffers = product :: itemsSeenNotInOffers).purchase(till)(product)
  }
}