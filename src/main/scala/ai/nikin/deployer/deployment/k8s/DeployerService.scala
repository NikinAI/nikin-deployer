package ai.nikin.deployer.deployment.k8s

import ai.nikin.deployer.deployment.model.K8sResource._
import ai.nikin.deployer.deployment.model.{K8SResourceName, K8sResource}
import com.coralogix.zio.k8s.client.K8sFailure.syntax.K8sZIOSyntax
import com.coralogix.zio.k8s.client.apps.v1.deployments
import com.coralogix.zio.k8s.client.apps.v1.deployments.Deployments
import com.coralogix.zio.k8s.client.model.K8sNamespace
import com.coralogix.zio.k8s.client.sparkoperator.v1beta2.scheduledsparkapplications.ScheduledSparkApplications
import com.coralogix.zio.k8s.client.sparkoperator.v1beta2.sparkapplications.SparkApplications
import com.coralogix.zio.k8s.client.sparkoperator.v1beta2.{scheduledsparkapplications, sparkapplications}
import com.coralogix.zio.k8s.client.v1.configmaps.ConfigMaps
import com.coralogix.zio.k8s.client.v1.secrets.Secrets
import com.coralogix.zio.k8s.client.v1.services.Services
import com.coralogix.zio.k8s.client.v1.{configmaps, secrets, services}
import com.coralogix.zio.k8s.client.{K8sFailure, NamespacedResource}
import com.coralogix.zio.k8s.model.pkg.apis.meta.v1.{DeleteOptions, Status}
import zio.ZIO

trait DeployerService {
  def deploy(resource: K8sResource): ZIO[Services with Deployments with ConfigMaps with SparkApplications with ScheduledSparkApplications with Secrets, K8sFailure, Unit]
  def replace(updatedResource: K8sResource): ZIO[Services with Deployments with ConfigMaps with SparkApplications with ScheduledSparkApplications with Secrets, K8sFailure, Unit]
  def delete(resource: K8sResource): ZIO[Services with Deployments with ConfigMaps with SparkApplications with ScheduledSparkApplications with Secrets, K8sFailure, Status]
  def get[T: zio.Tag](name: K8SResourceName, namespace: K8sNamespace): ZIO[NamespacedResource[T], K8sFailure, Option[T]]
  def get(resource: K8sResource): ZIO[Services with Deployments with ConfigMaps with SparkApplications with ScheduledSparkApplications with Secrets, K8sFailure, Option[K8sResource]]

}
object LiveDeployerService extends DeployerService {
  override def replace(updatedResource: K8sResource): ZIO[Services with Deployments with ConfigMaps with SparkApplications with ScheduledSparkApplications with Secrets, K8sFailure, Unit] = {
    val update = updatedResource match {
      case K8sDeployment(dep, ns, name) => deployments.replace(name.value, dep, ns)
      case K8sService(svc, ns, name) => services.replace(name.value, svc, ns)
      case K8sConfigMap(cm, ns, name) => configmaps.replace(name.value, cm, ns)
      case K8sSecret(s, ns, name) => secrets.replace(name.value, s, ns)
      case K8sSparkApp(app, ns, name) => sparkapplications.replace(name.value, app, ns)
      case K8sScheduledSparkApp(app, ns, name) => scheduledsparkapplications.replace(name.value, app, ns)
    }
    update.unit
  }


  override def deploy(resource: K8sResource): ZIO[Services with Deployments with ConfigMaps with SparkApplications with ScheduledSparkApplications with Secrets, K8sFailure, Unit] = resource match {
    case K8sDeployment(dep, ns, name) => deployments.create(dep, ns)
      .map(d => K8sDeployment(d, ns, name))
    case K8sService(svc, ns, name) => services.create(svc, ns)
      .map(s => K8sService(s, ns, name))
    case K8sConfigMap(cm, ns, name) => configmaps.create(cm, ns)
      .map(cm => K8sConfigMap(cm, ns, name))
    case K8sSecret(s, ns, name) => secrets.create(s, ns)
      .map(s => K8sSecret(s, ns, name))
    case K8sSparkApp(app, ns, name) => sparkapplications.create(app, ns)
      .map(app => K8sSparkApp(app, ns, name))
    case K8sScheduledSparkApp(app, ns, name) => scheduledsparkapplications.create(app, ns)
      .map(cm => K8sScheduledSparkApp(cm, ns, name))
  }

  override def delete(resource: K8sResource): ZIO[Services with Deployments with ConfigMaps with SparkApplications with ScheduledSparkApplications with Secrets, K8sFailure, Status] = resource match {
    case K8sDeployment(dep, ns, K8SResourceName(name)) => deployments.delete(name, DeleteOptions(), ns)
    case K8sService(svc, ns, K8SResourceName(name)) => services.delete(name, DeleteOptions(), ns)
    case K8sConfigMap(cm, ns, K8SResourceName(name)) => configmaps.delete(name, DeleteOptions(), ns)
    case K8sSecret(s, ns, K8SResourceName(name)) => secrets.delete(name, DeleteOptions(), ns)
    case K8sSparkApp(app, ns, K8SResourceName(name)) => sparkapplications.delete(name, DeleteOptions(), ns)
    case K8sScheduledSparkApp(app, ns, K8SResourceName(name)) => scheduledsparkapplications.delete(name, DeleteOptions(), ns)
  }


  override def get[T : zio.Tag](name: K8SResourceName, namespace: K8sNamespace): ZIO[NamespacedResource[T], K8sFailure, Option[T]] =
    for {
      svc <- ZIO.service[NamespacedResource[T]]
      result <- svc.get(name.value, namespace).ifFound
    } yield(result)

  override def get(resource: K8sResource): ZIO[Services with Deployments with ConfigMaps with SparkApplications with ScheduledSparkApplications with Secrets, K8sFailure, Option[K8sResource]] = resource match {
    case K8sDeployment(_, ns, name) => deployments.get(name.value, ns)
      .map(d => K8sDeployment(d, ns, name))
      .ifFound
    case K8sService(_, ns, name) => services.get(name.value, ns)
      .map(s => K8sService(s, ns, name))
      .ifFound
    case K8sConfigMap(_, ns, name) => configmaps.get(name.value, ns)
      .map(cm => K8sConfigMap(cm, ns, name))
      .ifFound
    case K8sSecret(_, ns, name) => secrets.get(name.value, ns)
      .map(s => K8sSecret(s, ns, name))
      .ifFound
    case K8sSparkApp(_, ns, name) => sparkapplications.get(name.value, ns)
      .map(app => K8sSparkApp(app, ns, name))
      .ifFound
    case K8sScheduledSparkApp(_, ns, name) => scheduledsparkapplications.get(name.value, ns)
      .map(cm => K8sScheduledSparkApp(cm, ns, name))
    .ifFound
  }
}



