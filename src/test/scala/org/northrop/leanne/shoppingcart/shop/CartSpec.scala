package org.northrop.leanne.shoppingcart.shop

import org.northrop.leanne.UnitSpec
import scala.collection.immutable._

/**
    Behaviour tests for org.northrop.leanne.shoppingcart.shop.Cart
*/
class CartSpec extends UnitSpec {
  "Cart" should "initialise with list of Products" in {
      // setup
      val contents = Product("something") :: Product("something-else") :: List()

      // check
      Cart(contents).contents shouldBe contents
  }
}