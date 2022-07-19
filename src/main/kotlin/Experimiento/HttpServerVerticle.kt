package Experimiento

import io.vertx.core.Promise
import io.vertx.core.AbstractVerticle
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.handler.BodyHandler


class HttpServerVerticle:AbstractVerticle(){

  private val users=JsonObject().put(
    "users",
    JsonObject().put(
      "tonys",
      JsonObject().apply {
        put("user_id","tonys")
        put("user_name","Tony Stark")
        put("user_alias","Iron Man")
        put("company","Stark Industries")
      }))

  override fun start(promise:Promise<Void>) {
    var router= Router.router(vertx).apply {
      get("/api/users").handler(this@HttpServerVerticle::getUsers)
      post("/api/users").handler(BodyHandler.create()).handler(this@HttpServerVerticle::setUser)
      /*put("/api/users").handler(BodyHandler.create()).handler(this@HttpServerVerticle::updateUser)
      delete("/api/users").handler(this@HttpServerVerticle::deleteUser)*/
    }
    vertx
      .createHttpServer()
      .requestHandler(router)
      .listen(8282)
  }

  private fun getUsers(context: RoutingContext){
    context.response().statusCode=200
    context.response().putHeader("Content-Type","application/json")
    context.response().end(users.encode())
  }

  private fun setUser(context: RoutingContext){
    val userId=context.request().getParam("user_id")
    val userName=context.request().getParam("user_name")
    val nameAlias=context.request().getParam("user_alias")
    val company=context.request().getParam("company")

    users.getJsonObject("users").put(
      userId,
      JsonObject().apply {
        put("user_id",userId)
        put("user_name",userName)
        put("user_alias",nameAlias)
        put("company",company)
      })

    val response=JsonObject().apply {
      put("success",true)
      put("action","insert")
      put("current_rows",users)
    }

    context.response().statusCode=200
    context.response().putHeader("Content-Type","application/json")
    context.response().end(response.encode())


  }
}
