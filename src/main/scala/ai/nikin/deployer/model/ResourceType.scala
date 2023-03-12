package ai.nikin.deployer.model

trait ResourceType
object ResourceType {
  case object Service extends ResourceType
  case object Deployment extends ResourceType
  case object Secret extends ResourceType
  case object ConfigMap extends ResourceType
  case object SparkApp extends ResourceType
  case object ScheduledSparkApp extends ResourceType
}
