import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.buildFeatures.PullRequests
import jetbrains.buildServer.configs.kotlin.buildFeatures.commitStatusPublisher
import jetbrains.buildServer.configs.kotlin.buildFeatures.notifications
import jetbrains.buildServer.configs.kotlin.buildFeatures.perfmon
import jetbrains.buildServer.configs.kotlin.buildFeatures.pullRequests
import jetbrains.buildServer.configs.kotlin.buildSteps.kotlinFile
import jetbrains.buildServer.configs.kotlin.buildSteps.maven
import jetbrains.buildServer.configs.kotlin.triggers.vcs

/*
The settings script is an entry point for defining a TeamCity
project hierarchy. The script should contain a single call to the
project() function with a Project instance or an init function as
an argument.

VcsRoots, BuildTypes, Templates, and subprojects can be
registered inside the project using the vcsRoot(), buildType(),
template(), and subProject() methods respectively.

To debug settings scripts in command-line, run the

    mvnDebug org.jetbrains.teamcity:teamcity-configs-maven-plugin:generate

command and attach your debugger to the port 8000.

To debug in IntelliJ Idea, open the 'Maven Projects' tool window (View
-> Tool Windows -> Maven Projects), find the generate task node
(Plugins -> teamcity-configs -> teamcity-configs:generate), the
'Debug' option is available in the context menu for the task.
*/

version = "2024.03"

project {

    buildType(Build)
    buildType(Deploy)

    params {
        text("notified_persons", """
            tommaso@teklada.com
            tpgionfriddo@gmail.com
        """.trimIndent(),
              regex = """^((([\w-\.]+@([\w-]+\.)+[\w-]{2,4})) *(\r\n|\r|\n) *)*(([\w-\.]+@([\w-]+\.)+[\w-]{2,4}))${'$'}""")
        param("repo_name", "spring-petclinic")
        password("github_key", "credentialsJSON:405d3744-add6-49d5-a271-6dfd185492c2")
        param("repo_owner", "TPG-Teklada")
    }
}

object Build : BuildType({
    name = "Build"

    artifactRules = "+:target/*.jar"
    publishArtifacts = PublishMode.SUCCESSFUL

    params {
        param("maven.project.artifactId", "%repo_name%")
        param("maven.project.version", "1")
    }

    vcs {
        root(DslContext.settingsRoot)
    }

    steps {
        maven {
            name = "build"
            id = "build"
            goals = "package"
        }
    }

    triggers {
        vcs {
            branchFilter = "+:pull/*"
        }
    }

    features {
        perfmon {
        }
        pullRequests {
            provider = github {
                authType = vcsRoot()
                filterAuthorRole = PullRequests.GitHubRoleFilter.MEMBER_OR_COLLABORATOR
            }
        }
        commitStatusPublisher {
            publisher = github {
                githubUrl = "https://api.github.com"
                authType = vcsRoot()
            }
        }
    }
})

object Deploy : BuildType({
    name = "Deploy"

    vcs {
        root(DslContext.settingsRoot)
    }

    steps {
        kotlinFile {
            id = "kotlinScript"
            path = "github_release.main.kts"
            arguments = """-repoOwner "%repo_owner%" -repoName "%repo_name%" -tagName "v${Build.depParamRefs["maven.project.version"]}" -releaseName "Release ${Build.depParamRefs["maven.project.version"]}" -description "Description of the release" -filePath "${Build.depParamRefs["maven.project.artifactId"]}-${Build.depParamRefs["maven.project.version"]}.jar" -token "%github_key%""""
        }
    }

    triggers {
        vcs {
            branchFilter = ""
        }
    }

    features {
        notifications {
            notifierSettings = emailNotifier {
                email = "%notified_persons%"
            }
            buildFinishedSuccessfully = true
        }
    }

    dependencies {
        dependency(Build) {
            snapshot {
                onDependencyFailure = FailureAction.FAIL_TO_START
            }

            artifacts {
                artifactRules = "+:*.jar"
            }
        }
    }
})
