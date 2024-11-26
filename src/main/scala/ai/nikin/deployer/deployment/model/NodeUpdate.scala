package ai.nikin.deployer.deployment.model

import ai.nikin.deployer.interpreter.model.{DDL, JAR}

trait NodeType
case class LakeNode(name: String, ddl: DDL) extends NodeType
case class HttpService(name: String, address: Int) extends NodeType
case class SparkApplication(name: String, jar: JAR) extends NodeType
case class ScheduledSparkApplication(name: String, jar: JAR, schedule: String) extends NodeType
trait NodeUpdate[T <: NodeType] {
  val resource: T
}

case class Create[T <: NodeType](override val resource: T) extends NodeUpdate[T]
case class Update[T <: NodeType](override val resource: T) extends NodeUpdate[T]
case class Delete[T <: NodeType](override val resource: T) extends NodeUpdate[T]
