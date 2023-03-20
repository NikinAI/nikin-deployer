package ai.nikin.deployer.delta.repository

import ai.nikin.deployer.interpreter.model.InterpretedGraph

trait DeltaRepository {
  def getGraph(graphName: String): Option[InterpretedGraph]
}
