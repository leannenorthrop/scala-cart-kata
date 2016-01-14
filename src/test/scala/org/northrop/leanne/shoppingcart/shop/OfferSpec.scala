package org.northrop.leanne.shoppingcart.shop

import org.northrop.leanne._
import scala.collection.immutable._

/**
    Behaviour tests for org.northrop.leanne.shoppingcart.shop.Offer
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
}