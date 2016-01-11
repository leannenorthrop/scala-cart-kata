package org.northrop.leanne.shoppingcart

object Main {
  def main(args: Array[String]): Unit = args.length match {
    case 0 => println("""Usage:
              |scala org.northrop.leanne.shoppingcart.Main 'comma separated list of products to purchase'
              |""".stripMargin)
    case 1 => println("Total = 0")
    case _ => println("Total = 0. Don't know what to do with additional arguments: " + args.splitAt(1)._2.deep.mkString(", "))
  }
}