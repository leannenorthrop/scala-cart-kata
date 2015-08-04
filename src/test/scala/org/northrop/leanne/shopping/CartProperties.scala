import org.scalacheck.Properties
import org.scalacheck.Prop.forAll
import org.northrop.leanne.shopping.Cart
import org.northrop.leanne.shopping.till._

object CartProperties extends Properties("String") {

  property("cart.buy") = forAll { (a: String) =>
  	val till = TillHelper("orange:40").get
    val cartContents = List(a)
  	Cart(till,cartContents).buy() >= 0.0f
  }

}