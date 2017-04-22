package functions.implementations.core

import org.scalatest.{FlatSpec, Matchers}

/**
  * Created by wmaroy on 22.04.17.
  */
class ExtractEntityFunctionTest extends FlatSpec with Matchers {

  "An entity" should "be extracted correctly" in {

    val property = "<br />[[Melinda Gates|Melinda Gates]]<br />January 1, 1994<br />"
    val fn = new ExtractEntityFunction()
    val result = fn.execute(property)

    result.head should be("http://en.dbpedia.org/resource/Melinda_Gates")

  }

}