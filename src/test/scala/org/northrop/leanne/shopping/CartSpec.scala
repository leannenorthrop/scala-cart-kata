package org.northrop.leanne.shopping.till

import org.northrop.leanne.shopping.UnitSpec
import org.northrop.leanne.shopping.till._
import org.northrop.leanne.shopping.Cart
import scala.collection.immutable._

/**
    Behaviour tests for shop till.
*/
class CartSpec extends UnitSpec {
	trait NoDiscountTill {
		val state = Map[String,LineValues]()
    	val till = Till(Map[String,Int]("orange"->40), Map[String,String](), state)
  	}

  trait DiscountTill {
    val state = Map[String,LineValues]()
      val till = Till(Map[String,Int]("orange"->40), Map[String,String]("orange"->"3;80"), state)
    }

	"Buying empty cart" should "return 0" in new NoDiscountTill {
		val products = List[String]()
    	val cart = Cart(till,products)

    	val total = cart.buy()

    	total should be === 0.0
  	} 

	"Buying an unknown product" should "return 0" in new NoDiscountTill {
		val products = List[String]("umbrella")
    	val cart = Cart(till,products)

    	val total = cart.buy()

    	total should be === 0.0
  	}   	

	"Buying an orange" should "return price of orange in float" in new NoDiscountTill {
		val products = List[String]("orange")
    	val cart = Cart(till,products)

    	val total = cart.buy()

    	total should be === 0.4f
  	} 

  	"Buying 2 oranges" should "return sum of prices in float" in new NoDiscountTill {
		  val products = List[String]("orange","orange")
    	val cart = Cart(till,products)

    	val total = cart.buy()

    	total should be === (2*40)/100.0f
  	} 

    "Buying 3 oranges" should "return sum of prices in float with discount offer applied" in new DiscountTill {
      val products = List[String]("orange","orange","orange")
      val cart = Cart(till,products)

      val total = cart.buy()

      total should be === 80/100.0f
    } 

    "Buying 5 oranges" should "return sum of prices in float with discount offer applied" in new DiscountTill {
      val products = List[String]("orange","orange","orange","orange","orange")
      val cart = Cart(till,products)

      val total = cart.buy()

      total should be === (2*40+80)/100.0f
    }         
}