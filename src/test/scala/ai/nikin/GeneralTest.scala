//package ai.nikin
//
//import ai.nikin.deployer.delta.DeltaCalculatorService
//import ai.nikin.deployer.delta.model.Pipeline
//import ai.nikin.deployer.deployment.service.PipelineUpdateService
//import ai.nikin.deployer.interpreter.model.{Interpreter, JsonGraph}
//import zio.{Scope, ZIO}
//import zio.test.{Spec, TestEnvironment, ZIOSpecDefault}
//import zio._
//import zio.test._
//import zio.test.Assertion._
//
//object GeneralTest extends ZIOSpecDefault{
//  override def spec: Spec[TestEnvironment with Scope, Any] = suite("GeneralTests")(
//    test(""){
//      val jsonGraph = new JsonGraph {}
//      for{
//        interpreter <- ZIO.service[Interpreter]
//        delta <- ZIO.service[DeltaCalculatorService]
//        zio <- ZIO.service[PipelineUpdateService]
//        interpreted = interpreter.interpret(jsonGraph)
//        delta <- delta.calculateDelta(interpreted)
//      } yield assertTrue(true)
//    }
//  )
//}
