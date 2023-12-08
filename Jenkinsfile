pipeline {
    agent any

    environment {
        MAVEN_HOME = tool 'maven-3.9.5'
        DockerRepository = 'tugceerkaner/simple-java-maven-app'
    }

    stages {
        stage('Build') {
            steps {
                sh "${MAVEN_HOME}/bin/mvn clean install"
            }
        }
        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('sonar-server') {
                    sh "${MAVEN_HOME}/bin/mvn sonar:sonar " +
                       "-Dsonar.projectKey=simple-java-maven-app " +
                       "-Dsonar.projectName=Java_Maven_App " +
                       "-Dsonar.coverage.jacoco.xmlReportPaths=**/jacoco.xml"
                }
            }
        }
        stage('Quality Gate Check') {
            steps {
                script {
                    def qg = waitForQualityGate()
                    if (qg.status != 'OK') {
                        error "Pipeline aborted due to quality gate failure: ${qg.status}"
                    }
                }
            }
        }
        stage('Build Docker Image') {
            steps {
                sh "docker build -t ${env.DockerRepository}:v${BUILD_NUMBER} ."
            }
        }
        stage('Push Docker Image to Dockerhub') {
            steps{
                withCredentials([usernameColonPassword(credentialsId: 'docker', variable: 'DOCKER_PASS')]) {
                    sh "docker push ${env.DockerRepository}:v${BUILD_NUMBER}"
                }
            }
        }
        stage('Deploy Application') {
            steps {
                sh "docker stop hello_world | true"
                sh "docker rm hello_world | true"
                sh "docker run --name hello_world -d -p 8050:8080 ${env.DockerRepository}:v${BUILD_NUMBER}"
            }
        }
    }
}