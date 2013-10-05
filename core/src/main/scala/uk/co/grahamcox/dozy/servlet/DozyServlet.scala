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
  /** The handler to use */
  private var handler: Option[Handler] = None
  /**
   * Actually service an incoming request
   * @param req The request to service
   * @param res The response to write to
   */
  override def service(req: HttpServletRequest, res: HttpServletResponse) {
    val servletConfig = getServletConfig()
    val servletContext = getServletContext()
    val request = new HttpRequest(req)

    logger.debug(s"Handling request: ${request.getMethod} ${request.getURL}")
    logger.debug(s"Parameters: ${request.getParams}")
    logger.debug(s"Headers: ${request.getHeaders}")

    val response = handler filter {
      h => h.canHandle(request)
    } map {
      h => h.handle(request)
    } getOrElse(Response(404))
    logger.debug(s"Response: $response")

    // Handle setting the headers
    response.headers.foreach {
      case (key, value) => res.addHeader(key, value)
    }

    (response.payload.map {
      // This converts the payload to a string
      _ match {
        case payload: Any => ("text/plain", payload.toString)
      }
    }).foreach {
      // This writes the payload to the output
      case (contentType: String, payload: String) => {
        res.setContentType(contentType)
        res.getWriter.write(payload)
      }
    }

    // Handle setting the status code
    response match {
      case (Response(statusCode, Some(message), _, _)) => res.setStatus(statusCode, message)
      case (Response(statusCode, None, _, _)) => res.setStatus(statusCode)
    }
  }

  /**
   * Handle the initialization of the servlet, loading the configuration and determining the handlers
   */
  override def init() {
    val servletConfig = getServletConfig()
    val servletContext = getServletContext()
    val servletName = servletConfig.getServletName

    logger.info(s"Starting up Servlet: $servletName")
    handler = Some(new DefaultHeadersHandler(new DelegatingHandler))
  }

  /**
   * Handle the desctruction of the servlet, destroying the configuration
   */
  override def destroy() {
    val servletConfig = getServletConfig()
    val servletContext = getServletContext()
    val servletName = servletConfig.getServletName

    logger.info(s"Shutting down Servlet: $servletName")
    handler = None
  }
}
