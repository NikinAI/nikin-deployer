import sbt._

object Dependencies {
  object Versions {
    lazy val zioCoreVersion = "2.0.9"
    lazy val zioK8sVersion = "2.0.2"
    lazy val sttpVersion = "3.8.1"
    lazy val avroVersion = "1.11.1"
    lazy val nikinCoreVersion = "0.1.0-SNAPSHOT"
  }
  lazy val core = "dev.zio" %% "zio" % Versions.zioCoreVersion
  lazy val zioK8s = "com.coralogix" %% "zio-k8s-client" % Versions.zioK8sVersion
  lazy val zioTest = "dev.zio" %% "zio-test" % Versions.zioCoreVersion % Test
  lazy val zioTestSbt = "dev.zio" %% "zio-test-sbt" % Versions.zioCoreVersion % Test

  lazy val sttpHttpBackend = "com.softwaremill.sttp.client3" %% "async-http-client-backend-zio" % Versions.sttpVersion
  lazy val sttpSlf4j = "com.softwaremill.sttp.client3" %% "slf4j-backend" % Versions.sttpVersion
  lazy val sttpClientCore = "com.softwaremill.sttp.client3" %% "core" % Versions.sttpVersion
  lazy val zioK8sClient = "com.coralogix" %% "zio-k8s-client" % Versions.zioK8sVersion
  lazy val avro = "org.apache.avro" % "avro" % Versions.avroVersion
  lazy val nikinSdk = "ai.nikin" %% "pipeline-sdk" % Versions.nikinCoreVersion

  lazy val deps = Seq(
    core,
    zioK8s,
    zioK8sClient,
    sttpHttpBackend,
    sttpSlf4j,
    avro,
    zioTest,
    zioTestSbt,
    nikinSdk
  )
}
