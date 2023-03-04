import sbt._
object Dependencies {
  object Versions {
    lazy val zioCoreVersion = "2.0.9"
    lazy val zioK8sVersion = "2.0.2"
  }
  lazy val core = "dev.zio" %% "zio" % Versions.zioCoreVersion
  lazy val zioK8s = "com.coralogix" %% "zio-k8s-client" % Versions.zioK8sVersion
  lazy val zioK8sClient = "com.coralogix" %% "zio-k8s-client" % Versions.zioK8sVersion
  lazy val deps = Seq(
    core,
    zioK8s,
    zioK8sClient
  )
}
