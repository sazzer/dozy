package uk.co.grahamcox.dozy.servlet

/**
 * Trait encapsulating the core Request details
 */
trait Request {
  /**
   * Get the URL that was requested. This is the full URL, including all components
   * @return the URL that was requested
   */
  def getFullURL: String
  /**
   * Get the URL that was requested. This is the URL only as far as the inside of the servlet
   * @return the URL that was requested
   */
  def getURL: String
  /**
   * Get the HTTP Method that was requested
   * @return the HTTP Method
   */
  def getMethod: String
  /**
   * Get the value of a single header
   * @param name The name of the header to get
   * @return the header value
   */
  def getHeader(name: String): Option[String]
  /**
   * Get all of the headers
   * @return the headers
   */
  def getHeaders: Map[String, String]
  /**
   * Get a single parameter
   * @param name The name of the parameter to get
   * @return the parameter value
   */
  def getParam(name: String): Option[String]
  /**
   * Get all of the parameters
   * @return the parameters
   */
  def getParams: Map[String, String]
}
