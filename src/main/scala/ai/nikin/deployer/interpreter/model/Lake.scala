package ai.nikin.deployer.interpreter.model


case class Lake(name: String) extends Definition
case class SparkApplication(
                           name: String,
                           inputLake: String,
                           outputLake: String,

                           )