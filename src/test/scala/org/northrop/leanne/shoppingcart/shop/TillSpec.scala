package org.northrop.leanne.shoppingcart.shop

import org.northrop.leanne.UnitSpec
import scala.collection.immutable._

/**
    Behaviour tests for org.northrop.leanne.shoppingcart.shop.Till
*/
class TillSpec extends UnitSpec {
  trait TillWithoutOrangePriceObjects {
    val prices = ProductPrice(Product("apple"), 33) :: List()
    val till = Till(prices)
    val scanner = Till.scan(till)_
  }

  trait TillObjects {
    val prices = ProductPrice(Product("apple"), 33) :: ProductPrice(Product("orange"), 20) :: List()
    val till = Till(prices)
    val scanner = Till.scan(till)_
  }

  "ProductPrice" should "initialise with Product and price in pence value" in {
      // setup
      val name = "apple"
      val productPrice = ProductPrice(Product(name), 100)

      // check
      productPrice.product.name shouldBe name
      productPrice.priceInPence shouldBe 100
  }

  "Till" should "initialise with List of ProductPrices" in new TillObjects {
      // check
      till.prices shouldBe prices
  }

  "Till lookupPrice" should "return Some price in pence value for a known Product" in new TillObjects {
      // setup
      val product = Product("apple")

      // do it
      val priceOption = till.lookupPrice(product)

      // check
      priceOption shouldBe Some(33)
  }

  "Till lookupPrice" should "return None for a unknown Product" in new TillWithoutOrangePriceObjects {
      // setup
      val product = Product("orange")

      // do it
      val priceOption = till.lookupPrice(product)

      // check
      priceOption shouldBe None
  }

  "Till scan" should "return total value of known products" in new TillObjects {
      // setup
      val cartContents = "apple, abc, orange, , apple, apple"
      val cart = Cart(cartContents)

      // do it
      val total = scanner(cart)

      // check
      total shouldBe 119
  }

  "Offer" should "initial with name and condition" in {
      // setup
      val name = "Apples ~ Buy 1 Get 1 Free"
      val condition = Map(Product("apple") -> 2)
      val offer = Offer(name, condition)

      // check
      offer.name shouldBe name
      offer.conditions shouldBe condition

  }
}