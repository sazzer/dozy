package uk.co.grahamcox.dozy.servlet

/**
 * Wrapper around a single handler in a single controller
 */
trait Handler {
  /**
   * Handle the request provided
   * @param req The request to handle
   * @return the response from handling the request
   */
  def handle(req: Request): Response;

  /**
   * Determine if this handler is capable of handling the given request
   * @param req The request to check
   * @return True if we can handle the request. False if not
   */
  def canHandle(req: Request): Boolean;
}
