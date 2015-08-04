package org.northrop.leanne.shopping

package object till {
	type ShopTill[+A] = Till => (A,Till)
}