package ai.nikin.deployer.k8s

import com.coralogix.zio.k8s.client.sparkoperator.definitions.scheduledsparkapplication.v1beta2.ScheduledSparkApplication
import com.coralogix.zio.k8s.client.sparkoperator.definitions.sparkapplication.v1beta2.SparkApplication
import com.coralogix.zio.k8s.client.sparkoperator.v1beta2.scheduledsparkapplications
import com.coralogix.zio.k8s.client.sparkoperator.v1beta2.scheduledsparkapplications.ScheduledSparkApplications
import com.coralogix.zio.k8s.client.{K8sFailure, model}
import com.coralogix.zio.k8s.model.pkg.apis.meta.v1.{DeleteOptions, Status}
import zio.ZIO
import zio.stream.ZSink

object SparkSchedulerDeployer extends K8SDeployer [ScheduledSparkApplications.Service, ScheduledSparkApplication]{
  override def getAll(ns: model.K8sNamespace): ZIO[ScheduledSparkApplications.Service, K8sFailure, List[ScheduledSparkApplication]] = {
    val sink = ZSink.collectAllToSet[ScheduledSparkApplication]

    scheduledsparkapplications.getAll(Some(ns))
      .run(sink)
      .map(_.toList)
  }

  override def get(ns: model.K8sNamespace, k8SResourceName: K8SResourceName): ZIO[ScheduledSparkApplications.Service, K8sFailure, ScheduledSparkApplication] =
    scheduledsparkapplications.get(k8SResourceName.name, ns)

  override def delete(ns: model.K8sNamespace, k8SResourceName: K8SResourceName): ZIO[ScheduledSparkApplications.Service, K8sFailure, Status] =
    scheduledsparkapplications.delete(k8SResourceName.name, DeleteOptions(None, None, None, None, None), ns)

  override def create(ns: model.K8sNamespace, definition: ScheduledSparkApplication): ZIO[ScheduledSparkApplications.Service, K8sFailure, ScheduledSparkApplication] = {
    scheduledsparkapplications.create(definition, ns)
  }
}
