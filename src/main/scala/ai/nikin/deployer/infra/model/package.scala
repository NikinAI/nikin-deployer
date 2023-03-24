package ai.nikin.deployer.infra

package object model {
  trait InfraResource
  case class Bucket(region: String, location: String) extends InfraResource
  case class KVTable(region: String, tableName: String) extends InfraResource

}