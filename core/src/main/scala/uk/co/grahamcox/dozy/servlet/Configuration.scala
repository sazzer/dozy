package uk.co.grahamcox.dozy.servlet

/**
 * Trait that represents the configuration class to use for the servlet
 */
trait Configuration {
  /**
   * Provide any configuration that is required for the trait to be able to find the classes needed
   * @param configuration The configuration that is used
   */
  def configure(configuration: Map[String, String])
  /**
   * Get all of the controllers that are defined in this configuration
   * @return the collection of controllers - that is all of the classes with the @Controller annotation
   */
  def controllers: Iterable[AnyRef]
}
