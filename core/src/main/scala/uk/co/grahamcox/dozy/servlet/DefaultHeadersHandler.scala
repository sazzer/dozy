package uk.co.grahamcox.dozy.servlet

/**
 * Handler that works by finding the appropriate handler to delegate to and calling that instead
 */
class DefaultHeadersHandler(handler: Handler) extends Handler {
  /** The default headers to use if not provided in the response */
  private val defaultHeaders = Map(
    ("X-Powered-By" -> "Dozy")
  )
  /**
   * Handle the request provided
   * @param req The request to handle
   * @return the response from handling the request
   */
  def handle(req: Request): Response = {
    val inner = handler.handle(req)
    Response(inner.statusCode, inner.message, defaultHeaders ++ inner.headers, inner.payload)
  }

  /**
   * Determine if this handler is capable of handling the given request
   * @param req The request to check
   * @return True if we can handle the request. False if not
   */
  def canHandle(req: Request): Boolean = handler.canHandle(req)
}


