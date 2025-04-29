pipeline {
    agent any  // 使用任意可用节点
    tools {
        maven 'Maven 3.8.6'  // 对应 Jenkins 中配置的 Maven 名称
        jdk 'JDK 11'         // 对应 Jenkins 中配置的 JDK 名称
    }
    stages {
        stage('Checkout') {
            steps {
                git url: 'https://github.com/spring-projects/spring-petclinic.git', branch: 'main'
            }
        }
        stage('Build') {
            steps {
                sh 'mvn clean package -DskipTests'  // 跳过测试（仅演示打包）
            }
        }
        stage('Archive') {
            steps {
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            }
        }
    }
}
