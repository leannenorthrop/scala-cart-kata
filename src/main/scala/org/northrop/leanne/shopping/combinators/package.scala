package org.northrop.leanne.shopping

/**
    Utility library of combinator functions based on material
    from Paul CHiusano and Runar Bjarnason's book 'Functional Programming in Scala',
    Chapter 6.
*/
package object combinators {
	type State[S,+A] = S => (A,S)

	/** Returns state transition where supplied value and state are returned without modification. */
	def unit[S,A](a:A) : State[S,A] = (s:S) => (a,s)
}