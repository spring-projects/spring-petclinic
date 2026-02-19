import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.buildSteps.script

class StandardPipeline(
    private val prefix: String
) {

    val build = BuildType {
        id("${prefix}_Build")
        name = "$prefix :: Build"

        vcs {
            root(DslContext.settingsRoot)
        }

        steps {
            script {
                name = "Build"
                scriptContent = "./gradlew clean build"
            }
        }
    }

    val test = BuildType {
        id("${prefix}_Test")
        name = "$prefix :: Test"

        vcs {
            root(DslContext.settingsRoot)
        }

        steps {
            script {
                name = "Test"
                scriptContent = "./gradlew test"
            }
        }

        dependencies {
            snapshot(build) {}
        }
    }

    val deploy = BuildType {
        id("${prefix}_Deploy")
        name = "$prefix :: Deploy"
        type = BuildTypeSettings.Type.DEPLOYMENT

        vcs {
            root(DslContext.settingsRoot)
        }

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
