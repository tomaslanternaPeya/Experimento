package Experimiento

import io.vertx.codegen.Helper
import io.vertx.core.AbstractVerticle
import io.vertx.core.Promise

class MainVerticle : AbstractVerticle() {

  override fun start(startPromise: Promise<Void>) {
    vertx.deployVerticle(HttpServerVerticle())
  }
}
