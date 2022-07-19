import Experimiento.MainVerticle
import io.vertx.core.Launcher


class App {

  fun main(){
    Launcher.executeCommand("run", MainVerticle::class.java.name)
  }
}
