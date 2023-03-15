package ai.nikin.deployer.deployment.service

import ai.nikin.deployer.deployment.k8s
import ai.nikin.deployer.deployment.k8s.{DeployerService, K8sResourceMapper}
import ai.nikin.deployer.deployment.model.ResourceType.SparkApp
import ai.nikin.deployer.deployment.model.{Create, Delete, HttpService, K8sResource, NodeType, NodeUpdate, Update}
import com.coralogix.zio.k8s.client.K8sFailure
import com.coralogix.zio.k8s.client.apps.v1.deployments.Deployments
import com.coralogix.zio.k8s.client.sparkoperator.definitions.sparkapplication.v1beta2.SparkApplication
import com.coralogix.zio.k8s.client.sparkoperator.v1beta2.scheduledsparkapplications.ScheduledSparkApplications
import com.coralogix.zio.k8s.client.sparkoperator.v1beta2.sparkapplications.SparkApplications
import com.coralogix.zio.k8s.client.v1.configmaps.ConfigMaps
import com.coralogix.zio.k8s.client.v1.secrets.Secrets
import com.coralogix.zio.k8s.client.v1.services.Services
import zio.ZIO

trait GraphUpdateService {
  def performGraphUpdate(graphUpdate: Seq[NodeUpdate[NodeType]]): ZIO[Services with Deployments with ConfigMaps with SparkApplications with ScheduledSparkApplications with Secrets, K8sFailure, Seq[Unit]]
}

class K8sGraphUpdateService(deployerService: DeployerService) extends GraphUpdateService {
  override def performGraphUpdate(graphUpdate: Seq[NodeUpdate[NodeType]]): ZIO[Services with Deployments with ConfigMaps with SparkApplications with ScheduledSparkApplications with Secrets, K8sFailure, Seq[Unit]] = {
    val resourceUpdates = graphUpdate
      .map(nU => K8sResourceMapper.mapToResources(nU.resource))
      .flatten

    val (deleteOperations, createOperations) = resourceUpdates.partition {
      case _: k8s.Delete => true
      case _ => false
    }

    val deleteResult = deleteOperations.map {
      case k8s.Delete(rsc) => deployerService.delete(rsc)
    }

    ZIO.collectAll(deleteResult)
      .orElse(ZIO.collectAll(deleteOperations.map(r => deployOpt(r.resource))))
      .flatMap(_ =>
        ZIO.collectAll(createOperations.map(cr => deployerService.deploy(cr.resource)))
          .orElse(ZIO.collectAll(createOperations.map(cr => deleteOpt(cr.resource))))

      )
  }

  private def deployOpt(resource: K8sResource) = {
    deployerService
      .get(resource)
      .flatMap{
        case Some(_) => ZIO.unit
        case None => deployerService.deploy(resource).unit
      }
  }

  private def deleteOpt(resource: K8sResource) = {
    deployerService
      .get(resource)
      .flatMap {
        case Some(_) => deployerService.delete(resource).unit
        case None => ZIO.unit
      }
  }


}