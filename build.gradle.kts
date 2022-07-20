import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.api.tasks.testing.logging.TestLogEvent.*
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.net.*


plugins {
  kotlin ("jvm") version "1.6.10"
  application
  id("com.github.johnrengelman.shadow") version "7.0.0"
}

group = "com.example"
version = "1.0.0-SNAPSHOT"

repositories {
  mavenLocal()
  mavenCentral()
  jcenter()
  maven {
    name = "pedidosya.jfrog"
    url = URI("https://pedidosya.jfrog.io/artifactory/pedidosya-gradle-prod-local")
    credentials {
      username = project.property("artifactory.username") as String
      password = project.property("artifactory.password") as String
    }
  }
  maven("https://dl.bintray.com/kotlin/kotlin-eap")
}

val vertxVersion = "4.3.2"
val junitJupiterVersion = "5.7.0"

val mainVerticleName = "Experimiento.MainVerticle"
val launcherClassName = "io.vertx.core.Launcher"

val watchForChange = "src/**/*"
val doOnChange = "${projectDir}/gradlew classes"

application {
  mainClass.set(launcherClassName)
}

dependencies {
  implementation(platform("io.vertx:vertx-stack-depchain:$vertxVersion"))
  implementation("io.vertx:vertx-web-client")
  implementation("io.vertx:vertx-auth-jwt")
  implementation("io.vertx:vertx-health-check")
  implementation("io.vertx:vertx-web")
  implementation("io.vertx:vertx-grpc-server")
  implementation("io.vertx:vertx-service-discovery")
  implementation("io.vertx:vertx-http-service-factory")
  implementation("io.vertx:vertx-json-schema")
  implementation("io.vertx:vertx-grpc-client")
  implementation("io.vertx:vertx-config")
  implementation("io.vertx:vertx-circuit-breaker")
  implementation("io.vertx:vertx-mongo-client")
  implementation("io.vertx:vertx-lang-kotlin")
  /*implementation("aws.sdk.kotlin:s3:0.9.4-beta")
  implementation("aws.sdk.kotlin:dynamodb:0.9.4-beta")
  implementation("aws.sdk.kotlin:iam:0.9.4-beta")
  implementation("aws.sdk.kotlin:cloudwatch:0.9.4-beta")
  implementation("aws.sdk.kotlin:cognito:0.9.4-beta")
  implementation("aws.sdk.kotlin:sns:0.9.4-beta")
  implementation("aws.sdk.kotlin:pinpoint:0.9.4-beta")*/
  implementation("com.pedidosya.aws.sdk:aws-sdk-dynamodb:1.0")
  implementation("com.pedidosya.courier.common:courier-common:1.0.27")
  implementation("com.pedidosya.courier.common:courier-common-dynamodb:1.0.27")
  implementation(kotlin("stdlib-jdk8"))
  testImplementation("io.vertx:vertx-unit")
  testImplementation("junit:junit:4.13.1")
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions.jvmTarget = "1.8"

tasks.withType<ShadowJar> {
  archiveClassifier.set("fat")
  manifest {
    attributes(mapOf("Main-Verticle" to mainVerticleName))
  }
  mergeServiceFiles()
}

tasks.withType<Test> {
  useJUnit()
  testLogging {
    events = setOf(PASSED, SKIPPED, FAILED)
  }
}

tasks.withType<JavaExec> {
  args = listOf("run", mainVerticleName, "--redeploy=$watchForChange", "--launcher-class=$launcherClassName", "--on-redeploy=$doOnChange")
}
