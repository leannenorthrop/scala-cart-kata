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

  def findOffer(till: Till)(product: Product) :  Option[Offer] = till.findOffers(product).find(_.isApplicable(this))

  def lookupDiscount(till: Till)(product: Product) : Option[Int] = {
    findOffer(till)(product).map(_.discountInPence)
  }

  def apply(offer: Offer) : TillScannerState = {
    def removeOfferItems() = offer.conditions.foldLeft(List.empty[Product]){ 
        (seenNonOfferProducts, offerCondition) =>  
        val (thisOfferProducts, nonOfferProducts) = seenNonOfferProducts.partition(_ == offerCondition._1)
        thisOfferProducts.drop(offerCondition._2) ++ nonOfferProducts ++ seenNonOfferProducts
      }
    copy(itemsSeenNotInOffers = removeOfferItems())
  }

  def scan(till: Till)(product: Product) : TillScannerState = {
    val priceOption = till.lookupPrice(product)
    val discountedPriceOption = priceOption.map(_ + lookupDiscount(till)(product).getOrElse(0))
    val me = this
    val newState = findOffer(till)(product).map(me.apply(_)).getOrElse(this)
    newState.copy(totalInPence = me.totalInPence + discountedPriceOption.getOrElse(0),
                                    errors = discountedPriceOption.map(_ => me.errors).getOrElse(s"No price for product $product." :: me.errors))
  }
}