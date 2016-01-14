package org.northrop.leanne.shoppingcart.shop

import org.northrop.leanne._
import scala.collection.immutable._

/**
    Behaviour tests for org.northrop.leanne.shoppingcart.shop.TillScannerState
*/
class TillScannerStateSpec extends UnitSpec {
  trait TillWithoutOrangePriceObjects {
    val prices = Map(Product("apple")->33)
    val offers = List.empty[Offer]
    val till = Till(prices, offers)
    val scanner = Till.scan(till)_
  }

  trait TillObjects {
    val prices = Map(Product("apple") -> 33, Product("orange")->20)
    val offers = List.empty[Offer]
    val till = Till(prices, offers)
    val scanner = Till.scan(till)_
  }

  trait TillWithOffersObjects {
    val applePrice = 33
    val orangePrice = 20
    val prices = Map(Product("apple") -> applePrice, Product("orange") -> orangePrice)
    val offers = Offer("Oranges ~ 3 for Price of 2", ListMap(Product("orange")->3), -orangePrice) ::
                 Offer("Apples ~ Buy 1 Get 1 Free", ListMap(Product("apple")->2), -applePrice) :: Nil
    val till = Till(prices, offers)
    val scanner = Till.scan(till)_
  }

  "TillScannerState" should "initialise with lists, errors, and total price in pence" in {
      // set up
      val runningTotalInPence = 976
      val runningSeenNonDiscountedProducts = Product("apple") :: Product("orange") :: Nil
      val runningSeenProducts = Product("apple") :: Product("apple") :: Product("apple") :: Product("orange") :: Nil

      // do it
      val state = TillScannerState(runningSeenProducts, 
                            runningSeenNonDiscountedProducts,
                            List.empty[String], 
                            runningTotalInPence)
  
      // check
      state.totalInPence shouldBe runningTotalInPence
      state.itemsSeenNotInOffers shouldBe runningSeenNonDiscountedProducts
      state.itemsSeen shouldBe runningSeenProducts
      state.errors shouldBe Nil
  }

  "TillScannerState isApplicable" should "return true if till state fulfills offer conditions" in new TillWithOffersObjects {
    // setup
    val tillState = TillScannerState(List.empty[Product], List.fill(3)(Product("orange")), List.empty[String], 0)

    // do it
    val isApplicable = tillState.isApplicable(offers.head)

    // check
    isApplicable shouldBe true
  }
   
  "TillScannerState isApplicable" should "return false if till state does not fulfill offer condition" in new TillWithOffersObjects {
    // setup
    val tillState = TillScannerState(List.empty[Product], List.empty[Product], List.empty[String], 0)

    // do it
    val isApplicable = tillState.isApplicable(offers.head)

    // check
    isApplicable shouldBe false
  }

  "TillScannerState findOffer" should "find first offer that applies for itemsSeenNotInOffers if it exists" in new TillWithOffersObjects {
      // setup
      val tillState = TillScannerState(List.empty[Product], List.fill(4)(Product("apple")), List.empty[String], 0)

      // do it
      val offerOption = tillState.findOffer(till)(Product("apple"))
  
      // check
      offerOption should not be None
      offerOption.get.name shouldBe "Apples ~ Buy 1 Get 1 Free"
  }

  "TillScannerState findOffer" should "return None if no offer exists" in new TillWithOffersObjects {
      // setup
      val tillState = TillScannerState(List.empty[Product], List.fill(4)(Product("strawberry")), List.empty[String], 0)

      // do it
      val offerOption = tillState.findOffer(till)(Product("apple"))
  
      // check
      offerOption shouldBe None
  }

  /*
    "Till lookupOfferDiscount" should "return new state if offer applies" in new TillWithOffersObjects {
      // set up
      val tillState = TillScannerState(List.fill(2)(Product("apple")), List.fill(2)(Product("apple")), List.empty[String], 0)

      // do it
      val (newState, discountInPence) = till.lookupOfferDiscount(tillState, Product("apple")).getOrElse((tillState,0))

      // check
      newState.itemsSeenNotInOffers shouldBe empty
  }
  */

  /*"Till lookupOfferDiscount" should "return None if no offers apply" taggedAs(OfInterest) in new TillWithOffersObjects {
      // do it
      val offerOption = till.lookupOfferDiscount(Product("apple"))

      // check
      offerOption shouldBe None
  }

  "Till lookupOfferDiscount" should "return discount in pence if offer applies" in new TillWithOffersObjects {
      // do it
      val (newState, discountInPence) = till.lookupOfferDiscount(Product("apple")).getOrElse((tillState,0))

      // check
      discountInPence shouldBe -applePrice
  }*/

  "TillScannerState apply" should "return new state with discounted products removed from non offer products seen list" in {
      // setup
      val threeFor2OrangesOffer = Offer("Oranges ~ 3 for Price of 2", ListMap(Product("orange")->3), -25)
      val tillState = TillScannerState(List.empty[Product], List.fill(3)(Product("orange")), List.empty[String], 0)

      // do it
      val newState = tillState(threeFor2OrangesOffer)

      // check
      newState.itemsSeenNotInOffers shouldBe empty
  }
}