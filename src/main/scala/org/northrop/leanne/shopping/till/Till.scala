package org.northrop.leanne.shopping.till

import org.northrop.leanne.shopping.till._
import scala.collection.immutable._

/** Shop till holding current prices, offers and state*/
case class Till(prices:Map[String,Int], offers:Map[String,String], private val state:Map[String,LineValues]) {
	def priceLookup(product:String) : Int = this.prices.getOrElse(product,0)
	def offerLookup(product:String) : Option[List[Int]] = {
		if (this.offers.getOrElse(product, "") == "") None 
		else 
			Some(this.offers.getOrElse(product, "").split(";").toList.map({ s =>
				val str = s.replaceAll("[^\\d]","")
				if (str.trim.length == 0) 0 else str.toInt
			}))
	}
	def productStateLookup(product:String) : LineValues = this.state.getOrElse(product,Tuple1[Int](0))
}

/** Companion object supplying useful functions for operating on shop till. */
object Till {	
	/** Returns transition function to lookup price of given product */
	def purchase(product:String) : ShopTill[Option[Int]] = {
		till => {
			val price : Int = till.priceLookup(product)
			val result = if (price == 0) None else Option(price)
			(result,till)
		}
	}	

	/** Returns transition function to caclulate discount 'price' for given product based on current state
	    of the till's seen products.*/
	def discount(product:String) : ShopTill[Option[Int]] = {
		till => {
			var discount : Option[Int] = None
			val productstate : LineValues = till.productStateLookup(product)
			val count : Int = productstate._1 + 1
			var updatedTill = Till.update(till, product, Tuple1[Int](count))
			val price : Int = till.prices.getOrElse(product,0)

			val offerRule : Option[List[Int]] = till.offerLookup(product)
			if (offerRule != None) {
				val offerParts = offerRule.get
				val i = offerParts(0)
				if (count == i) {
					val fullprice : Int = price * count
					discount = Some(offerParts(1) - fullprice)
					updatedTill = Till.update(till, product, Tuple1[Int](0))
				}
			}
			(discount,updatedTill)
		}
	}	

	def update(till:Till, product:String, values:LineValues) : Till = Till(till.prices,till.offers,((till.state - product) ++ List(product -> values)))
}

object TillHelper {
	def toMap(str:String):Map[String,String] = {
		val parts = str.toLowerCase().split(",").flatMap(s => s.trim.split(":")).toList
		val m = parts.sliding(2,2).collect{case List(a,b) => (a,b)}.toList.toMap
		m
	}

	def toPriceMap(prices:String):Map[String,Int] = {
		val m = toMap(prices)
		m.map({case (key, value) => (key, value.replaceAll("[^\\d]","").toInt)})
	}

	def toOffersMap(offers:String):Map[String,String] = toMap(offers)	

	def apply(prices:String) : Option[Till] = {
		if (prices.trim.length > 0) {
			try {
				Some(Till(toPriceMap(prices), Map[String,String](), Map[String,LineValues]()))
			} catch {
				case _ : Throwable => None
			}
		} else {
			None
		}
	}
	def apply(prices:String,offers:String) : Option[Till] = {
		if (prices.trim.length > 0) {
			Some(Till(toPriceMap(prices), toOffersMap(offers), Map[String,LineValues]()))
		} else {
			None
		}
	}	
}