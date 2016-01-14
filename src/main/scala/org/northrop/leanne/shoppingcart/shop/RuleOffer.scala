package org.northrop.leanne.shoppingcart.shop

import scala.collection.immutable._

case class RuleOffer(val name: String, val discount: TillScannerState => Option[Int], val update: TillScannerState => TillScannerState, val isApplicable: TillScannerState => Boolean)

object RuleOffer {
  def discount(offer: Offer)(s: TillScannerState) : Option[Int] = {
    if (isApplicable(offer)(s)) Some(offer.discountInPence) else None
  }

  def update(offer: Offer)(s: TillScannerState) : TillScannerState = {
    def removeOfferItems() = offer.conditions.foldLeft(List.empty[Product]){ 
        (seenNonOfferProducts, offerCondition) =>  
        val (thisOfferProducts, nonOfferProducts) = s.itemsSeenNotInOffers.partition(_ == offerCondition._1)
        thisOfferProducts.drop(offerCondition._2) ++ nonOfferProducts ++ seenNonOfferProducts
      }
    val newItemsSeenNotInOffers = removeOfferItems()
    if (newItemsSeenNotInOffers == s.itemsSeenNotInOffers) s else s.copy(itemsSeenNotInOffers = newItemsSeenNotInOffers)
  }

  def isApplicable(offer: Offer)(s: TillScannerState) : Boolean = {
    val offerItemsInCart = s.itemsSeenNotInOffers.groupBy(_.name).filterKeys(offer.conditions contains Product(_))
    if (offerItemsInCart.isEmpty) false
    else offerItemsInCart.forall { 
      (productMapEntry) => 
      val (productName, list) = productMapEntry
      (list.length / (offer.conditions(Product(productName)))) >= 1
    }
  }

  def apply(offer: Offer) : RuleOffer = {
    RuleOffer(offer.name, discount(offer)_, update(offer)_, isApplicable(offer)_)
  }
}