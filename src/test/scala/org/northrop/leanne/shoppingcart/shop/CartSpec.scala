package org.northrop.leanne.shoppingcart.shop

import org.northrop.leanne.UnitSpec
import scala.collection.immutable._

/**
    Behaviour tests for org.northrop.leanne.shoppingcart.shop.Cart
*/
class CartSpec extends UnitSpec {
  "Cart" should "initialise with list of Products" in {
      // setup
      val contents = List(Product("apple"),Product("orange"))

      // check
      Cart(contents).contents shouldBe contents
  }

  "Cart apply" should "convert comma separated products into Cart with List of Products" in {
      // setup
      val itemsToBuy = "something, apple, , orange, an unknown thing"

      // do it
      val (errors, cart) = Cart(itemsToBuy)

      // check
      cart.contents shouldBe List(Product("apple"), Product("orange"))
  }

  "Cart apply" should "return list of Throwables for unknown Products" in {
      // setup
      val itemsToBuy = "something, apple, , orange, an unknown thing"

      // do it
      val (errors, cart) = Cart(itemsToBuy)

      // check
      errors contains (new IllegalArgumentException("requirement failed: Bad product name: something"))
      errors contains (new IllegalArgumentException("requirement failed: Bad product name:  "))
      errors contains (new IllegalArgumentException("requirement failed: Bad product name: an unknown thing"))
  }
}