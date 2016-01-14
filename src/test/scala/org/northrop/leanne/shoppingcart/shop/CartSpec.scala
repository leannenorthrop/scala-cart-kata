package org.northrop.leanne.shoppingcart.shop

import org.northrop.leanne.UnitSpec
import scala.collection.immutable._

/**
    Behaviour tests for org.northrop.leanne.shoppingcart.shop.Cart
*/
class CartSpec extends UnitSpec {
  "Cart" should "initialise with list of Products" in {
      // setup
      val contents = Some(Product("apple")) :: Some(Product("orange")) :: List()

      // check
      Cart(contents).contents shouldBe contents
  }

  "Cart apply" should "convert comma separated products into Cart with List of Products" in {
      // setup
      val itemsToBuy = "something, apple, , orange, an unknown thing"

      // do it
      val cart = Cart(itemsToBuy)

      // check
      cart.contents shouldBe List(None, Some(Product("apple")), None, Some(Product("orange")), None)
  }

  "Cart items" should "return list of products" in {
      // setup
      val itemsToBuy = "something, apple, , orange, an unknown thing"

      // do it
      val cart = Cart(itemsToBuy)

      // check
      cart.items shouldBe List(Product("apple"), Product("orange"))
  }
}