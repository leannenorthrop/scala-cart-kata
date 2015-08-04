package org.northrop.leanne.shopping.till

import org.northrop.leanne.shopping.UnitSpec
import org.northrop.leanne.shopping.till._
import scala.collection.immutable._

/**
    Behaviour tests for shop till.
*/
class TillSpec extends UnitSpec {
	"Till purchasing an unknown product" should "return till transition function which returns None" in {
		val till = Till(Map[String,Int](), Map[String,String](), Map[String,(Int)]())

		val purchaseGrapes = Till.purchase("grapes")
		val result = purchaseGrapes(till)

		val opt:Option[Int] = result._1
		opt should be === None
	}

	it should "not modify till state" in {
		val till = Till(Map[String,Int](), Map[String,String](), Map[String,(Int)]())

		val purchaseGrapes = Till.purchase("grapes")
		val result = purchaseGrapes(till)

		result._2 should be === till
	}

	"Till purchasing a known product" should "return till transition function which returns Some with price in pence" in {
		val grapesPrice = 300
		val till = Till(Map[String,Int]("grapes"->grapesPrice), Map[String,String](), Map[String,(Int)]())

		val purchaseGrapes = Till.purchase("grapes")
		val result = purchaseGrapes(till)

		val opt: Option[Int] = result._1
		opt.value should be === grapesPrice
	}		

	it should "not modify till state" in {
		val grapesPrice = 300
		val till = Till(Map[String,Int]("grapes"->grapesPrice), Map[String,String](), Map[String,(Int)]())

		val purchaseGrapes = Till.purchase("grapes")
		val result = purchaseGrapes(till)

		result._2 should be === till		
	}
}