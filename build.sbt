import scala.collection.immutable.Seq

ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.15"

ThisBuild / organization     := "ai.nikin"
ThisBuild / organizationName := "NikinAI"

ThisBuild / scalacOptions ++=
  Seq(
    "-Xlint:adapted-args", // Warn if an argument list is modified to match the receiver.
    "-Xlint:nullary-unit", // Warn when nullary methods return Unit.
    "-Xlint:inaccessible", // Warn about inaccessible types in method signatures.
    "-Xlint:infer-any", // Warn when a type argument is inferred to be Any.
    "-Xlint:constant", // Evaluation of a constant arithmetic expression results in an error.
    "-Ywarn-unused:imports", /*"-Xfatal-warnings",*/ "-deprecation", "-Ywarn-dead-code",
    "-Ywarn-unused:params", "-Ywarn-unused:locals", "-Ywarn-value-discard",
    "-Ywarn-unused:privates", "-Ymacro-annotations",
    "-Wconf:cat=other-implicit-type&src=src_managed/.*:s",
    "-Wconf:cat=unused-imports&src=src_managed/.*:s"
  )

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
