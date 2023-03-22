package ai.nikin.deployer.delta.repository

import ai.nikin.deployer.interpreter.model.InterpretedGraph
import zio.Task

trait DeltaRepository {
  def getGraph(graphName: String): Task[Option[InterpretedGraph]]
}
