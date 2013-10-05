package uk.co.grahamcox.dozy.servlet

/**
 * Handler that works by finding the appropriate handler to delegate to and calling that instead
 */
class DelegatingHandler extends Handler {
  /**
   * Handle the request provided
   * @param req The request to handle
   * @return the response from handling the request
   */
  def handle(req: Request): Response = {
    Response(404, Some("No handler found"), payload = Some("No handler could be found"))
  }

  /**
   * Determine if this handler is capable of handling the given request
   * @param req The request to check
   * @return True if we can handle the request. False if not
   */
  def canHandle(req: Request): Boolean = {
    false
  }
}

