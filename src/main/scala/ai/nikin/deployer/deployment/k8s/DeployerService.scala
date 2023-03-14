package ai.nikin.deployer.deployment.k8s

import ai.nikin.deployer.deployment.model.K8sResource._
import ai.nikin.deployer.deployment.model.ResourceType._
import ai.nikin.deployer.deployment.model.{K8SResourceName, K8sResource, ResourceType}
import com.coralogix.zio.k8s.client.K8sFailure
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
import com.coralogix.zio.k8s.model.pkg.apis.meta.v1.{DeleteOptions, Status}
import zio.ZIO

trait DeployerService {
  def deploy(resource: K8sResource): ZIO[Services with Deployments with ConfigMaps with SparkApplications with ScheduledSparkApplications with Secrets, K8sFailure, Unit]
  def replace(updatedResource: K8sResource): ZIO[Services with Deployments with ConfigMaps with SparkApplications with ScheduledSparkApplications with Secrets, K8sFailure, Unit]
  def delete(name: K8SResourceName, namespace: K8sNamespace, resourceType: ResourceType): ZIO[Services with Deployments with ConfigMaps with SparkApplications with ScheduledSparkApplications with Secrets, K8sFailure, Status]
  def get(name: K8SResourceName, namespace: K8sNamespace, resourceType: ResourceType): ZIO[Services with Deployments with ConfigMaps with SparkApplications with ScheduledSparkApplications with Secrets, K8sFailure, Option[K8sResource]]
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
  override def delete(name: K8SResourceName, namespace: K8sNamespace, resourceType: ResourceType): ZIO[Services with Deployments with ConfigMaps with SparkApplications with ScheduledSparkApplications with Secrets, K8sFailure, Status] = resourceType match {
    case Deployment => deployments.delete(name.value, DeleteOptions(), namespace)
    case Service => services.delete(name.value, DeleteOptions(), namespace)
    case ConfigMap => configmaps.delete(name.value, DeleteOptions(), namespace)
    case Secret => secrets.delete(name.value, DeleteOptions(), namespace)
    case SparkApp => sparkapplications.delete(name.value, DeleteOptions(), namespace)
    case ScheduledSparkApp => scheduledsparkapplications.delete(name.value, DeleteOptions(), namespace)
  }

  override def get(name: K8SResourceName, namespace: K8sNamespace, resourceType: ResourceType): ZIO[Services with Deployments with ConfigMaps with SparkApplications with ScheduledSparkApplications with Secrets, K8sFailure, Option[K8sResource]] = resourceType match {
      case Deployment => deployments.get(name.value, namespace)
        .map(d => K8sDeployment(d, namespace, name)).ifFound
      case Service => services.get(name.value, namespace)
        .map(s => K8sService(s, namespace, name)).ifFound
      case ConfigMap => configmaps.get(name.value, namespace)
        .map(cm => K8sConfigMap(cm, namespace, name)).ifFound
      case Secret => secrets.get(name.value, namespace)
        .map(s => K8sSecret(s, namespace, name)).ifFound
      case SparkApp => sparkapplications.get(name.value, namespace)
        .map(app => K8sSparkApp(app, namespace, name)).ifFound
      case ScheduledSparkApp => scheduledsparkapplications.get(name.value, namespace)
        .map(app => K8sScheduledSparkApp(app, namespace, name)).ifFound
    }
}



