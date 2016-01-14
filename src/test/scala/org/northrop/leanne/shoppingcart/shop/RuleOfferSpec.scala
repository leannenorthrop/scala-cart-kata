package org.northrop.leanne.shoppingcart.shop

import org.northrop.leanne._
import scala.collection.immutable._

/**
    Behaviour tests for org.northrop.leanne.shoppingcart.shop.RuleOffer
*/
class RuleOfferSpec extends UnitSpec {
  trait TillWithOffersObjects {
    val applePrice = 33
    val orangePrice = 20
    val prices = Map(Product("apple") -> applePrice, Product("orange") -> orangePrice)
    val offers = Offer("Oranges ~ 3 for Price of 2", ListMap(Product("orange")->3), -orangePrice) ::
                 Offer("Apples ~ Buy 1 Get 1 Free", ListMap(Product("apple")->2), -applePrice) :: Nil
    val till = Till(prices, offers)
    val scanner = Till.scan(till)_
  }

  "RuleOffer isApplicable" should "return true if till state fulfills offer conditions" in new TillWithOffersObjects {
    // setup
    val tillState = TillScannerState(List.empty[Product], List.fill(3)(Product("orange")), List.empty[String], 0)

    // do it
    val isApplicable = RuleOffer(offers.head).isApplicable(tillState)

    // check
    isApplicable shouldBe true
  }
   
  "RuleOffer isApplicable" should "return false if till state does not fulfill offer condition" in new TillWithOffersObjects {
    // setup
    val tillState = TillScannerState(List.empty[Product], List.empty[Product], List.empty[String], 0)

    // do it
    val isApplicable = RuleOffer(offers.head).isApplicable(tillState)

    // check
    isApplicable shouldBe false
  }

  "RuleOffer" should "return offer discount" in new TillWithOffersObjects {
    // setup
    val tillState = TillScannerState(List.empty[Product], List.fill(3)(Product("orange")), List.empty[String], 0)

    // do it
    val discountOption = RuleOffer(offers.head).discount(tillState)

    // check
    discountOption shouldBe Some(-orangePrice)
  }

  "RuleOffer update" should "return scanner state if no offers apply" in {
      // setup
      val threeFor2OrangesOffer = RuleOffer(Offer("Oranges ~ 3 for Price of 2", ListMap(Product("orange")->3), -25))
      val allItems = List.fill(3)(Product("strawberry")) ++ List.fill(3)(Product("apple"))
      val tillState = TillScannerState(allItems, allItems, List.empty[String], -22)

      // do it
      val newState = threeFor2OrangesOffer.update(tillState)

      // check
      newState shouldBe tillState
  }

  "RuleOffer update" should "return new state with only discounted products removed from itemsSeenNotInOffers" in {
      // setup
      val threeFor2OrangesOffer = RuleOffer(Offer("Oranges ~ 3 for Price of 2", ListMap(Product("orange")->3), -25))
      val nonOfferItems = List.fill(3)(Product("strawberry")) ++ List.fill(3)(Product("apple"))
      val allItems = nonOfferItems ++ List.fill(3)(Product("orange"))
      val tillState = TillScannerState(allItems, allItems, List.empty[String], -22)

      // do it
      val newState = threeFor2OrangesOffer.update(tillState)

      // check
      newState.itemsSeenNotInOffers shouldBe nonOfferItems
      newState.itemsSeen shouldBe allItems
      newState.totalInPence shouldBe -22
      newState.errors shouldBe empty
  }

  "RuleOffer discount" should "return Some(discountInPence) if offer applies" in {
    // setup
    val allItems = List.fill(3)(Product("strawberry"))
    val tillState = TillScannerState(allItems, allItems, List.empty[String], 24)
    val discountInPence = (ts:TillScannerState) => Some(12)
    val id = (ts:TillScannerState) => ts
    val alwaysValid= (ts:TillScannerState) => true

    // do it
    val discountOption = RuleOffer("test rule offer", discountInPence, id, alwaysValid).discount(tillState)

    // check
    discountOption shouldBe Some(12)
  }

  "RuleOffer isApplicable" should "return false" in {
    // setup
    val allItems = List.fill(3)(Product("strawberry"))
    val tillState = TillScannerState(allItems, allItems, List.empty[String], 24)
    val discountInPence = (ts:TillScannerState) => Some(12)
    val id = (ts:TillScannerState) => ts
    val alwaysValid= (ts:TillScannerState) => false

    // do it
    val result = RuleOffer("test rule offer", discountInPence, id, alwaysValid).isApplicable(tillState)

    // check
    result shouldBe false
  }

  "RuleOffer update" should "return itself" in {
    // setup
    val allItems = List.fill(3)(Product("strawberry"))
    val tillState = TillScannerState(allItems, allItems, List.empty[String], 24)
    val discountInPence = (ts:TillScannerState) => Some(12)
    val id = (ts:TillScannerState) => ts
    val alwaysValid= (ts:TillScannerState) => false

    // do it
    val result = RuleOffer("test rule offer", discountInPence, id, alwaysValid).update(tillState)

    // check
    result shouldBe tillState
  }

}