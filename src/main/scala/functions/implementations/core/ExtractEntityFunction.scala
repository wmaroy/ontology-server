package functions.implementations.core

import dbpedia.dataparsers.util.{Language, Redirects}
import dbpedia.dataparsers.{ObjectParser, StringParser}
import dbpedia.dataparsers.util.wikiparser.impl.simple.SimpleWikiParser

/**
  * Created by wmaroy on 22.04.17.
  */
class ExtractEntityFunction {

  val wikiparser = new SimpleWikiParser

  def execute(property : String): Seq[String] = {

    val propertyNode = wikiparser.parseProperty(property)
    val context = new {
      val language : Language = Language.English
      val redirects : Redirects = new Redirects(Map())
    }
    val objectParser = new ObjectParser(context)

    Seq(objectParser.parse(propertyNode).get)

  }

}
