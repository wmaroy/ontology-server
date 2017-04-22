import dbpedia.dataparsers.ontology.datatypes.Datatype
import dbpedia.dataparsers.ontology.{Ontology, OntologyDatatypes}
import dbpedia.dataparsers.util.{Language, Redirects}
import dbpedia.dataparsers._
import dbpedia.dataparsers.util.wikiparser.impl.simple.SimpleWikiParser
import org.scalatest.{FlatSpec, Matchers}

/**
  * Created by wmaroy on 21.04.17.
  */
class DBpediaParserTest extends FlatSpec with Matchers {

  val wikiparser = new SimpleWikiParser
  val context = new {
    val language : Language = Language.English
    val redirects : Redirects = new Redirects(Map())
    val ontology : Ontology = new Ontology(null,null,OntologyDatatypes.load().map(t => (t.name, t)).toMap,null,null,null)
  }


  "A parser" should "work" in {

    val property = "{{URL|gatesnotes.com}}"
    val propertyNode = wikiparser.parseProperty(property)

    val parsers = Map("string_parser:\t" -> StringParser,
                      "date_parser:\t" -> new DateTimeParser(context, new Datatype("xsd:date")),
                      "link_parser:\t" -> new LinkParser(),
                      "object_parser:\t" -> new ObjectParser(context))

    parsers.foreach(tuple => println(tuple._1 + " " + tuple._2.parse(propertyNode).getOrElse()))

  }

}
