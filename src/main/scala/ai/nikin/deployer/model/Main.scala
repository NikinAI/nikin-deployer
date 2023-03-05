package ai.nikin.deployer.model

import ai.nikin.deployer.k8s.SparkDeployer
import com.coralogix.zio.k8s
import com.coralogix.zio.k8s.client.config.{asynchttpclient, k8sCluster, kubeconfig}
import com.coralogix.zio.k8s.client.model.K8sNamespace
import com.coralogix.zio.k8s.client.sparkoperator.definitions.sparkapplication.v1beta2.SparkApplication
import com.coralogix.zio.k8s.client.sparkoperator.v1beta2.sparkapplications
import zio.{Scope, ZIO, ZIOAppArgs, ZIOAppDefault}

object Main extends ZIOAppDefault{

  val config = kubeconfig(disableHostnameVerification = true)
    .project(cfg => cfg.dropTrailingDot)

  // K8s configuration and client layers
  val client = config >>> k8s.client.config.asynchttpclient.k8sSttpClient
  val cluster = config >>> k8sCluster

  val sparkApplication = (client ++ cluster) >>> sparkapplications.SparkApplications.live
  override def run: ZIO[Any with ZIOAppArgs with Scope, Any, Any] = {
    SparkDeployer.getAll(K8sNamespace("default"))
      .provideLayer(sparkApplication)

  }
}
