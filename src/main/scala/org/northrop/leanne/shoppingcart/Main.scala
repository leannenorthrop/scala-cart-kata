package org.northrop.leanne.shoppingcart

import org.northrop.leanne.shoppingcart.shop._
import scala.collection.immutable._

object Main {
  private val prices = ProductPrice(Product("apple"), 60) :: ProductPrice(Product("orange"), 25) :: List()
  private val offers = List[Offer]()
  private val till = Till(prices, offers)
  private val scanner = Till.scan(till)_

  def main(args: Array[String]): Unit = args.length match {
    case 0 => println("""Usage:
              |scala org.northrop.leanne.shoppingcart.Main 'comma separated list of products to purchase'
              |""".stripMargin)
    case 1 => println(f"Total = ${scanner(Cart(args(0)))/100}%.2f")
    case _ => println("Total = 0. Don't know what to do with additional arguments: " + args.splitAt(1)._2.deep.mkString(", "))
  }
}