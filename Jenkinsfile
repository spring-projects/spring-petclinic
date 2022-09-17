node("JDK-11-MVN") {
  stage("get-code") {
    git "https://github.com/spring-projects/spring-petclinic.git"
  }
  stage("build") {
    sh "mvn package"
  }
}
