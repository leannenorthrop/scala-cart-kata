package org.northrop.leanne.shoppingcart.shop

import org.northrop.leanne._
import scala.collection.immutable._

/**
    Behaviour tests for org.northrop.leanne.shoppingcart.shop.Till
*/
class TillSpec extends UnitSpec {
  trait TillWithoutOrangePriceObjects {
    val prices = Map(Product("apple")->33)
    val offers = List.empty[Offer]
    val till = Till(prices, offers)
    val scanner = Till.scan(till)_
  }

  trait TillObjects {
    val prices = Map(Product("apple") -> 33, Product("orange")->20)
    val offers = List.empty[Offer]
    val till = Till(prices, offers)
    val scanner = Till.scan(till)_
  }

  trait TillWithOffersObjects {
    val applePrice = 33
    val orangePrice = 20
    val prices = Map(Product("apple") -> applePrice, Product("orange") -> orangePrice)
    val offers = Offer("Apples ~ Buy 1 Get 1 Free", ListMap(Product("apple")->2), -applePrice) :: 
                 Offer("Oranges ~ 3 for Price of 2", ListMap(Product("orange")->3), -orangePrice) :: Nil
    val till = Till(prices, offers)
    val scanner = Till.scan(till)_
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
      val (_, total) = scanner(cart)

      // check
      total shouldBe 119
  }

  "Till lookupOfferDiscount" should "return None if no offers apply" taggedAs(OfInterest) in new TillWithOffersObjects {
      // do it
      val offerOption = till.lookupOfferDiscount(TillState(List.empty[Product], List.empty[Product], List.empty[String], 0), Product("apple"))

      // check
      offerOption shouldBe None
  }

  "Till lookupOfferDiscount" should "return discount in pence if offer applies" in new TillWithOffersObjects {
      // set up
      val tillState = TillState(List.fill(2)(Product("apple")), List.fill(2)(Product("apple")), List.empty[String], 0)

      // do it
      val (newState, discountInPence) = till.lookupOfferDiscount(tillState, Product("apple")).getOrElse((tillState,0))

      // check
      discountInPence shouldBe -applePrice
  }

  "Till lookupOfferDiscount" should "return new state if offer applies" in new TillWithOffersObjects {
      // set up
      val tillState = TillState(List.fill(2)(Product("apple")), List.fill(2)(Product("apple")), List.empty[String], 0)

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
                            List.empty[String], 
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
      val (_, total) = scanner(cart)

      // check
      total shouldBe (applePrice * 2 + orangePrice * 3)
  }

  "Till scan" should "return error for valid products without prices" in new TillWithoutOrangePriceObjects {
      // setup
      val cartContents = "apple, abc, orange, apple, apple"
      val cart = Cart(cartContents)

      // do it
      val (errors, _) = scanner(cart)

      // check
      errors shouldBe List("No price for product Product(orange).")
  }

  "TillState apply" should "return new state with discounted products removed from non offer products seen list" in {
      // setup
      val threeFor2OrangesOffer = Offer("Oranges ~ 3 for Price of 2", ListMap(Product("orange")->3), -25)
      val tillState = TillState(List.empty[Product], List.fill(3)(Product("orange")), List.empty[String], 0)

      // do it
      val newState = tillState(threeFor2OrangesOffer)

      // check
      newState.seenNonOfferProducts shouldBe empty
  }
}