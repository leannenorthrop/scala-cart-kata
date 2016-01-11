package org.northrop.leanne.shoppingcart.shop

import org.northrop.leanne.UnitSpec

/**
    Behaviour tests for org.northrop.leanne.shoppingcart.shop.Product
*/
class ProductSpec extends UnitSpec {
  "Product" should "initialise with name" in {
      // setup
      val name = "some-product-name"

      // check
      Product(name).name shouldBe name
  }
}