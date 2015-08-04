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
		val till = Till(Map[String,Int]("grapes"->grapesPrice), Map[String,String]("grapes"->"3,130"), Map[String,LineValues]())

		val discountedGrapes = Till.discount("grapes")
		val result = discountedGrapes(till)
		
		result._1 should be === None		
	}

	it should "return till transition function which return Some discount amount in pence" in {
		val grapesPrice = 300
		val till = Till(Map[String,Int]("grapes"->grapesPrice), Map[String,String]("grapes"->"3,130"), Map[String,LineValues]())

		val discountedGrapes = List[String]("grapes","grapes","grapes").flatMap( i => List(Till.discount(i)) )
		val result = sequence(discountedGrapes)(till)

		val opt: Option[Int] = result._1.last
		opt.value should be === -770		
	}

	it should "return None if no discount is available for product" in {
		val grapesPrice = 300
		val till = Till(Map[String,Int]("grapes"->grapesPrice), Map[String,String]("grapes"->"3,130"), Map[String,LineValues]())

		val discountedGrapes = Till.discount("orange")
		val result = discountedGrapes(till)
		
		result._1 should be === None		
	}
}