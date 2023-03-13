package ai.nikin.deployer.deployment.model

import com.coralogix.zio.k8s.client.model.K8sNamespace

trait NodeType
case class HttpService(address: Int) extends NodeType
case class SparkApplication(jarLocation: String) extends NodeType
case class ScheduledSparkApplication(jarLocation: String) extends NodeType
trait NodeUpdate[T <: NodeType]{
  val resource: T}

case class Create[T <: NodeType](override val resource: T) extends NodeUpdate[T]
case class Update[T <: NodeType](override val resource: T) extends NodeUpdate[T]
case class Delete[T <: NodeType](override val resource: T) extends NodeUpdate[T]