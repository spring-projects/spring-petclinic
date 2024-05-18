import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.buildFeatures.PullRequests
import jetbrains.buildServer.configs.kotlin.buildFeatures.commitStatusPublisher
import jetbrains.buildServer.configs.kotlin.buildFeatures.perfmon
import jetbrains.buildServer.configs.kotlin.buildFeatures.pullRequests
import jetbrains.buildServer.configs.kotlin.buildSteps.kotlinScript
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
        password("github_key", "credentialsJSON:405d3744-add6-49d5-a271-6dfd185492c2")
    }
}

object Build : BuildType({
    name = "Build"

    artifactRules = "+:target/*.jar"
    publishArtifacts = PublishMode.SUCCESSFUL

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

    steps {
        kotlinScript {
            id = "kotlinScript"
            content = """
                @file:DependsOn("com.squareup.okhttp3:okhttp:4.11.0")
                @file:DependsOn("org.json:json:20211205")
                
                import okhttp3.*
                import okhttp3.MediaType.Companion.toMediaType
                import org.json.JSONObject
                import java.io.File
                import java.io.IOException
                
                
                
                val client = OkHttpClient()
                
                fun createRelease(client: OkHttpClient, token: String, owner: String, repo: String, tagName: String, name: String, body: String): Int {
                    val url = "https://api.github.com/repos/${'$'}owner/${'$'}repo/releases"
                    val json = JSONObject()
                        .put("tag_name", tagName)
                        .put("name", name)
                        .put("body", body)
                
                    val requestBody = RequestBody.create("application/json; charset=utf-8".toMediaType(), json.toString())
                    val request = Request.Builder()
                        .url(url)
                        .header("Authorization", "token ${'$'}token")
                        .post(requestBody)
                        .build()
                
                    client.newCall(request).execute().use { response ->
                        if (!response.isSuccessful) throw IOException("Unexpected code ${'$'}response")
                        val responseData = response.body?.string()
                        val jsonResponse = JSONObject(responseData)
                        return jsonResponse.getInt("id")
                    }
                }
                
                fun uploadFileToRelease(client: OkHttpClient, token: String, owner: String, repo: String, releaseId: Int, filePath: String) {
                    val url = "https://uploads.github.com/repos/${'$'}owner/${'$'}repo/releases/${'$'}releaseId/assets?name=${'$'}{File(filePath).name}"
                    val file = File(filePath)
                    val requestBody = RequestBody.create(("application/zip").toMediaType(), file)
                
                    val request = Request.Builder()
                        .url(url)
                        .header("Authorization", "token ${'$'}token")
                        .header("Content-Type", "application/zip")
                        .post(requestBody)
                        .build()
                
                    client.newCall(request).execute().use { response ->
                        if (!response.isSuccessful) throw IOException("Unexpected code ${'$'}response")
                        println("File uploaded successfully")
                    }
                }
                
                
                fun getArg(args: Array<String>, argName: String): String {
                    return if (args.contains("-${'$'}argName")) args[1 + args.indexOf("-${'$'}argName")]
                        else ""
                }
                
                val repoOwner = getArg(args,"repoOwner" )
                val repoName = getArg(args,"repoName" )
                val tagName = getArg(args,"tagName" )
                val releaseName = getArg(args,"releaseName" )
                val releaseDescription = getArg(args,"description" )
                val filePath = getArg(args,"filePath" )
                val githubToken = getArg(args,"token" )
                
                val releaseId = createRelease(client, githubToken, repoOwner, repoName, tagName, releaseName, releaseDescription)
                //uploadFileToRelease(client, githubToken, repoOwner, repoName, releaseId, filePath)
            """.trimIndent()
            arguments = """-repoOwner "TPG-Teklada" -repoName "spring-petclinic" -tagName "v1.0.0" -releaseName "Release 1.0.0" -description "Description of the release" -filePath "*.jar" -token "%github_key%""""
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
