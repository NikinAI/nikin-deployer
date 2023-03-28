package ai.nikin.deployer.delta

import ai.nikin.deployer.delta.repository.DeltaRepository
import ai.nikin.deployer.deployment.model
import ai.nikin.deployer.deployment.model._
import ai.nikin.deployer.interpreter.model.{IngestService, Lake, SparkApplication}
import ai.nikin.deployer.interpreter.model.InterpretedPipeline
import zio.Task



trait DeltaCalculatorService {
  def calculateDelta(updatedPipeline: InterpretedPipeline): Task[Seq[NodeUpdate[_]]]
}

class NoDeltaService(deltaRepository: DeltaRepository) extends DeltaCalculatorService {
  private def generateDeletions(existingPipeline: InterpretedPipeline): List[Delete[_]] =
    existingPipeline.definitions.values.toList.collect {
      case SparkApplication(name, jar, inputLocation, outputLocation, function, inputColName, outputColName) =>
        Delete(model.SparkApplication(name, jar))
      case IngestService(name, address) =>
        Delete(HttpService(name, address))
    }

  override def calculateDelta(updatedPipeline: InterpretedPipeline): Task[List[NodeUpdate[_]]] = {
    val existingGraph = deltaRepository.getGraph(updatedPipeline.name)

    val deleteExisting = existingGraph
      .map(_.map(generateDeletions(_))
        .getOrElse(List.empty)
      )

    val deltaUpdates = updatedPipeline.definitions.values.collect {
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
