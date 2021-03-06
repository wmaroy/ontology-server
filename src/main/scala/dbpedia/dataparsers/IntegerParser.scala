package dbpedia.dataparsers

import java.text.ParseException
import java.util.logging.{Level, Logger}

import dbpedia.dataparsers.util.wikiparser.Node
import dbpedia.dataparsers.util.{Language, ParserUtils}
import scala.language.reflectiveCalls

/**
 * Parses integer numbers.
 */
class IntegerParser(context : { def language : Language },
                    strict : Boolean = false,
                    multiplicationFactor : Double = 1.0,
                    validRange : Double => Boolean = (i => true)) extends DataParser
{
    private val parserUtils = new ParserUtils(context.language)

    private val logger = Logger.getLogger(getClass.getName)

    private val language = context.language.wikiCode

    override val splitPropertyNodeRegex = if (DataParserConfig.splitPropertyNodeRegexInteger.contains(language))
                                            DataParserConfig.splitPropertyNodeRegexInteger.get(language).get
                                          else DataParserConfig.splitPropertyNodeRegexInteger.get("en").get

    // we allow digits, minus, comma, dot and space in numbers
    private val IntegerRegex = """\D*?(?:\D\d+\s+)?(-?[0-9,\. ]+).*""".r

    override def parse(node : Node) : Option[Long] =
    {
        for( text <- StringParser.parse(node);
             convertedText = parserUtils.convertLargeNumbers(text);
             value <- parseValue(convertedText) )
        {
            return Some((value * multiplicationFactor).round)
        }

        None
    }

    def parseValue(input : String) : Option[Double] =   // double is returned because                                       // RMLProcessRunner needs this
    {
        val numberStr = if(strict) input.trim else IntegerRegex.findFirstMatchIn(input.trim) match
        {
            case Some(s) => s.subgroups(0).toString() // s is the WHOLE MATCH, while we want the matching subgroup
            case None =>
            {
                logger.log(Level.FINE, "Cannot convert '" + input + "' to an integer, IntegerRegex did not match")
                return None
            }
        }

        try
        {
            val result = parserUtils.parse(numberStr).doubleValue
            if( validRange(result) )
            {
                val hasMinusSign = (!input.equals(numberStr) && DataParserConfig.dashVariations.contains(input.trim.charAt(0)))
                val negatize = if (result>=0 && hasMinusSign) -1 else 1
                Some(negatize * result)
            }
            else
            {
                None
            }
        }
        catch
        {
            case ex : ParseException =>
            {
                logger.log(Level.FINE, "Cannot convert '" + numberStr + "' to an integer", ex)
                None
            }
            case ex : NumberFormatException =>
            {
                logger.log(Level.FINE, "Cannot convert '" + numberStr + "' to an integer", ex)
                None
            }
            case ex : ArrayIndexOutOfBoundsException =>
            {
                logger.log(Level.FINE, "Cannot convert '" + numberStr + "' to an integer", ex)
                None
            }
        }
    }
}