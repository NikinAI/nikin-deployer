package ai.nikin.deployer.interpreter.model

import org.apache.avro.reflect.AvroSchema


sealed trait AggFunction
object AggFunction {
  case object Min extends AggFunction
  case object Max extends AggFunction
  case object Avg extends AggFunction
}

case class Schema(name: String, avroSchema: AvroSchema)
case class Lake(name: String, schema: Schema) extends Definition
case class SparkApplication(
                           name: String,
                           inputLake: String,
                           outputLake: String,
                           function: AggFunction,
                           inputColName: String,
                           outputColName: String
                           ) extends Definition