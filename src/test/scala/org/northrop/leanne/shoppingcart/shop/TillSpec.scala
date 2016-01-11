package org.northrop.leanne.shoppingcart.shop

import org.northrop.leanne.UnitSpec

/**
    Behaviour tests for org.northrop.leanne.shoppingcart.shop.Till
*/
class TillSpec extends UnitSpec {
  "ProductPrice" should "initialise with Product and price in pence value" in {
      // setup
      val name = "apple"
      val productPrice = ProductPrice(Product(name), 100)

      // check
      productPrice.product.name shouldBe name
      productPrice.priceInPence shouldBe 100
  }

  "Till" should "initialise with List of ProductPrices" in {
      // setup
      val prices = ProductPrice(Product("apple"), 33) :: ProductPrice(Product("orange"),44) :: List()

      // check
      Till(prices).prices shouldBe prices
  }
}