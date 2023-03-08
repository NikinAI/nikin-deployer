package ai.nikin.deployer.k8s

import com.coralogix.zio.k8s.client.{K8sFailure, model}
import com.coralogix.zio.k8s.client.sparkoperator.definitions.sparkapplication.v1beta2.SparkApplication
import com.coralogix.zio.k8s.client.sparkoperator.v1beta2.sparkapplications
import com.coralogix.zio.k8s.client.sparkoperator.v1beta2.sparkapplications.SparkApplications
import com.coralogix.zio.k8s.model.pkg.apis.meta.v1.{DeleteOptions, Status}
import zio.{Task, ZIO}
import zio.stream.ZSink

object SparkAppDeployer extends K8SDeployer [SparkApplications.Service, SparkApplication]{
  override def getAll(ns: model.K8sNamespace): ZIO[SparkApplications.Service, K8sFailure, List[SparkApplication]] = {
    val sink = ZSink.collectAllToSet[SparkApplication]

    sparkapplications.getAll(Some(ns))
      .run(sink)
      .map(_.toList)
  }

  override def get(ns: model.K8sNamespace, k8SResourceName: K8SResourceName): ZIO[SparkApplications.Service, K8sFailure, SparkApplication] =
    sparkapplications.get(k8SResourceName.name, ns)

  override def delete(ns: model.K8sNamespace, k8SResourceName: K8SResourceName): ZIO[SparkApplications.Service, K8sFailure, Status] =
    sparkapplications.delete(k8SResourceName.name, DeleteOptions(None, None, None, None, None), ns)

  override def create(ns: model.K8sNamespace, definition: SparkApplication): ZIO[SparkApplications.Service, K8sFailure, SparkApplication] = {
    sparkapplications.create(definition, ns)
  }
}
