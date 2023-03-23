package ai.nikin.deployer.delta

import ai.nikin.deployer.delta.repository.DeltaRepository
import ai.nikin.deployer.deployment.model
import ai.nikin.deployer.deployment.model.{Create, Delete, HttpService, LakeNode, NodeType, NodeUpdate}
import ai.nikin.deployer.interpreter.model.{IngestService, InterpretedPipeline, Lake, SparkApplication}
import zio.{Task, UIO, ZIO}



trait DeltaCalculatorService {
  def calculateDelta(updatedPipeline: InterpretedPipeline): Task[Seq[NodeUpdate[_]]]
}

class NoDeltaService(deltaRepository: DeltaRepository) extends DeltaCalculatorService{
  override def calculateDelta(updatedPipeline: InterpretedPipeline): Task[List[NodeUpdate[_]]] = {
    val existingGraph = deltaRepository.getGraph(updatedPipeline.name)

    val deleteExisting = existingGraph
      .map{graphOpt => graphOpt.map(_.definitions.values.collect {
        case SparkApplication(name, jar, inputLocation, outputLocation, function, inputColName, outputColName) =>
          Delete(model.SparkApplication(name, jar))
        case IngestService(name, address) =>
          Delete(HttpService(name, address))
      }.toList).getOrElse(List.empty[NodeUpdate[NodeType]])
      }

    val deltaUpdates = updatedPipeline.definitions.values.collect{
      case Lake(name, schema, ddl) =>
        Create(LakeNode(name, ddl))
    }.toList


    val recreate = updatedPipeline.definitions.values.collect {
        case SparkApplication(name, jar, inputLocation, outputLocation, function, inputColName, outputColName) =>
          Create(model.SparkApplication(name, jar))
        case IngestService(name, address) =>
          Create(HttpService(name, address))
      }.toList

    val allUpdates =  deleteExisting.map(_ ++ deltaUpdates ++ recreate)

    allUpdates
  }
}
