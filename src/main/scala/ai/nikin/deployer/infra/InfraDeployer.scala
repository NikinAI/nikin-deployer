package ai.nikin.deployer.infra
import ai.nikin.deployer.infra.model._
import com.coralogix.zio.k8s.client.K8sFailure
import com.coralogix.zio.k8s.client.io.upbound.aws.s3.definitions.bucket.v1beta1
import com.coralogix.zio.k8s.client.io.upbound.aws.s3.v1beta1._
import com.coralogix.zio.k8s.client.io.upbound.aws.s3.definitions.bucket.v1beta1.Bucket.Spec.{ForProvider, ProviderConfigRef}
import com.coralogix.zio.k8s.model.pkg.apis.meta.v1.ObjectMeta
import zio.{Task, ZIO}
trait InfraDeployer {
  def createResource[T <: InfraResource](resource: T): ZIO[buckets.Buckets, K8sFailure, Unit]
}

object LiveInfraDeployer extends InfraDeployer {
  override def createResource[T <: InfraResource](resource: T): ZIO[buckets.Buckets, K8sFailure, Unit] = resource match {
    case Bucket(region, name) => {
      val bucket = v1beta1.Bucket(None,
        v1beta1.Bucket.Spec(
          None,
          None,
          Some(ProviderConfigRef("default", None)),
          None,
          None,
          ForProvider(None, None, region, None),
        ),
        Some(ObjectMeta(name = "random-bucket"))
      )

      buckets.create(bucket)
        .unit
    }
  }
}
