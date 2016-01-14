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
      val expectedMsg = """
                          |Total including any offers....   0.00
                          |Errors = 
                          |requirement failed: Bad product name: something-to-buy
                          |requirement failed: Bad product name: 
                          |requirement failed: Bad product name: top
                          |""".stripMargin

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
      val expectedMsg = """apple.........................   0.60
                          |apple.........................   0.60
                          |orange........................   0.25
                          |apple.........................   0.60
                          |Total including any offers....   1.45
                          |Errors = 
                          |requirement failed: Bad product name: something
                          |requirement failed: Bad product name: 
                          |""".stripMargin

      // do it
      Console.withOut(stream) {
        app.main(args)
      }

      // check
      stream.toString shouldBe expectedMsg
  }

  "Main entry point" should "print formatted total price and warning for additional parameters" in new MainObject {
      // setup
      val stream = new java.io.ByteArrayOutputStream()
      val args = Array[String]("something-to-buy", "extra-arg", "further-arg")
      val expectedMsg = """
                          |Total including any offers....   0.00
                          |Errors = 
                          |requirement failed: Bad product name: something-to-buy
                          |Don't know what to do with additional arguments: extra-arg, further-arg
                          |""".stripMargin

      // do it
      Console.withOut(stream) {
        app.main(args)
      }

      // check
      stream.toString shouldBe expectedMsg
  }

  "Main entry point" should "print formatted total price for known products and warning for additional parameters" in new MainObject {
      // setup
      val stream = new java.io.ByteArrayOutputStream()
      val args = Array[String]("apple, pear, orange", "extra-arg", "further-arg")
      val expectedMsg = """apple.........................   0.60
                          |orange........................   0.25
                          |Total including any offers....   0.85
                          |Errors = 
                          |requirement failed: Bad product name: pear
                          |Don't know what to do with additional arguments: extra-arg, further-arg
                          |""".stripMargin

      // do it
      Console.withOut(stream) {
        app.main(args)
      }

      // check
      stream.toString shouldBe expectedMsg
  }

  "Main entry point" should "print errors" in new MainObject {
      // setup
      val stream = new java.io.ByteArrayOutputStream()
      val args = Array[String]("apple, strawberry, pear, orange, strawberry")
      val expectedMsg = """apple.........................   0.60
                          |strawberry....................   0.00
                          |orange........................   0.25
                          |strawberry....................   0.00
                          |Total including any offers....   0.85
                          |Errors = 
                          |No price for product Product(strawberry).
                          |No price for product Product(strawberry).
                          |requirement failed: Bad product name: pear
                          |""".stripMargin

      // do it
      Console.withOut(stream) {
        app.main(args)
      }

      // check
      stream.toString shouldBe expectedMsg
  }

  "Main entry point" should "print errors and warning for additional parameters" in new MainObject {
      // setup
      val stream = new java.io.ByteArrayOutputStream()
      val args = Array[String]("apple, strawberry, pear, orange, strawberry", "extra-arg", "further-arg")
      val expectedMsg = """apple.........................   0.60
                          |strawberry....................   0.00
                          |orange........................   0.25
                          |strawberry....................   0.00
                          |Total including any offers....   0.85
                          |Errors = 
                          |No price for product Product(strawberry).
                          |No price for product Product(strawberry).
                          |requirement failed: Bad product name: pear
                          |Don't know what to do with additional arguments: extra-arg, further-arg
                          |""".stripMargin

      // do it
      Console.withOut(stream) {
        app.main(args)
      }

      // check
      stream.toString shouldBe expectedMsg
  }

}