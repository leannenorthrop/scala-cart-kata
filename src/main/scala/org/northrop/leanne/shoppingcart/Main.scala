package org.northrop.leanne.shoppingcart

import org.northrop.leanne.shoppingcart.shop._
import scala.collection.immutable._

object Main {
  private val prices = Map(Product("apple")->60, Product("orange")->25)
  private val offers = Offer("Apples ~ Buy 1 Get 1 Free", Map(Product("apple")->2), -60) :: 
                       Offer("Oranges ~ 3 for Price of 2", Map(Product("orange")->3), -25) :: Nil
  private val till = Till(prices, offers)
  private val scanner = Till.scan(till)_

  def printReceipt(cart: Cart, total: Int) : Unit = {
    def priceStr(price: Double) : String = f"${price}%.2f".reverse.padTo(6," ").mkString.reverse
    def itemStr(item: String) : String = item.padTo(30,".").mkString
    println(cart.contents.map(product => s"${itemStr(product.name)} ${priceStr(till.price(product).getOrElse(0) / 100d)}" ).mkString("\n"))
    println(s"${itemStr("Total")} ${priceStr(total/100d)}")
  }
    
  def run(cartContents: String) : Unit = {
    val (unknownProducts, cart) = Cart(cartContents)
    val (errors, total) = scanner(cart)
    printReceipt(cart, total)

    val errs = errors ++ unknownProducts.map(_.getMessage())
    if (errs != List.empty[String]) println("Errors = \n" + errs.mkString("\n"))
  } 

  def main(args: Array[String]) : Unit = args.length match {
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