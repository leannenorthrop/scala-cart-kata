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

	"Map2 combinator" should "run provided state transitions" in {
		val testValue = "Transition0"
		val transition1 = mockFunction[String, (String,String)]
		transition1 expects (testValue) returning (testValue,"Transition1") once
		val transition2 = mockFunction[String, (String,String)]
		transition2 expects ("Transition1") returning ("Transition1","Transition2") once

		val mapped : State[String,String] = map2(transition1,transition2)((a,b) => a + b)
		val result = mapped(testValue)

		assert(result._1 === (testValue+"Transition1"))
		assert(result._2 === "Transition2")
	}

	it should "combine state transition results using given function" in {
		val testValue1 = "Hi 1"
		val testValue2 = "Hi 2"
		val testValue3 = "Yes I've been run"
		val f = mockFunction[String, String, String]
		f expects (testValue1, testValue2) returning (testValue3) once

		val mapped : State[String,String] = map2(unit[String,String](testValue1),unit[String,String](testValue2))(f)
		val result = mapped("")

		assert(result._1 === testValue3)
	}
}
