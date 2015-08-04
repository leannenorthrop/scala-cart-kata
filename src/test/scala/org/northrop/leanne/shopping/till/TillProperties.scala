import org.scalacheck.Properties
import org.scalacheck.Prop.forAll
import org.northrop.leanne.shopping.till._

object TillProperties extends Properties("String") {

  property("tillHelper.toMap") = forAll { (a: String) =>
  	TillHelper.toMap(a).size >= 0
  }

  property("till.purchase") = forAll { (a: String) =>
  	val till = TillHelper("orange:40").get
  	Till.purchase(a)(till)._2 == till
  }

  property("till.discount") = forAll { (a: String) =>
  	val till = TillHelper("orange:40").get
  	Till.purchase(a)(till)._2 == till
  }

}