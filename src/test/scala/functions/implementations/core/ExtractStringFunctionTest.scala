package functions.implementations.core

import org.scalatest.{FlatSpec, Matchers}

/**
  * Created by wmaroy on 22.04.17.
  */
class ExtractStringFunctionTest extends FlatSpec with Matchers {

  "A string" should "be extracted correctly" in {

    val property = "<br />[[Melinda Gates|Melinda Gates]]<br /><br />"
    val fn = new ExtractStringFunction()
    val result = fn.execute(property)

    result.head should be("Melinda Gates")

  }

}
