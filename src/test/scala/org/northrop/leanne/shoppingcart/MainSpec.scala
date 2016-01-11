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

  "Main entry point" should "exist" in new MainObject {
      // setup
      val stream = new java.io.ByteArrayOutputStream()
      val args = Array[String]()

      // do it
      Console.withOut(stream) {
        app.main(args)
      }

      // check
      stream.toString shouldBe "hello world\n"
  }
}