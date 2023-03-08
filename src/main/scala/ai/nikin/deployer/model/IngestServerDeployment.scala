package ai.nikin.deployer.model

import com.coralogix.zio.k8s.client.sparkoperator.definitions.sparkapplication.v1beta2.SparkApplication
import com.coralogix.zio.k8s.client.sparkoperator.definitions.scheduledsparkapplication.v1beta2.ScheduledSparkApplication

import com.coralogix.zio.k8s.model.apps.v1.Deployment
import com.coralogix.zio.k8s.model.core.v1.{Secret, Service, ConfigMap}
import com.coralogix.zio.k8s.client.model.K8sNamespace
case class IngestServerDeploymentDescriptor(service: Service, deployment: Deployment, configMap: ConfigMap,
                                            secrets: Seq[Secret], namespace: K8sNamespace)

case class SparkApplicationDeploymentDescriptor(sparkApplication: SparkApplication, configMap: ConfigMap,
                                                secrets: Seq[Secret], namespace: K8sNamespace)

case class ScheduledSparkApplicationDeploymentDescriptor(sparkApplication: ScheduledSparkApplication, configMap: ConfigMap,
                                                         secrets: Seq[Secret], namespace: K8sNamespace)

