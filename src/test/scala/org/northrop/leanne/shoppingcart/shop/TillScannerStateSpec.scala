package org.northrop.leanne.shoppingcart.shop

import org.northrop.leanne._
import scala.collection.immutable._

/**
    Behaviour tests for org.northrop.leanne.shoppingcart.shop.TillScannerState
*/
class TillScannerStateSpec extends UnitSpec {
  trait TillWithOffersObjects {
    val applePrice = 33
    val orangePrice = 20
    val prices = Map(Product("apple") -> applePrice, Product("orange") -> orangePrice)
    val offers = Offer("Oranges ~ 3 for Price of 2", ListMap(Product("orange")->3), -orangePrice) ::
                 Offer("Apples ~ Buy 1 Get 1 Free", ListMap(Product("apple")->2), -applePrice) :: Nil
    val till = Till(prices, offers)
    val scanner = Till.scan(till)_
  }

  trait TillWithDiscountedOrangesObjects {
    val applePrice = 33
    val orangePrice = 20
    val prices = Map(Product("apple") -> applePrice, Product("orange") -> orangePrice)
    val offers = Offer("Oranges ~ 3 for Price of 2.5", ListMap(Product("orange")->3), -10) ::
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

  "TillScannerState lookupDiscount" should "return None if no offers apply" taggedAs(OfInterest) in new TillWithOffersObjects {
      // setup
      val tillState = TillScannerState(List.empty[Product], List.empty[Product], List.empty[String], 0)

      // do it
      val offerOption = tillState.lookupDiscount(till)(Product("strawberry"))

      // check
      offerOption shouldBe None
  }

  "TillScannerState lookupDiscount" should "return discount in pence if offer applies" in new TillWithOffersObjects {
      // setup
      val tillState = TillScannerState(List.empty[Product], List.fill(4)(Product("apple")), List.empty[String], 0)

      // do it
      val discountInPence = tillState.lookupDiscount(till)(Product("apple")).getOrElse(-1)

      // check
      discountInPence shouldBe -applePrice
  }

  "TillScannerState apply" should "return new state with only discounted products removed from itemsSeenNotInOffers" in {
      // setup
      val threeFor2OrangesOffer = Offer("Oranges ~ 3 for Price of 2", ListMap(Product("orange")->3), -25)
      val nonOfferItems = List.fill(3)(Product("strawberry")) ++ List.fill(3)(Product("apple"))
      val allItems = nonOfferItems ++ List.fill(3)(Product("orange"))
      val tillState = TillScannerState(allItems, allItems, List.empty[String], -22)

      // do it
      val newState = tillState(threeFor2OrangesOffer)

      // check
      newState.itemsSeenNotInOffers shouldBe nonOfferItems
      newState.itemsSeen shouldBe allItems
      newState.totalInPence shouldBe -22
      newState.errors shouldBe empty
  }

  "TillScannerState apply" should "return itself if no offers apply" in {
      // setup
      val threeFor2OrangesOffer = Offer("Oranges ~ 3 for Price of 2", ListMap(Product("orange")->3), -25)
      val allItems = List.fill(3)(Product("strawberry")) ++ List.fill(3)(Product("apple"))
      val tillState = TillScannerState(allItems, allItems, List.empty[String], -22)

      // do it
      val newState = tillState(threeFor2OrangesOffer)

      // check
      newState shouldBe tillState
  }

  "TillScannerState scan" should "return new totalInPence if price exists for product" in new TillWithOffersObjects {
      // setup
      val allItems = List.fill(3)(Product("strawberry"))
      val tillState = TillScannerState(allItems, allItems, List.empty[String], 0)

      // do it
      val newState = tillState.scan(till)(Product("apple"))

      // check
      newState.totalInPence shouldBe applePrice
  }

  "TillScannerState scan" should "return new totalInPence with discount applied if price and applicable offer exists for product" in new TillWithDiscountedOrangesObjects {
      // setup
      val allItems = List.fill(3)(Product("strawberry")) ++ List.fill(2)(Product("orange"))
      val tillState = TillScannerState(allItems, allItems, List.empty[String], orangePrice*2)

      // do it
      val newState = tillState.scan(till)(Product("orange"))

      // check
      newState.totalInPence shouldBe 50
  }

  "TillScannerState scan 2 apples" should "return price of one apple" in new TillWithOffersObjects {
      // setup
      val tillState = TillScannerState(List.empty[Product], List.empty[Product], List.empty[String], 0)

      // do it
      val newState = tillState.scan(till)(Product("apple")).scan(till)(Product("apple"))

      // check
      newState.totalInPence shouldBe applePrice
  }
}