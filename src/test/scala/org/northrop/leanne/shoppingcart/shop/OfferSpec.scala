package org.northrop.leanne.shoppingcart.shop

import org.northrop.leanne.UnitSpec
import scala.collection.immutable._

/**
    Behaviour tests for org.northrop.leanne.shoppingcart.shop.
*/
class OfferSpec extends UnitSpec {
  "Offer" should "initial with name and condition" in {
      // setup
      val name = "Apples ~ Buy 1 Get 1 Free"
      val condition = Map(Product("apple") -> 2)
      val offer = Offer(name, condition, -33)

      // check
      offer.name shouldBe name
      offer.conditions shouldBe condition
      offer.discountInPence shouldBe -33
  }

  "Offer" should "return true if till state fulfills offer conditions" in {
    // setup
    val threeFor2OrangesOffer = Offer("Oranges ~ 3 for Price of 2", ListMap(Product("orange")->3), -25)
    val tillState = TillState(List.empty[Product], List.fill(3)(Product("orange")), List.empty[String], 0)

    // do it
    val isApplicable = threeFor2OrangesOffer.isApplicable(tillState)

    // check
    isApplicable shouldBe true
  }
}