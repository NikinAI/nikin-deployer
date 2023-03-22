package ai.nikin.deployer.interpreter.model

import org.apache.avro.reflect.AvroSchema


sealed trait AggFunction
object AggFunction {
  case object Min extends AggFunction
  case object Max extends AggFunction
  case object Avg extends AggFunction
}

case class JAR(location: String, version: Version)
case class Version(version: String) extends AnyVal
case class DDL(value: String) extends AnyVal
case class Schema(name: String, avroSchema: AvroSchema)
case class Lake(name: String, schema: Schema, ddl: DDL) extends Definition
case class IngestService(name: String,
                         address: Int) extends Definition
case class SparkApplication(
                           name: String,
                           jar: JAR,
                           inputLocation: String,
                           outputLocation: String,
                           function: AggFunction,
                           inputColName: String,
                           outputColName: String
                           ) extends Definition

case class Table(
                name: String,
                schema: Schema,
                ddl: DDL
                ) extends Definition