package ai.nikin.deployer.model

import com.coralogix.zio.k8s.client.model.K8sNamespace
import com.coralogix.zio.k8s.client.sparkoperator.definitions.scheduledsparkapplication.v1beta2.ScheduledSparkApplication
import com.coralogix.zio.k8s.client.sparkoperator.definitions.sparkapplication.v1beta2.SparkApplication
import com.coralogix.zio.k8s.model.apps.v1.Deployment
import com.coralogix.zio.k8s.model.core.v1.{ConfigMap, Secret, Service}

case class K8SResourceName(value: String)
sealed trait K8sResource{
  val name: K8SResourceName
  val namespace: K8sNamespace
}
object K8sResource  {
  case class K8sService(svc: Service, override val namespace: K8sNamespace, override val name: K8SResourceName) extends K8sResource
  case class K8sConfigMap(cm: ConfigMap, override val namespace: K8sNamespace, override val name: K8SResourceName) extends K8sResource
  case class K8sDeployment(dep: Deployment, override val namespace: K8sNamespace, override val name: K8SResourceName) extends K8sResource
  case class K8sSparkApp(app: SparkApplication, override val namespace: K8sNamespace, override val name: K8SResourceName) extends K8sResource
  case class K8sScheduledSparkApp(app: ScheduledSparkApplication, override val namespace: K8sNamespace, override val name: K8SResourceName) extends K8sResource
  case class K8sSecret(secret: Secret, override val namespace: K8sNamespace, override val name: K8SResourceName) extends K8sResource
}