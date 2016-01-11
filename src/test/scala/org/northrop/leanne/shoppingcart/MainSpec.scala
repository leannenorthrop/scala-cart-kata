package org.northrop.leanne.shoppingcart

import org.northrop.leanne.UnitSpec
import org.scalatest._
import Matchers._

/**
    Behaviour tests for org.northrop.leanne.shoppingcart.Main
*/
class MainSpec extends UnitSpec {
  trait MainObject {
    val app = Main
  }

  "Main entry point" should "print usage instructions when no arguments supplied" in new MainObject {
      // setup
      val stream = new java.io.ByteArrayOutputStream()
      val args = Array[String]()
      val expectedMsg = """Usage:
              |scala org.northrop.leanne.shoppingcart.Main 'comma separated list of products to purchase'
              |""".stripMargin + "\n"

      // do it
      Console.withOut(stream) {
        app.main(args)
      }

      // check
      stream.toString shouldBe expectedMsg
  }

  "Main entry point" should "print formatted total price of 0 for list of unknown items" in new MainObject {
      // setup
      val stream = new java.io.ByteArrayOutputStream()
      val args = Array[String]("something-to-buy, , top")
      val expectedMsg = "Total = 0.00\n"

      // do it
      Console.withOut(stream) {
        app.main(args)
      }

      // check
      stream.toString shouldBe expectedMsg
  }

  "Main entry point" should "print formatted total price for list of known items" in new MainObject {
      // setup
      val stream = new java.io.ByteArrayOutputStream()
      val args = Array[String]("apple, apple, something, , orange, apple")
      val expectedMsg = "Total = 2.00\n"

      // do it
      Console.withOut(stream) {
        app.main(args)
      }

      // check
      stream.toString shouldBe expectedMsg
  }

  "Main entry point" should "print 0 total price and warning for additional parameters" in new MainObject {
      // setup
      val stream = new java.io.ByteArrayOutputStream()
      val args = Array[String]("something-to-buy", "extra-item", "further-item")
      val expectedMsg = "Total = 0. Don't know what to do with additional arguments: extra-item, further-item\n"

      // do it
      Console.withOut(stream) {
        app.main(args)
      }

      // check
      stream.toString shouldBe expectedMsg
  }
}