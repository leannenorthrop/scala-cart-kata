package org.northrop.leanne.shoppingcart

import org.northrop.leanne.shoppingcart.shop._
import scala.collection.immutable._

object Main {
  private val prices = ProductPrice(Product("apple"), 60) :: ProductPrice(Product("orange"), 25) :: Nil
  private val offers = Offer("Apples ~ Buy 1 Get 1 Free", ListMap(Product("apple")->2), -60) :: 
                       Offer("Oranges ~ 3 for Price of 2", ListMap(Product("orange")->3), -25) :: Nil
  private val till = Till(prices, offers)
  private val scanner = Till.scan(till)_

  def run(cartContents : String) : Unit = {
      val (errors, total) = scanner(Cart(cartContents))
      println(f"Total = ${total/100d}%.2f")
      if (errors != List.empty[String]) println("Errors = \n" + errors.mkString("\n"))
  } 

  def main(args : Array[String]): Unit = args.length match {
    case 0 => println("""Usage:
              |scala org.northrop.leanne.shoppingcart.Main 'comma separated list of products to purchase'
              |""".stripMargin)
    case 1 => 
      run(args(0))
    case _ => 
      run(args(0))
      println("Don't know what to do with additional arguments: " + args.splitAt(1)._2.deep.mkString(", "))
  }
}