package ai.nikin.deployer.interpreter

import ai.nikin.pipeline.model.dsl._
import scalax.collection.edges.DiEdge
import model.InterpretedPipeline
import scalax.collection.immutable.Graph

trait Interpreter {
  def interpret(untypedGraph: Graph[UntypedVertex, DiEdge[UntypedVertex]]): InterpretedPipeline
}
