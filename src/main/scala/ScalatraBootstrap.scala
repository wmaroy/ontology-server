import java.io.File

import fno.app._
import org.scalatra._
import javax.servlet.ServletContext

import dbpedia.dataparsers.ontology.OntologySingleton
import fno.app.servlets.OntologyScalaServlet

class ScalatraBootstrap extends LifeCycle {
  override def init(context: ServletContext) {

    context.mount(new OntologyScalaServlet, "/*")

    OntologySingleton.load

    println("[ONTOLOGY LOG] Ontology Properties Size: " + OntologySingleton.getPropertiesSize)

  }
}
