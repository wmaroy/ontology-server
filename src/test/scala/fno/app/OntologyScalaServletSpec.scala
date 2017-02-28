package fno.app

import fno.app.servlets.OntologyScalaServlet
import org.scalatra.test.specs2._

// For more on Specs2, see http://etorreborre.github.com/specs2/guide/org.specs2.guide.QuickStart.html
class OntologyScalaServletSpec extends ScalatraSpec {

  def is =
  "GET / on OntologyScalaServlet"                     ^
    "should return status 200"                  ! root200^
                                                end

  addServlet(classOf[OntologyScalaServlet], "/*")

  def root200 = get("http://localhost:8080/functions/isSetFunction?property=null") {
    status must_== 200
  }
}
