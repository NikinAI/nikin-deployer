package ai.nikin.deployer.interpreter

import ai.nikin.pipeline.model.dsl._
import scalax.collection.Graph
import scalax.collection.GraphEdge.DiEdge
import model.InterpretedPipeline
trait Interpreter {
  def interpret(untypedGraph: Graph[UntypedVertex, DiEdge]): InterpretedPipeline
}
