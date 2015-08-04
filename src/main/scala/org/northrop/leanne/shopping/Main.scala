package org.northrop.leanne.shopping

import org.northrop.leanne.shopping.till._

object Main extends App {
	def cart(str:String):List[String] = str.split(",").toList.map(s => s.trim.toLowerCase())

	args.length match {
		case 0 => println("""Usage: Main cart [prices] [offers]
							| where -
							|   cart is a single comma separated string argument containing shopping cart contents
							|   prices is an optional single comma separated string consisting of one or more productname:pence-value pairs
							|   offers is an optional single comma separated string consisting of one or more productname:offer where offer is the format quantity;pence-value
							""".stripMargin)
		case 1 => 
			val cartContents = cart(args(0))
			val prices = "orange:40,apple:50"
			val till = TillHelper(prices)
			if (till != None) {
				val total = Cart(till.get,cartContents).buy()
				println(f"Charge for cart is ${total}%.2f")
			}
		case 2 =>
			val cartContents = cart(args(0))
			val till = TillHelper(args(1))
			if (till != None) {
				val total = Cart(till.get,cartContents).buy()
				println(f"Charge for cart is ${total}%.2f")
			} else {
				println("Empty prices option?")
			}
		case 3 =>
			val cartContents = cart(args(0))
			val till = TillHelper(args(1),args(2))
			if (till != None) {
				val total = Cart(till.get,cartContents).buy()
				println(f"Charge for cart is ${total}%.2f")
			} else {
				println("Empty prices option?")
			}
		case _ => println("Too many arguments specified")		
	}

}