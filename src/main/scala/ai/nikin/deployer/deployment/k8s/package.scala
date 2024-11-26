package ai.nikin.deployer.deployment

import com.coralogix.zio.k8s.client.sparkoperator.definitions.scheduledsparkapplication.v1beta2.ScheduledSparkApplication
import com.coralogix.zio.k8s.client.sparkoperator.definitions.sparkapplication.v1beta2.SparkApplication
import io.circe.yaml.parser.parse

import java.nio.file.{Files, Paths}
package object k8s {

  object K8sSparkApplication {
    lazy val defaultApp: SparkApplication =
      parse(
        Files.readString(Paths.get(getClass.getResource("/examples/sparkApplication.yml").toURI))
      ).map(_.as[SparkApplication]).right.get.right.get
  }

  object K8sScheduledSparkApplication {
    lazy val defaultApp: ScheduledSparkApplication =
      parse(
        Files.readString(
          Paths.get(getClass.getResource("/examples/scheduledSparkApplication.yml").toURI)
        )
      ).map(_.as[ScheduledSparkApplication]).right.get.right.get
  }
}
