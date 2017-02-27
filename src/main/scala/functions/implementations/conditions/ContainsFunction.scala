package functions.implementations.conditions

import dbpedia.dataparsers.StringParser
import dbpedia.dataparsers.util.wikiparser.PropertyNode
import dbpedia.dataparsers.util.wikiparser.impl.simple.SimpleWikiParser
import functions.Function

/**
  * Checks if an Infobox property contains a given value.
  */
class ContainsFunction(property : String, value : String) extends Function {

  def execute(): Boolean = {
    property.contains(value)
  }

}
