package configuration

import com.pedidosya.courier.common.configuration.IConfig
import io.vertx.config.ConfigRetriever
import io.vertx.config.ConfigRetrieverOptions
import io.vertx.config.ConfigStoreOptions
import io.vertx.core.Vertx
import io.vertx.core.json.JsonObject
import io.vertx.core.logging.Logger
import io.vertx.core.logging.LoggerFactory
import io.vertx.kotlin.config.getConfigAwait
import java.io.File

class Config(private val vertx: Vertx) : IConfig {

  private val retriever: ConfigRetriever = getRetriever()

  override suspend fun load(): JsonObject {
    retriever.getConfigAwait()
    return get()
  }

  override fun get(): JsonObject {
    return retriever.cachedConfig
  }

  private fun getRetriever(): ConfigRetriever {
    val options = getOptions()
    return ConfigRetriever.create(vertx, options)
  }

  private fun getOptions(): ConfigRetrieverOptions {
    val configRetrieverOptions = ConfigRetrieverOptions()
    configRetrieverOptions.stores = getStores()
    configRetrieverOptions.scanPeriod = SCAN_PERIOD
    return configRetrieverOptions
  }

  private fun getStores(): List<ConfigStoreOptions> {
    val stores = mutableListOf<ConfigStoreOptions>()

    // Default configuration file
    val fileStore = ConfigStoreOptions()
      .setType("file")
      .setConfig(JsonObject().put("path", "application.json"))
      .setOptional(false)

    // Environment configuration file will override default configurations
    val envFileStore = ConfigStoreOptions()
      .setType("file")
      .setConfig(JsonObject().put("path", "application-${Environment.getEnvironment()}.json"))
      .setOptional(false)

    // Environment variables override file configurations
    val envVariablesStore = ConfigStoreOptions().setType("env")

    // Vault configuration overrides all others
    /*val vaultStore = getVaultStore()*/

    stores.add(fileStore)
    stores.add(envFileStore)
    stores.add(envVariablesStore)
    /*vaultStore?.let { stores.add(it) }*/
    return stores
  }

  private fun getVaultStore(): ConfigStoreOptions? {
    return runCatching {
      val vaultConfig = JsonObject()
        .put("host", VAULT_HOST) // The host name
        .put("token", getVaultToken())
        .put("path", "secret/courier-business-ordering-service/${Environment.getEnvironment()}")
        .put("port", VAULT_PORT)
        .put("ssl", true)

      ConfigStoreOptions()
        .setType("vault")
        .setConfig(vaultConfig)
        .setOptional(Environment.isDevelopment())
    }.onFailure {
      println("Could not load Vault configuration")
    }.getOrNull()
  }

  private fun getVaultToken(): String {
    val token = Environment.getVariable("VAULT_TOKEN")
    return if (token.isEmpty()) {
      val path = "${Environment.getVariable("HOME")}/.vault_token"
      println("Vault token path is: $path")
      File(path).readText(Charsets.UTF_8).trim()
    } else {
      println("Vault token read from environment")
      token
    }
  }

  companion object {
    private const val SCAN_PERIOD: Long = 0 // just one time
    private const val VAULT_PORT: Int = 443
    private const val VAULT_HOST: String = "vault.peya.co"
  }
}
