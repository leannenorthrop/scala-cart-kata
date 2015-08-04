package org.northrop.leanne.shopping.till

import org.northrop.leanne.shopping.till._
import scala.collection.immutable._

/** Shop till holding current prices, offers and state*/
case class Till(prices:Map[String,Int], offers:Map[String,String], private val state:Map[String,(Int)]) {
}

/** Companion object supplying useful functions for operating on shop till. */
object Till {	
	/** Returns transition function to lookup price of given product */
	def purchase(product:String) : ShopTill[Option[Int]] = {
		till => {
			val price : Int = till.prices.getOrElse(product, 0)
			val result = if (price == 0) None else Option(price)
			(result,till)
		}
	}		
}