import sbt._

object Dependencies {
  object Versions {
    lazy val zioCoreVersion   = "2.1.13"
    lazy val zioK8sVersion    = "3.1.0"
    lazy val sttpVersion      = "3.10.1"
    lazy val avroVersion      = "1.12.0"
    lazy val nikinCoreVersion = "0.1.0-SNAPSHOT"
  }

  lazy val core       = "dev.zio"       %% "zio"            % Versions.zioCoreVersion
  lazy val zioK8s     = "com.coralogix" %% "zio-k8s-client" % Versions.zioK8sVersion
  lazy val zioTest    = "dev.zio"       %% "zio-test"       % Versions.zioCoreVersion % Test
  lazy val zioTestSbt = "dev.zio"       %% "zio-test-sbt"   % Versions.zioCoreVersion % Test

  lazy val sttpSlf4j      = "com.softwaremill.sttp.client3" %% "slf4j-backend"            % Versions.sttpVersion
  lazy val zioK8sClient   = "com.coralogix"                 %% "zio-k8s-client"           % Versions.zioK8sVersion
  lazy val avro           = "org.apache.avro"                % "avro"                     % Versions.avroVersion
  lazy val nikinSdk       = "ai.nikin"                      %% "pipeline-sdk"             % Versions.nikinCoreVersion
  lazy val quickLens      = "com.coralogix"                 %% "zio-k8s-client-quicklens" % Versions.zioK8sVersion

  lazy val deps =
    Seq(
      core,
      zioK8s,
      zioK8sClient,
      sttpSlf4j,
      avro,
      zioTest,
      zioTestSbt,
      nikinSdk,
      quickLens
    )
}
