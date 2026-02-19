import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.buildFeatures.perfmon
import StandardPipeline

version = "2025.11"

project {

    val pipeline = StandardPipeline(
        projectId = "MyService",
        repoUrl = "git@gitlab.com:company/my-service.git"
    )

    buildType(pipeline.build)
    buildType(pipeline.test)
    buildType(pipeline.deploy)
}
