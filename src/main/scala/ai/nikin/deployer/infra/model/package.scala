package ai.nikin.deployer.infra

package object model {
  trait InfraResource
  case class Bucket(region: String, name: String) extends InfraResource
  case class Database(region: String, name: String) extends InfraResource

}
