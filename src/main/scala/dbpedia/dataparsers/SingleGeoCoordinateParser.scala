package dbpedia.dataparsers

import java.util.logging.{Level, Logger}

import dbpedia.config.GeoCoordinateParserConfig
import dbpedia.dataparsers.coordinate.{Latitude, Longitude, SingleGeoCoordinate}
import dbpedia.dataparsers.util.Language
import dbpedia.dataparsers.util.wikiparser.Node

import scala.language.reflectiveCalls
import scala.util.control.ControlThrowable

/**
 * Parses a single geographical coordinate, ie. either a latitude or a longitude.
 */
class SingleGeoCoordinateParser(context : { def language : Language }) extends DataParser
{
    private val logger = Logger.getLogger(classOf[GeoCoordinateParser].getName)
    private val language = context.language.wikiCode
    
    private val lonHemLetterMap = GeoCoordinateParserConfig.longitudeLetterMap.getOrElse(language,GeoCoordinateParserConfig.longitudeLetterMap("en"))
    private val latHemLetterMap = GeoCoordinateParserConfig.latitudeLetterMap.getOrElse(language,GeoCoordinateParserConfig.latitudeLetterMap("en"))
    
	private val lonHemRegex = lonHemLetterMap.keySet.mkString("|")
	private val LongitudeRegex = ("""([0-9]{1,2})/([0-9]{1,2})/([0-9]{0,2}(?:.[0-9]{1,2})?)[/]?[\s]?("""+ lonHemRegex +""")""").r
	
	private val latHemRegex = latHemLetterMap.keySet.mkString("|")
	private val LatitudeRegex = ("""([0-9]{1,2})/([0-9]{1,2})/([0-9]{0,2}(?:.[0-9]{1,2})?)[/]?[\s]?("""+ latHemRegex +""")""").r
	

    override def parse(node : Node) : Option[SingleGeoCoordinate] =
    {
        try
        {
            for( text <- StringParser.parse(node);
                 coordinate <- parseSingleCoordinate(text) )
            {
                return Some(coordinate)
            }
        }
        catch
        {
            case ex : ControlThrowable => throw ex
            case ex : Exception => logger.log(Level.FINE, "Could not extract coordinates", ex)
        }

        None
    }

    
    def parseSingleCoordinate(coordStr : String) : Option[SingleGeoCoordinate] = 
    {
    	coordStr match {
    	  case LatitudeRegex(latDeg, latMin, latSec, latHem) => Some(new Latitude(latDeg.toDouble, latMin.toDouble, ("0"+latSec).toDouble, latHemLetterMap.getOrElse(latHem,"N")))
    	  case LongitudeRegex(lonDeg, lonMin, lonSec, lonHem) => Some(new Longitude(lonDeg.toDouble, lonMin.toDouble, ("0"+lonSec).toDouble, lonHemLetterMap.getOrElse(lonHem,"E")))
    	  case _ => None
    	}
    }
        
}
