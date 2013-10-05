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
}
