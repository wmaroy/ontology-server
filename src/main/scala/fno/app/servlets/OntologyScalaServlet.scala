package fno.app.servlets

import dbpedia.dataparsers.ontology.OntologySingleton
import functions._
import functions.implementations._
import functions.implementations.conditions.{ContainsFunction, EqualsFunction, IsSetFunction}
import functions.implementations.date.{EndDateFunction, StartDateFunction}
import functions.implementations.geocoordinate.{LatFunction, LonFunction}
import play.api.libs.json.{JsArray, JsObject, JsString, JsValue}

class OntologyScalaServlet extends OntologyserverStack {


  /**
    * GET request that executes one of the DBPedia functions based on the parameter
    */
  get("/functions/:function") {

    val function = params("function")

    val result = function match {
      case "simpleProperty" => simplePropertyFunction
      case "latFunction" => latFunction
      case "lonFunction" => lonFunction
      case "startDateFunction" => startDateFunction
      case "endDateFunction" => endDateFunction
      case "containsFunction" => containsFunction
      case "isSetFunction" => isSetFunction
      case "equalsFunction" => equalsFunction
    }

    val JsSeq = result.map(x => JsString(x))
    val resultJson : JsValue = JsArray(JsSeq)
    resultJson.toString()

  }

  /**
    * Parses the latitude coordinates from a wikitext string
    * @return
    */
  private def latFunction : Seq[String] = {
    val coordinate = getParam("coordinate")
    val singleGeoCoordinate = getParam("singleCoordinate")
    val degrees = getParam("degrees")
    val minutes = getParam("minutes")
    val seconds = getParam("seconds")
    val direction = getParam("direction")

    val seq = try {
      val fn = new LatFunction(coordinate, singleGeoCoordinate, degrees, minutes, seconds, direction)
      fn.execute()
    } catch {
      case ex : Exception =>
        Seq()
    }

    seq
  }

  /**
    * Parses the longitude coordinates  from a wikitext string
    * @return
    */
  private def lonFunction : Seq[String] = {

    val coordinate = getParam("coordinate")
    val singleGeoCoordinate = getParam("singleCoordinate")
    val degrees = getParam("degrees")
    val minutes = getParam("minutes")
    val seconds = getParam("seconds")
    val direction = getParam("direction")

    val seq = try {
      val fn = new LonFunction(coordinate, singleGeoCoordinate, degrees, minutes, seconds, direction)
      fn.execute()
    } catch {
      case ex : Exception =>
        Seq()
    }

    seq
  }

  /**
    * Parses the start date from a wikitext string
    * @return
    */
  private def startDateFunction : Seq[String] = {

    val property = getParam("property")
    val ontologyProperty = getParam("ontologyProperty")

    val seq = try {
      val fn = new StartDateFunction(property, ontologyProperty)
      Seq(fn.execute())
    } catch {
      case ex : Exception =>
        Seq()
    }

    seq
  }

  /**
    * Parses the end date from a wikitext string
    * @return
    */
  private def endDateFunction : Seq[String] = {
    val property = getParam("property")
    val ontologyProperty = getParam("ontologyProperty")

    val seq = try {
      val fn = new EndDateFunction(property, ontologyProperty)
      Seq(fn.execute())
    } catch {
      case ex : Exception =>
        Seq()
    }

    seq
  }

  /**
    * Checks if the property parameter contains a given string
    * @return
    */
  private def containsFunction : Seq[String] = {

    val property = getParam("property")
    val value = getParam("value")

    val seq = try {
      val fn = new ContainsFunction(property, value)
      Seq(fn.execute().toString)
    } catch {
      case ex : Exception =>
        Seq()
    }

    seq

  }

  /**
    * Checks if the property parameter is set or not
    * @return
    */
  private def isSetFunction : Seq[String] = {

    val property = getParam("property")


    val seq = try {
      val fn = if(property != null) new IsSetFunction(property) else new IsSetFunction("null") // this is a hack :(
      Seq(fn.execute().toString)
    } catch {
      case ex : Exception =>
        Seq()
    }

    seq
  }

  /**
    * Compares the property parameter with the value parameter
    * @return
    */
  private def equalsFunction : Seq[String] = {

    val property = getParam("property")
    val value = getParam("value")

    val seq = try {
      val fn = new EqualsFunction(property, value)
      Seq(fn.execute().toString)
    } catch {
      case ex : Exception =>
        Seq()
    }

    seq

  }

  /**
    * Parses a simple property from a wikitext
    * @return
    */
  private def simplePropertyFunction : Seq[String] = {

    val property = getParam("property")
    val select = getParam("select")
    val prefix = getParam("prefix")
    val suffix = getParam("suffix")
    val factor = getParam("factor").toDouble
    val unit = getParam("unit")
    val datatype = getParam("datatype")
    val transform = getParam("transform")

    try {
      val fn = new SimplePropertyFunction(property, select, prefix, suffix, transform, factor, datatype, unit)
      fn.execute()
    } catch {
      case ex : Exception =>
        Seq()
    }

  }

  /**
    * Tries to extract a parameter, if no parameter is found return null
    * @param param
    * @return
    */
  private def getParam(param : String) : String = {
    try {
      val parameter = params.get(param).orNull
      if(parameter != "null") parameter else null
    } catch {
      case ex : Exception =>
        null
    }
  }


}
