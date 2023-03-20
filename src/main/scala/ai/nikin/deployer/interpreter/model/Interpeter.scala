package ai.nikin.deployer.interpreter.model

trait JsonGraph
trait Definition
trait InterpretedGraph{
  val name: String
  val definitions: Map[String, Definition]
}
trait Interpreter {
  def interpret(jsonGraph: JsonGraph): InterpretedGraph

}
