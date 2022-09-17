node("JDK-11-MVN") {
  stage("get-code") {
    git url: "https://github.com/spring-projects/spring-petclinic.git", branch: "main"
  }
  stage("build") {
    sh "mvn package"
  }
}
