package org.northrop.leanne.shoppingcart.shop

import org.northrop.leanne.UnitSpec

/**
    Behaviour tests for org.northrop.leanne.shoppingcart.shop.Product
*/
class ProductSpec extends UnitSpec {
  "Product" should "initialise with known item name" in {
      // setup
      val name = "apple"

      // check
      Product(name).name shouldBe name
  }

  "Product" should "initialise with known item name ignoring case" in {
      // setup
      val name = "ApPle"

      // check
      Product(name).name shouldBe name
  }

  "Product" should "not construct for unknown items" in {
      // set up
      val name = "unknown product name"

      // check
      the [IllegalArgumentException] thrownBy {
        Product(name)
      } should have message "requirement failed: Bad product name: " + name
  }
}