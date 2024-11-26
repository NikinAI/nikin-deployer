package ai.nikin.deployer.infra

import ai.nikin.deployer.infra.model._
import ai.nikin.deployer.infra.model.k8s._
import com.coralogix.zio.k8s.client.K8sFailure
import com.coralogix.zio.k8s.client.io.upbound.aws.kms.v1beta1._
import com.coralogix.zio.k8s.client.io.upbound.aws.rds.v1beta1._
import com.coralogix.zio.k8s.client.io.upbound.aws.s3.v1beta1._
import com.coralogix.zio.k8s.client.model.K8sNamespace
import com.coralogix.zio.k8s.client.v1.secrets
import com.coralogix.zio.k8s.quicklens._
import com.softwaremill.quicklens.{ModifyPimp, QuicklensEach}
import zio.ZIO
import zio.prelude.data.Optional
trait InfraDeployer {
  def createResource[T <: InfraResource](
      resource: T
  ): ZIO[buckets.Buckets with bucketpublicaccessblocks.BucketPublicAccessBlocks with keys.Keys with instances.Instances with secrets.Secrets, K8sFailure, Unit]
}

object AWSLiveInfraDeployer extends InfraDeployer {
  override def createResource[T <: InfraResource](
      resource: T
  ): ZIO[buckets.Buckets with bucketpublicaccessblocks.BucketPublicAccessBlocks with keys.Keys with instances.Instances with secrets.Secrets, K8sFailure, Unit] =
    resource match {
      case model.Bucket(region, name) =>
        val bucket      =
          K8sBucket
            .defaultBucket
            .modify(_.spec.forProvider.region)
            .setTo(region)
            .modify(_.metadata.each.name)
            .setTo(Optional.Present(name))
        val accessBlock =
          K8sBucket.defaultBlock.modify(_.spec.forProvider.bucket).setTo(Optional.Present(name))

        buckets.create(bucket).flatMap(_ => bucketpublicaccessblocks.create(accessBlock)).unit
      case Database(region, name)     =>
        val secretName  = s"$name-${getRandomK8sString()}"
        val rdsInstance =
          K8sDatabase
            .defaultInstance
            .modify(_.metadata.each.name)
            .setTo(Optional.Present(name))
            .modify(_.spec.forProvider.region)
            .setTo(region)
            .modify(_.spec.forProvider.passwordSecretRef.each.name)
            .setTo(secretName)
            .modify(_.spec.forProvider.kmsKeyIdRef.each.name)
            .setTo(secretName)
        val secret      = K8sSecret.defaultSecret.modify(_.metadata.each.name).setTo(secretName)
        val kmsKey      = K8sKmsKey.defaultKey.modify(_.metadata.each.name).setTo(secretName)

        secrets
          .create(secret, K8sNamespace("default"))
          .flatMap(_ => keys.create(kmsKey))
          .flatMap(_ => instances.create(rdsInstance))
          .unit
    }
}
