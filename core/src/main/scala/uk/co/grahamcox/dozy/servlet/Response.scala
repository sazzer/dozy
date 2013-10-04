package uk.co.grahamcox.dozy.servlet

/**
 * Rich representation of the response to send to the client
 * @param statusCode The HTTP Status Code to send
 * @param message The HTTP Status Message to send
 * @param headers Any HTTP Headers to send
 * @param payload The response payload to send
 */
case class Response(val statusCode: Int = 200, 
  message: Option[String] = None, 
  headers: Map[String, String] = Map.empty[String, String], 
  payload: Option[Any] = None) {
}

