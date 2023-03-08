package ai.nikin.deployer.service

import ai.nikin.deployer.k8s.{ScheduledSparkAppDeployer, SparkAppDeployer}
import ai.nikin.deployer.model.{IngestServerDeploymentDescriptor, ScheduledSparkApplicationDeploymentDescriptor, SparkApplicationDeploymentDescriptor}
import com.coralogix.zio.k8s.client.K8sFailure
import com.coralogix.zio.k8s.client.v1.{configmaps, secrets, services}
import com.coralogix.zio.k8s.client.apps.v1.deployments
import com.coralogix.zio.k8s.client.apps.v1.deployments.Deployments
import com.coralogix.zio.k8s.client.sparkoperator.v1beta2.scheduledsparkapplications.ScheduledSparkApplications
import com.coralogix.zio.k8s.client.sparkoperator.v1beta2.sparkapplications.SparkApplications
import com.coralogix.zio.k8s.client.v1.configmaps.ConfigMaps
import com.coralogix.zio.k8s.client.v1.secrets.Secrets
import com.coralogix.zio.k8s.client.v1.services.Services
import zio.ZIO
trait DeployerService {
  def deployIngestService(descriptor: IngestServerDeploymentDescriptor): ZIO[Deployments with Services with ConfigMaps with Secrets, K8sFailure, Unit]
  def deploySparkApplication(descriptor: SparkApplicationDeploymentDescriptor): ZIO[ConfigMaps with SparkApplications with Secrets, K8sFailure, Unit]
  def deploySparkApplication(descriptor: ScheduledSparkApplicationDeploymentDescriptor): ZIO[ConfigMaps with ScheduledSparkApplications with Secrets, K8sFailure, Unit]

}

object LiveDeployerService extends DeployerService {

  override def deploySparkApplication(descriptor: SparkApplicationDeploymentDescriptor): ZIO[ConfigMaps with SparkApplications with Secrets, K8sFailure, Unit] = for {
      secrets <- ZIO.foreach(descriptor.secrets)(s => secrets.create(s, descriptor.namespace))
      cm <- configmaps.create(descriptor.configMap, descriptor.namespace)
      app <- SparkAppDeployer.create(descriptor.namespace, descriptor.sparkApplication)
    } yield ()

  override def deploySparkApplication(descriptor: ScheduledSparkApplicationDeploymentDescriptor): ZIO[ConfigMaps with ScheduledSparkApplications with Secrets, K8sFailure, Unit] = for {
    secrets <- ZIO.foreach(descriptor.secrets)(s => secrets.create(s, descriptor.namespace))
    cm <- configmaps.create(descriptor.configMap, descriptor.namespace)
    app <- ScheduledSparkAppDeployer.create(descriptor.namespace, descriptor.sparkApplication)
  } yield ()

  override def deployIngestService(descriptor: IngestServerDeploymentDescriptor) = {
    for {
      secrets <- ZIO.foreach(descriptor.secrets)(s => secrets.create(s, descriptor.namespace))
      configMap <- configmaps.create(descriptor.configMap, descriptor.namespace)
      service <- services.create(descriptor.service, descriptor.namespace)
      deployment <- deployments.create(descriptor.deployment, descriptor.namespace)
    } yield ()
  }
}

