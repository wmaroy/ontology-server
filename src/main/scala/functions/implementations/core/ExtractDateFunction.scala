package functions.implementations.core

import dbpedia.dataparsers.{DateTimeParser, ObjectParser}
import dbpedia.dataparsers.ontology.datatypes.Datatype
import dbpedia.dataparsers.util.{Language, Redirects}
import dbpedia.dataparsers.util.wikiparser.impl.simple.SimpleWikiParser

/**
  * Created by wmaroy on 22.04.17.
  */
class ExtractDateFunction {

  val wikiparser = new SimpleWikiParser

  def execute(property : String, dateDatatype : String): Seq[String] = {

    val propertyNode = wikiparser.parseProperty(property)
    val context = new {
      val language : Language = Language.English
      val redirects : Redirects = new Redirects(Map())
    }
    val dateTimeParser = new DateTimeParser(context, new Datatype(dateDatatype))

    Seq(dateTimeParser.parse(propertyNode).get.toString)

  }

}
