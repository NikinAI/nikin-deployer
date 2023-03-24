package ai.nikin.deployer.infra
import ai.nikin.deployer.infra.model._
import zio.Task
trait InfraDeployer {
  def createResource[T <: InfraResource](resource: T): Task[Unit]
}


