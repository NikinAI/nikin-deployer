package ai.nikin.deployer.infra.model

import com.coralogix.zio.k8s.client.io.upbound.aws.kms.definitions.key.v1beta1.Key
import com.coralogix.zio.k8s.client.io.upbound.aws.rds.definitions.instance.v1beta1.Instance
import com.coralogix.zio.k8s.client.io.upbound.aws.s3.definitions.bucket.v1beta1.Bucket
import com.coralogix.zio.k8s.client.io.upbound.aws.s3.definitions.bucketacl.v1beta1.BucketAcl
import com.coralogix.zio.k8s.client.io.upbound.aws.s3.definitions.bucketpublicaccessblock.v1beta1.BucketPublicAccessBlock
import com.coralogix.zio.k8s.model.core.v1.Secret
import io.circe.yaml.parser.parse

import java.nio.file.{Files, Paths}
import scala.util.Random
package object k8s {

  def getRandomK8sString(): String = List.fill(10)(Random.between(97, 123)).map(_.toChar).mkString

  object K8sBucket {
    lazy val defaultBucket: Bucket =
      parse(
        Files.readString(Paths.get(getClass.getResource("/examples/bucket.yml").toURI))
      ).map(_.as[Bucket]).right.get.right.get

    lazy val defaultBlock: BucketPublicAccessBlock =
      parse(
        Files.readString(Paths.get(getClass.getResource("/examples/bucketAcl.yml").toURI))
      ).map(_.as[BucketPublicAccessBlock]).right.get.right.get
  }

  object K8sDatabase {
    lazy val defaultInstance: Instance =
      parse(
        Files.readString(Paths.get(getClass.getResource("/examples/rds.yml").toURI))
      ).map(_.as[Instance]).right.get.right.get

    lazy val defaultBlock: BucketPublicAccessBlock =
      parse(
        Files.readString(Paths.get(getClass.getResource("/examples/bucketAcl.yml").toURI))
      ).map(_.as[BucketPublicAccessBlock]).right.get.right.get
  }

  object K8sKmsKey {
    lazy val defaultKey: Key =
      parse(
        Files.readString(Paths.get(getClass.getResource("/examples/kmsKey.yml").toURI))
      ).map(_.as[Key]).right.get.right.get
  }

  object K8sSecret {
    lazy val defaultSecret: Secret =
      parse(
        Files.readString(Paths.get(getClass.getResource("/examples/secret.yml").toURI))
      ).map(_.as[Secret]).right.get.right.get
  }
}
