package ai.nikin.deployer.deployment.k8s

import ai.nikin.deployer.deployment.model.K8sResource.{K8sScheduledSparkApp, K8sSparkApp}
import ai.nikin.deployer.deployment.model.{K8SResourceName, K8sResource, NodeType, ScheduledSparkApplication, SparkApplication}
import com.coralogix.zio.k8s.client.model.K8sNamespace
import com.softwaremill.quicklens.{ModifyPimp, QuicklensEach}
import com.coralogix.zio.k8s.quicklens._

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
          .modify(_.metadata.name.each).setTo(name)
          .modify(_.spec.mainApplicationFile.each).setTo(jar.location)
        Seq(
          Delete(K8sSparkApp(defaultApp, K8sNamespace("default"), K8SResourceName(name))),
          Create(K8sSparkApp(defaultApp, K8sNamespace("default"), K8SResourceName(name)))
        )

      case ScheduledSparkApplication(name, jar, schedule) =>
        val defaultApp = K8sScheduledSparkApplication.defaultApp
          .modify(_.metadata.name.each).setTo(name)
          .modify(_.spec.template.mainApplicationFile).setTo(jar.location)
          .modify(_.spec.schedule).setTo(schedule)


        Seq(
          Delete(K8sScheduledSparkApp(defaultApp, K8sNamespace("default"), K8SResourceName(name))),
          Create(K8sScheduledSparkApp(defaultApp, K8sNamespace("default"), K8SResourceName(name)))
        )
    }
}
