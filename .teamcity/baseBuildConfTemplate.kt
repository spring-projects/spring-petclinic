import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.buildSteps.script

object CommonBuildTemplate : Template({
    name = "Common Build Template"

    vcs {
        root(DslContext.settingsRoot)
    }

    params {
        param("env.PROJECT_NAME", "")
    }

    steps {
        script {
            name = "Gradle Build"
            scriptContent = "./gradlew clean build"
        }
    }
})
