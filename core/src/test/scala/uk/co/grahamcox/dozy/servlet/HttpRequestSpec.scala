package uk.co.grahamcox.dozy.servlet

import java.net.URL
import javax.servlet.http.HttpServletRequest
import org.specs2.mock._
import org.specs2.mutable._
import scala.collection.JavaConverters._

object MockRequest extends Mockito {
  def apply(method: String, 
    url: String, 
    params: Map[String, String] = Map.empty[String, String],
    headers: Map[String, String] = Map.empty[String, String]): HttpServletRequest = {
      val fullUrl = new URL(url)
      val path = fullUrl.getPath

      val httpServletRequest = mock[HttpServletRequest]
      httpServletRequest.getMethod returns method
      httpServletRequest.getServletPath returns "/" + path.stripPrefix("/").split("/").head
      httpServletRequest.getRequestURI returns path
      httpServletRequest.getRequestURL returns new StringBuffer(url)

      httpServletRequest.getParameterMap.asInstanceOf[java.util.Map[String, Array[String]]] returns (params.map { case (k, v) => 
        (k -> Array(v))
      }).asJava
      httpServletRequest.getParameterNames.asInstanceOf[java.util.Enumeration[String]] returns params.keys.toIterator.asJavaEnumeration
      params.foreach {
        case (key: String, value: String) => 
          httpServletRequest.getParameter(key) returns value
          httpServletRequest.getParameterValues(key) returns Array(value)
      }

      httpServletRequest.getHeaderNames.asInstanceOf[java.util.Enumeration[String]] returns headers.keys.toIterator.asJavaEnumeration
      headers.foreach {
        case (key: String, value: String) => 
          httpServletRequest.getHeader(key) returns value
      }
      httpServletRequest
  }
}

class HttpRequestSpec extends Specification with Mockito {
  "The HttpRequest class" should {
    "For a request of GET http://localhost:8080/servlet/path" in {
      val req = new HttpRequest(MockRequest("GET", "http://localhost:8080/servlet/path"))

      "Return a method of GET" in {
        req.getMethod must beEqualTo("GET")
      }
      "Return a full URL of http://localhost:8080/servlet/path" in {
        req.getFullURL must beEqualTo("http://localhost:8080/servlet/path")
      }
      "Return a local URL of /path" in {
        req.getURL must beEqualTo("/path")
      }
      "Return no parameters" in {
        req.getParams must beEmpty
      }
      "Return no headers" in {
        req.getHeaders must beEmpty
      }
      "Not find a non-existant parameter" in {
        req.getParam("bad") must beNone
      }
      "Not find a non-existant header" in {
        req.getParam("bad") must beNone
      }
    }
    "For a request of POST http://localhost:8080/servlet/path with content-type: application/json" in {
      val req = new HttpRequest(MockRequest("POST", "http://localhost:8080/servlet/path", 
        headers = Map(("content-type" -> "application/json"))))

      "Return a method of POST" in {
        req.getMethod must beEqualTo("POST")
      }

      "Return a content-type header of 'application/json'" in {
        req.getHeader("content-type") must beSome("application/json")
      }

      "Not find a non-existant header" in {
        req.getParam("bad") must beNone
      }
    }
    "For a request of GET http://localhost:8080/servlet/path?a=b&c=d" in {
      val req = new HttpRequest(MockRequest("GET", "http://localhost:8080/servlet/path", Map("a" -> "b", "c" -> "d")))

      "Return a method of GET" in {
        req.getMethod must beEqualTo("GET")
      }
      "Return a full URL of http://localhost:8080/servlet/path" in {
        req.getFullURL must beEqualTo("http://localhost:8080/servlet/path")
      }
      "Return a local URL of /path" in {
        req.getURL must beEqualTo("/path")
      }
      "Return two parameters" in {
        req.getParams must have size(2)
        req.getParams must havePair("a" -> "b")
        req.getParams must havePair("c" -> "d")
      }
      "Return a correct value for parameter 'a'" in {
        req.getParam("a") must beSome("b")
      }
      "Return a correct value for parameter 'c'" in {
        req.getParam("c") must beSome("d")
      }
      "Not find a non-existant parameter" in {
        req.getParam("bad") must beNone
      }
    }
  }
}
