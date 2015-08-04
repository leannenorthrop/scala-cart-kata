package org.northrop.leanne.shopping.till

import org.northrop.leanne.shopping.UnitSpec
import org.northrop.leanne.shopping.till._
import scala.collection.immutable._
import org.northrop.leanne.shopping.combinators._

/**
    Behaviour tests for shop till.
*/
class TillSpec extends UnitSpec {
	"Till purchasing an unknown product" should "return till transition function which returns None" in {
		val till = Till(Map[String,Int](), Map[String,String](), Map[String,LineValues]())

		val purchaseGrapes = Till.purchase("grapes")
		val result = purchaseGrapes(till)

		val opt:Option[Int] = result._1
		opt should be === None
	}

	it should "not modify till state" in {
		val till = Till(Map[String,Int](), Map[String,String](), Map[String,LineValues]())

		val purchaseGrapes = Till.purchase("grapes")
		val result = purchaseGrapes(till)

		result._2 should be === till
	}

	"Till purchasing a known product" should "return till transition function which returns Some with price in pence" in {
		val grapesPrice = 300
		val till = Till(Map[String,Int]("grapes"->grapesPrice), Map[String,String](), Map[String,LineValues]())

		val purchaseGrapes = Till.purchase("grapes")
		val result = purchaseGrapes(till)

		val opt: Option[Int] = result._1
		opt.value should be === grapesPrice
	}		

	it should "not modify till state" in {
		val grapesPrice = 300
		val till = Till(Map[String,Int]("grapes"->grapesPrice), Map[String,String](), Map[String,LineValues]())

		val purchaseGrapes = Till.purchase("grapes")
		val result = purchaseGrapes(till)

		result._2 should be === till		
	}

	"Till discount a known product" should "return None if discount can not be applied for product" in {
		val grapesPrice = 300
		val till = Till(Map[String,Int]("grapes"->grapesPrice), Map[String,String]("grapes"->"3;130"), Map[String,LineValues]())

		val discountedGrapes = Till.discount("grapes")
		val result = discountedGrapes(till)
		
		result._1 should be === None		
	}

	it should "return till transition function which return Some discount amount in pence" in {
		val grapesPrice = 300
		val till = Till(Map[String,Int]("grapes"->grapesPrice), Map[String,String]("grapes"->"3;130"), Map[String,LineValues]())

		val discountedGrapes = List[String]("grapes","grapes","grapes").flatMap( i => List(Till.discount(i)) )
		val result = sequence(discountedGrapes)(till)

		val opt: Option[Int] = result._1.last
		opt.value should be === -770		
	}

	it should "return None if no discount is available for product" in {
		val grapesPrice = 300
		val till = Till(Map[String,Int]("grapes"->grapesPrice), Map[String,String]("grapes"->"3;130"), Map[String,LineValues]())

		val discountedGrapes = Till.discount("orange")
		val result = discountedGrapes(till)
		
		result._1 should be === None		
	}

	"TillHelper" should "convert 'abc:def,xyz:aaa' string to map" in {
		val testString = "ABC:DEF,XYZ:AAA"

		val map = TillHelper.toMap(testString)

		map("abc") should be === "def"
		map("xyz") should be === "aaa"
	}

	"TillHelper" should "convert prices string 'orange:600,apple:20' string to map" in {
		val testString = "orange:600,apple:20"

		val map = TillHelper.toPriceMap(testString)

		map("orange") should be === 600
		map("apple") should be === 20
	}

	it should "return None if input prices string is empty" in {
		val testString = "    	"

		val till = TillHelper.apply(testString)

		assert(till === None)
	}

	it should "convert prices string 'orange:y600,apple:20' string to map" in {
		val testString = "orange:y600,apple:20"

		val map = TillHelper.toPriceMap(testString)

		map("orange") should be === 600
		map("apple") should be === 20
	}	

	it should "return None if input offers string is empty" in {
		val testString = "    "

		val till = TillHelper.apply("orange:600,apple:20",testString)

		assert(till === None)
	}

	it should "convert offers string 'orange:3;400,apple:2;100' string to map" in {
		val testString = "orange:3;400,apple:2;100"

		val map = TillHelper.toOffersMap(testString)

		map("orange") should be === "3;400"
		map("apple") should be === "2;100"
	}		

	it should "create till for given prices" in {
		val prices = "orange:600,apple:20"

		val till = TillHelper(prices)

		assert(till != None)
		till.get.priceLookup("orange") should be === 600
	}

	it should "create till for given prices and offers" in {
		val prices = "orange:600,apple:20"
		val offers = "orange:3;400,apple:2;100"

		val till = TillHelper(prices,offers)

		assert(till != None)
		till.get.priceLookup("orange") should be === 600
		till.get.offerLookup("orange").get(0) should be === 3
		till.get.offerLookup("orange").get(1) should be === 400
	}		
}