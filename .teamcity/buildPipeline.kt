import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.buildSteps.script
import jetbrains.buildServer.configs.kotlin.vcs.*


class StandardPipeline(
    projectId: String,
    repoUrl: String
) {

    val vcsRoot = GitVcsRoot {
        id("${projectId}_Vcs")
        name = "$projectId VCS"
        url = repoUrl
        branch = "refs/heads/main"
    }

    val build = BuildType {
        id("${projectId}_Build")
        name = "Build"
        templates(CommonBuildTemplate)

        vcs {
            root(vcsRoot)
        }
    }

    val test = BuildType {
        id("${projectId}_Test")
        name = "Test"

        steps {
            script {
                name = "Run Tests"
                scriptContent = "./gradlew test"
            }
        }

        dependencies {
            snapshot(build) {}
        }
    }

    val deploy = BuildType {
        id("${projectId}_Deploy")
        name = "Deploy"

        steps {
            script {
                name = "Deploy"
                scriptContent = "./gradlew publish"
            }
        }

        dependencies {
            snapshot(test) {}
        }
    }
}
