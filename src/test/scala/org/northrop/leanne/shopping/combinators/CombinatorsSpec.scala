package org.northrop.leanne.shopping.combinators

import org.northrop.leanne.shopping.UnitSpec
import org.northrop.leanne.shopping.combinators._

/**
    Behaviour tests for combinators library functions.
*/
class CombinatorsSpec extends UnitSpec {
	"Unit combinator" should "return pass-through function which returns given argument without modification" in {
		val testValue = "ABC"

		val stringUnit : State[String,String] = unit(testValue)
		val result = stringUnit("State")

		assert(result._1 === testValue)
	}

	it should "return supplied state without modification" in {
		val testValue = "State"

		val stringUnit : State[String,String] = unit("DEF")
		val result = stringUnit(testValue)

		assert(result._2 === testValue)
	}
}
