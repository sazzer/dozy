package uk.co.grahamcox.dozy.servlet

import javax.servlet.http.HttpServletRequest
import scala.collection.JavaConverters._

/**
 * Implementation of the Request that works in terms of an HttpServletRequest
 */
class HttpRequest(req: HttpServletRequest) extends Request {
  /** The parameters from the request */
  private val params: Map[String, String] = (req.getParameterMap.asScala.toMap.asInstanceOf[Map[String, Array[String]]].filter {
    case (key, value) => value.length > 0
  }).map {
    case (key, value) => (key -> value(0))
  }
  /** The headers from the request */
  private val headers: Map[String, String] = Map(req.getHeaderNames.asScala.toList.asInstanceOf[List[String]].map {
    name: String => (name -> req.getHeader(name))
  }: _*)
  /**
   * Get the URL that was requested. This is the full URL, including all components
   * @return the URL that was requested
   */
  def getFullURL: String = req.getRequestURL.toString

  /**
   * Get the URL that was requested. This is the URL only as far as the inside of the servlet
   * @return the URL that was requested
   */
  def getURL: String = req.getRequestURI.stripPrefix(req.getServletPath)
  /**
   * Get the HTTP Method that was requested
   * @return the HTTP Method
   */
  def getMethod: String = req.getMethod
  /**
   * Get the value of a single header
   * @param name The name of the header to get
   * @return the header value
   */
  def getHeader(name: String): Option[String] = headers.get(name)
  /**
   * Get all of the headers
   * @return the headers
   */
  def getHeaders: Map[String, String] = headers
  /**
   * Get a single parameter
   * @param name The name of the parameter to get
   * @return the parameter value
   */
  def getParam(name: String): Option[String] = params.get(name)
  /**
   * Get all of the parameters
   * @return the parameters
   */
  def getParams: Map[String, String] = params
}

