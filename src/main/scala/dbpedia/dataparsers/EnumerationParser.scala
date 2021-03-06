package dbpedia.dataparsers

import dbpedia.dataparsers.ontology.datatypes.EnumerationDatatype
import dbpedia.dataparsers.util.wikiparser.{Node, TextNode}


/**
 * Parses enumerations.
 */
class EnumerationParser(datatype : EnumerationDatatype) extends DataParser
{
    override def parse(node : Node) : Option[String] =
    {
        node match
        {
            case TextNode(text, line) => datatype.parse(text)
            case _ => node.children.flatMap(parse).headOption
        }
    }
}
