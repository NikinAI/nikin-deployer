package ai.nikin.deployer.deployment.k8s

import ai.nikin.deployer.deployment.model.K8sResource.{K8sScheduledSparkApp, K8sSparkApp}
import ai.nikin.deployer.deployment.model.{
  K8sResource,
  K8SResourceName,
  NodeType,
  ScheduledSparkApplication,
  SparkApplication
}
import com.coralogix.zio.k8s.client.model.K8sNamespace

trait K8sResourceUpdate {
  val resource: K8sResource
}
case class Create(override val resource: K8sResource) extends K8sResourceUpdate
case class Delete(override val resource: K8sResource) extends K8sResourceUpdate

object K8sResourceMapper {
  // In case of SparkApps, ScheduledSparkApps and potentially other stateful resources, the Update will be translated to Seq(Delete(), Create())
  // to avoid problems and make sure everything is correctly propagated.
  def mapToResources(nodeType: NodeType): Seq[K8sResourceUpdate] =
    nodeType match {
      case SparkApplication(name, jar) =>
        val defaultApp = K8sSparkApplication.defaultApp
        Seq(
          Delete(K8sSparkApp(defaultApp, K8sNamespace("default"), K8SResourceName(name))),
          Create(K8sSparkApp(defaultApp, K8sNamespace("default"), K8SResourceName(name)))
        )

      case ScheduledSparkApplication(name, jar) =>
        val defaultApp = K8sScheduledSparkApplication.defaultApp
        Seq(
          Delete(K8sScheduledSparkApp(defaultApp, K8sNamespace("default"), K8SResourceName(name))),
          Create(K8sScheduledSparkApp(defaultApp, K8sNamespace("default"), K8SResourceName(name)))
        )
    }
}
