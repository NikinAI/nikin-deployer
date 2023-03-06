package ai.nikin.deployer.model

import ai.nikin.deployer.k8s.SparkDeployer
import com.coralogix.zio.k8s
import com.coralogix.zio.k8s.client.config.{asynchttpclient, k8sCluster, kubeconfig}
import com.coralogix.zio.k8s.client.model.K8sNamespace
import com.coralogix.zio.k8s.client.sparkoperator.definitions.sparkapplication.v1beta2.SparkApplication
import com.coralogix.zio.k8s.client.sparkoperator.v1beta2.sparkapplications
import com.coralogix.zio.k8s.client.v1.pods
import com.coralogix.zio.k8s.client.v1.pods.Pods
import com.coralogix.zio.k8s.model.core.v1.Pod
import zio.stream.ZSink
import zio.{Scope, ZIO, ZIOAppArgs, ZIOAppDefault}

object Main extends ZIOAppDefault{

  val config = kubeconfig(disableHostnameVerification = true)
    .project(cfg => cfg.dropTrailingDot)

  // K8s configuration and client layers
  val client = config >>> k8s.client.config.asynchttpclient.k8sSttpClient
  val cluster = config >>> k8sCluster

  val sparkApplication = (client ++ cluster) >>> Pods.live
  override def run: ZIO[Any with ZIOAppArgs with Scope, Any, Any] = {
    val sink = ZSink.collectAllToSet[Pod]

    pods.getAll(Some(K8sNamespace("kube-system")))
      .run(sink)
      .map(_.toList)
      .map(println(_))
      .provide(sparkApplication)
  }
}
