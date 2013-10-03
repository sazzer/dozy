package uk.co.grahamcox.dozy.servlet

import grizzled.slf4j.Logger
import javax.servlet.http.HttpServlet
import javax.servlet.ServletConfig
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * The actual core servlet
 */
class DozyServlet extends HttpServlet {
  /** The logger to use */
  private val logger = Logger[this.type]
  /**
   * Actually service an incoming request
   * @param req The request to service
   * @param res The response to write to
   */
  override def service(req: HttpServletRequest, res: HttpServletResponse) {
    val servletConfig = getServletConfig()
    val servletContext = getServletContext()
    val method = req.getMethod()
    val servletPath = req.getServletPath()
    val path = req.getRequestURI().stripPrefix(servletPath)

    logger.debug(s"Handling request: $method $path")
  }

  /**
   * Handle the initialization of the servlet, loading the configuration and determining the handlers
   */
  override def init() {
    val servletConfig = getServletConfig()
    val servletContext = getServletContext()
    val servletName = servletConfig.getServletName

    logger.info(s"Starting up Servlet: $servletName")
  }

  /**
   * Handle the desctruction of the servlet, destroying the configuration
   */
  override def destroy() {
    val servletConfig = getServletConfig()
    val servletContext = getServletContext()
    val servletName = servletConfig.getServletName

    logger.info(s"Shutting down Servlet: $servletName")
  }
}
