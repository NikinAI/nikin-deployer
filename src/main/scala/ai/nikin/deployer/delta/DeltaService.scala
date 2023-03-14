package ai.nikin.deployer.delta

import ai.nikin.deployer.delta.model.Graph
import ai.nikin.deployer.deployment.model.{NodeType, NodeUpdate}
import zio.Task

trait DeltaService {
  def calculateDelta(updatedGraph: Graph): Task[Seq[NodeUpdate[NodeType]]]
}
