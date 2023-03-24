package ai.nikin.deployer.delta

import ai.nikin.deployer.delta.model.Pipeline
import ai.nikin.deployer.deployment.model.{NodeType, NodeUpdate}
import zio.Task

trait DeltaCalculatorService {
  def calculateDelta(updatedPipeline: Pipeline): Task[Seq[NodeUpdate[NodeType]]]
}
