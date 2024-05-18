// I know this is crap. I have never written Kotlin before and just wanted something that did the job for the sake of the exercise. 
// This is not how I normally code, I promise.

@file:DependsOn("com.squareup.okhttp3:okhttp:4.11.0")
@file:DependsOn("org.json:json:20211205")

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import org.json.JSONObject
import java.io.File
import java.io.IOException

import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.PathMatcher
import java.nio.file.FileSystems


val client = OkHttpClient()

fun createRelease(client: OkHttpClient, token: String, owner: String, repo: String, tagName: String, name: String, body: String): Int {
    val url = "https://api.github.com/repos/$owner/$repo/releases"
    val json = JSONObject()
        .put("tag_name", tagName)
        .put("name", name)
        .put("body", body)

    val requestBody = RequestBody.create("application/json; charset=utf-8".toMediaType(), json.toString())
    val request = Request.Builder()
        .url(url)
        .header("Authorization", "token $token")
        .post(requestBody)
        .build()

    client.newCall(request).execute().use { response ->
        if (!response.isSuccessful) throw IOException("Unexpected code $response")
        val responseData = response.body?.string()
        val jsonResponse = JSONObject(responseData)
        return jsonResponse.getInt("id")
    }
}

fun uploadFileToRelease(client: OkHttpClient, token: String, owner: String, repo: String, releaseId: Int, filePath: String) {
    val url = "https://uploads.github.com/repos/$owner/$repo/releases/$releaseId/assets?name=${File(filePath).name}"
    val file = File(filePath)
    val requestBody = RequestBody.create(("application/zip").toMediaType(), file)

    val request = Request.Builder()
        .url(url)
        .header("Authorization", "token $token")
        .header("Content-Type", "application/zip")
        .post(requestBody)
        .build()

    client.newCall(request).execute().use { response ->
        if (!response.isSuccessful) throw IOException("Unexpected code $response")
        println("File uploaded successfully")
    }
}


fun getArg(args: Array<String>, argName: String): String {
    return if (args.contains("-$argName")) args[1 + args.indexOf("-$argName")]
        else ""
}

fun findFirstFileMatchingPattern(pattern: String): String? {
    val matcher: PathMatcher = FileSystems.getDefault().getPathMatcher("glob:$pattern")
    val dir = Paths.get("").toAbsolutePath()
    Files.walk(dir).use { stream ->
        return stream
            .filter { matcher.matches(it.fileName) }
            .findFirst()
            .map { it.toAbsolutePath().toString() }
            .orElse(null)
    }
}


val repoOwner = getArg(args,"repoOwner" )
val repoName = getArg(args,"repoName" )
val tagName = getArg(args,"tagName" )
val releaseName = getArg(args,"releaseName" )
val releaseDescription = getArg(args,"description" )
val filePath = getArg(args,"filePath" )
val githubToken = getArg(args,"token" )

val realFilePath = findFirstFileMatchingPattern(filePath)
if (realFilePath != null) {
    val releaseId = createRelease(client, githubToken, repoOwner, repoName, tagName, releaseName, releaseDescription)
    uploadFileToRelease(client, githubToken, repoOwner, repoName, releaseId, realFilePath!!)
}
else{
    throw IOException("No file found")
}