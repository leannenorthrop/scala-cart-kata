package org.northrop.leanne.shoppingcart

object Main {
  def main(args: Array[String]): Unit = args.length match {
    case _ => println("""Usage:
              |scala org.northrop.leanne.shoppingcart.Main 'comma separated list of products to purchase'
              |""".stripMargin)
  }
}