package ai.nikin.deployer.deployment.k8s

import ai.nikin.deployer.deployment.model.{K8sResource, NodeType}

trait K8sResourceUpdate {
  val resource: K8sResource
}
case class Create(override val resource: K8sResource) extends K8sResourceUpdate
case class Delete(override val resource: K8sResource) extends K8sResourceUpdate

object K8sResourceMapper {
  // In case of SparkApps, ScheduledSparkApps and potentially other stateful resources, the Update will be translated to Seq(Delete(), Create())
  // to avoid problems and make sure everything is correctly propagated.
  def mapToResources(nodeType: NodeType): Seq[K8sResourceUpdate] = ???
}
