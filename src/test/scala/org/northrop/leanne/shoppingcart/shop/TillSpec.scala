package org.northrop.leanne.shoppingcart.shop

import org.northrop.leanne.UnitSpec
import scala.collection.immutable._

/**
    Behaviour tests for org.northrop.leanne.shoppingcart.shop.Till
*/
class TillSpec extends UnitSpec {
  trait TillWithoutOrangePriceObjects {
    val prices = ProductPrice(Product("apple"), 33) :: Nil
    val offers = List.empty[Offer]
    val till = Till(prices, offers)
    val scanner = Till.scan(till)_
  }

  trait TillObjects {
    val prices = ProductPrice(Product("apple"), 33) :: ProductPrice(Product("orange"), 20) :: Nil
    val offers = List.empty[Offer]
    val till = Till(prices, offers)
    val scanner = Till.scan(till)_
  }

  trait TillWithOffersObjects {
    val applePrice = 33
    val orangePrice = 20
    val prices = ProductPrice(Product("apple"), applePrice) :: ProductPrice(Product("orange"), orangePrice) :: Nil
    val offers = Offer("Apples ~ Buy 1 Get 1 Free", ListMap(Product("apple")->2), -applePrice) :: 
                 Offer("Oranges ~ 3 for Price of 2", ListMap(Product("orange")->3), -orangePrice) :: Nil
    val till = Till(prices, offers)
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
      val offer = Offer(name, condition, -33)

      // check
      offer.name shouldBe name
      offer.conditions shouldBe condition
      offer.discountInPence shouldBe -33
  }

  "Till lookupOfferDiscount" should "return None if no offers apply" in new TillWithOffersObjects {
      // do it
      val offerOption = till.lookupOfferDiscount(TillState(Nil, Nil, 0), Product("apple"))

      // check
      offerOption shouldBe None
  }

  "Till lookupOfferDiscount" should "return discount in pence if offer applies" in new TillWithOffersObjects {
      // set up
      val allProductsSeen = Product("apple") :: Product("apple") :: Nil
      val discountedProductsSeen = Product("apple") :: Nil
      val tillState = TillState(allProductsSeen, discountedProductsSeen, 0)

      // do it
      val (newState, discountInPence) = till.lookupOfferDiscount(tillState, Product("apple")).getOrElse((tillState,0))

      // check
      discountInPence shouldBe -applePrice
  }

  "Till lookupOfferDiscount" should "return new state if offer applies" in new TillWithOffersObjects {
      // set up
      val allProductsSeen = Product("apple") :: Product("apple") :: Nil
      val discountedProductsSeen = Product("apple") :: Nil
      val tillState = TillState(allProductsSeen, discountedProductsSeen, 0)

      // do it
      val (newState, discountInPence) = till.lookupOfferDiscount(tillState, Product("apple")).getOrElse((tillState,0))

      // check
      newState.seenNonOfferProducts shouldBe empty
  }

  "TillState" should "initialise with lists and total price in pence" in {
      // set up
      val runningTotalInPence = 976
      val runningSeenNonDiscountedProducts = Product("apple") :: Product("orange") :: Nil
      val runningSeenProducts = Product("apple") :: Product("apple") :: Product("apple") :: Product("orange") :: Nil

      // do it
      val state = TillState(runningSeenProducts, 
                            runningSeenNonDiscountedProducts,
                            runningTotalInPence)
  
      // check
      state.totalInPence shouldBe runningTotalInPence
      state.seenNonOfferProducts shouldBe runningSeenNonDiscountedProducts
      state.seenProducts shouldBe runningSeenProducts
  }

  "Till scan" should "apply all offers to cart contents" in new TillWithOffersObjects {
      // setup
      val cartContents = "apple, abc, orange, apple, orange, orange, orange, apple"
      val cart = Cart(cartContents)

      // do it
      val total = scanner(cart)

      // check
      total shouldBe (applePrice * 2 + orangePrice * 3)
  }
}