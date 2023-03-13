package ai.nikin.deployer.deployment.service

import ai.nikin.deployer.deployment.model.{K8sResource, NodeType}

object K8sResourceMapper {
  def mapToResources(nodeType: NodeType):Seq[K8sResource] = ???
}
