package ai.nikin.deployer.k8s

import com.coralogix.zio.k8s.client.K8sFailure
import com.coralogix.zio.k8s.client.model.K8sNamespace
import com.coralogix.zio.k8s.model.pkg.apis.meta.v1.Status
import zio.ZIO
case class K8SResourceName(name: String)

trait K8SDeployer[S, T] {
  def getAll(ns: K8sNamespace): ZIO[S, K8sFailure, List[T]]
  def get(ns: K8sNamespace, k8SResourceName: K8SResourceName): ZIO[S, K8sFailure, T]
  def delete(ns: K8sNamespace, k8SResourceName: K8SResourceName): ZIO[S, K8sFailure, Status]
  def create(definition: T, ns: K8sNamespace,): ZIO[S, K8sFailure, T]
}
