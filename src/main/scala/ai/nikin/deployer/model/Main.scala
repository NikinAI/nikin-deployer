package ai.nikin.deployer.model

import com.coralogix.zio.k8s.client.model.K8sNamespace
import com.coralogix.zio.k8s.client.sparkoperator.definitions.sparkapplication.v1beta2.SparkApplication
import com.coralogix.zio.k8s.client.sparkoperator.v1beta2.sparkapplications
import zio.{Scope, ZIO, ZIOAppArgs, ZIOAppDefault}

object Main extends ZIOAppDefault{


  override def run: ZIO[Any with ZIOAppArgs with Scope, Any, Any] ={
    sparkapplications.create(
      SparApp
    )
  }
}
