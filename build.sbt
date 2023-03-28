ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.10"

lazy val root = (project in file("."))
  .settings(
    name := "nikin-deployer"
  )


libraryDependencies ++= Dependencies.deps
externalCustomResourceDefinitions := Seq(
  file("crds/sparkoperator.k8s.io_scheduledsparkapplications.yaml"),
  file("crds/s3.aws.crossplane.io_buckets.yaml"),
  file("crds/s3.aws.crossplane.io_bucketpolicies.yaml"),
  file("crds/rds.aws.upbound.io_clusters.yaml"),
  file("crds/sparkoperator.k8s.io_sparkapplications.yaml"),

)

enablePlugins(K8sCustomResourceCodegenPlugin)
addCommandAlias("runScalafmt", ";scalafmt;scalafmtSbt")
