package configuration

object Environment {

  private const val DEVELOPMENT = "dev"
  private const val STAGING = "stg"
  private const val PRODUCTION = "live"
  private const val EMPTY_VALUE = ""

  fun isDevelopment(): Boolean {
    return DEVELOPMENT.contains(getEnvironment())
  }

  fun isStaging(): Boolean {
    return STAGING.contains(getEnvironment())
  }

  fun isProduction(): Boolean {
    return PRODUCTION.contains(getEnvironment())
  }

  fun getEnvironment(): String {
    val environment = getVariable("ENV")
    return if (environment.isNotEmpty()) environment else DEVELOPMENT
  }

  fun getVariable(name: String): String {
    return System.getenv(name) ?: EMPTY_VALUE
  }
}
