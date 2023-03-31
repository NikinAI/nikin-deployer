ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.10"

lazy val root = (project in file(".")).settings(
  name := "nikin-deployer"
)

libraryDependencies ++= Dependencies.deps
externalCustomResourceDefinitions :=
  Seq(
    file("crds/BucketAccessBlock.yaml"),
    file("crds/BucketPolicies.yaml"),
    file("crds/Bucket.yaml"),
    file("crds/KmsKey.yaml"),
    file("crds/RdsCluster.yaml"),
    file("crds/ScheduledSparkApplication.yaml"),
    file("crds/SparkApplication.yaml")
  )

enablePlugins(K8sCustomResourceCodegenPlugin)
addCommandAlias("runScalafmt", ";scalafmt;scalafmtSbt")
