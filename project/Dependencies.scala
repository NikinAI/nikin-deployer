import sbt._
object Dependencies {
  object Versions {
    lazy val zioCoreVersion = "2.0.9"
    lazy val zioK8sVersion = "2.0.2"
    lazy val sttpVersion = "3.8.1"
  }
  lazy val core = "dev.zio" %% "zio" % Versions.zioCoreVersion
  lazy val zioK8s = "com.coralogix" %% "zio-k8s-client" % Versions.zioK8sVersion
  lazy val sttpHttpBackend = "com.softwaremill.sttp.client3" %% "async-http-client-backend-zio" % Versions.sttpVersion
  lazy val sttpSlf4j = "com.softwaremill.sttp.client3" %% "slf4j-backend" % Versions.sttpVersion
  lazy val sttpClientCore = "com.softwaremill.sttp.client3" %% "core" % Versions.sttpVersion
  lazy val zioK8sClient = "com.coralogix" %% "zio-k8s-client" % Versions.zioK8sVersion
  lazy val deps = Seq(
    core,
    zioK8s,
    zioK8sClient,
    sttpHttpBackend,
    sttpSlf4j
  )
}
