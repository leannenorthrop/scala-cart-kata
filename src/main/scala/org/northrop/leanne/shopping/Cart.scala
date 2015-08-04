package org.northrop.leanne.shopping

import org.northrop.leanne.shopping.till._

/** Simple Cart class to operate on groups of products stored in this cart. */
case class Cart(private val till:Till, private val cart:List[String]) {
	import org.northrop.leanne.shopping.combinators._

	val scan = sequence(cart.flatMap( i => List(Till.purchase(i),Till.discount(i)) ))

	/** Returns total charge for cart contents as a float.*/
	def buy() : Float = {
		val (list,_) = scan(till)
		val total = list.flatten.foldLeft(Some(0))((acc,i)=>Some(acc.getOrElse(0)+i)).getOrElse(0)
		total/100.0f
	}	
}