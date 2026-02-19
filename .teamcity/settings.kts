import jetbrains.buildServer.configs.kotlin.*

version = "2025.11"

project {

    val pipeline = StandardPipeline("MyService")

    buildType(pipeline.build)
    buildType(pipeline.test)
    buildType(pipeline.deploy)
}

