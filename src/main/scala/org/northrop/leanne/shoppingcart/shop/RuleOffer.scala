package org.northrop.leanne.shoppingcart.shop

import scala.collection.immutable._

case class RuleOffer(val name: String, val discount: TillScannerState => Option[Int], val update: TillScannerState => TillScannerState, val isApplicable: TillScannerState => Boolean)

object RuleOffer {
  def discount(offer: Offer)(s: TillScannerState) : Option[Int] = {
    if (isApplicable(offer)(s)) Some(offer.discountInPence) else None
  }

  def update(offer: Offer)(s: TillScannerState) : TillScannerState = {
    def dropOfferItems() = offer.conditions.foldLeft(List.empty[Product]){ 
        (nonOfferProductsList, productOffer) =>  
        val (product, numberOfOfferProducts) = productOffer
        val (thisOfferProducts, nonOfferProducts) = s.itemsSeenNotInOffers.partition(_ == product)
        thisOfferProducts.drop(numberOfOfferProducts) ++ nonOfferProducts ++ nonOfferProductsList
      }
    val newItemsSeenNotInOffers = dropOfferItems()
    if (newItemsSeenNotInOffers == s.itemsSeenNotInOffers) s else s.copy(itemsSeenNotInOffers = newItemsSeenNotInOffers)
  }

  def isApplicable(offer: Offer)(s: TillScannerState) : Boolean = {
    val cartItemsIncludedInOffer = s.itemsSeenNotInOffers.groupBy(_.name).filterKeys(offer.conditions contains Product(_))
    if (cartItemsIncludedInOffer.isEmpty) false
    else cartItemsIncludedInOffer.forall { (itemsOnOffer) => 
      val (name, items) = itemsOnOffer
      (items.length / (offer.conditions(Product(name)))) >= 1
    }
  }

  def apply(offer: Offer) : RuleOffer = {
    RuleOffer(offer.name, discount(offer)_, update(offer)_, isApplicable(offer)_)
  }
}
