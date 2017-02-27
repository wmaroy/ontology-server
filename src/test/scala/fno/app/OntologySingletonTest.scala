package fno.app

import dbpedia.dataparsers.ontology.OntologySingleton
import org.scalatest.{FlatSpec, Matchers}


/**
  * Created by wmaroy on 17.02.17.
  */
class OntologySingletonTest extends FlatSpec with Matchers  {

  "An Ontology" should "load correctly without errors" in {
    OntologySingleton.load
  }

  it should "return the right size of properties" in {
    println(OntologySingleton.getPropertiesSize)
  }

}
