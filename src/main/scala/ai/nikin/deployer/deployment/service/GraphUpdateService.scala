package ai.nikin.deployer.deployment.service

import ai.nikin.deployer.deployment.model.ResourceType.SparkApp
import ai.nikin.deployer.deployment.model.{Create, HttpService, K8sResource, NodeType, NodeUpdate, Update}
import com.coralogix.zio.k8s.client.sparkoperator.definitions.sparkapplication.v1beta2.SparkApplication

trait GraphUpdateService {
  def performGraphUpdate(graphUpdate: Seq[NodeUpdate[NodeType]])
}

class LiveGraphUpdateService(deployerService: DeployerService) extends GraphUpdateService {
  override def performGraphUpdate(graphUpdate: Seq[NodeUpdate[NodeType]]): Unit = {
    val resourceUpdates = graphUpdate
      .map(nU => K8sResourceMapper.mapToResources(nU.resource))

    graphUpdate.zip(resourceUpdates)
      .map{
        case (Create(res), resourceUpdates) => resourceUpdates.map(deployerService.deploy(_))
        case (Update(res), resourceUpdates) => resourceUpdates
          .map(r => deployerService.get(r.name, r.namespace, r.resourceType))
          .map(_.*>)
      }

    SparkApplication.Spec()
  }
}