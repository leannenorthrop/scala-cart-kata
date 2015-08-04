package org.northrop.leanne.shopping

package object till {
	type ShopTill[+A] = Till => (A,Till)
	type LineValues = Tuple1[Int]
}