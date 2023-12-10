pipeline {
    agent any
    environment {
        MAVEN_HOME = tool 'maven-3.9.5'
        DOCKER_IMAGE_NAME = 'tugceerkaner/simple-java-maven-app'
        REMOTE_USER = 'root'
        REMOTE_HOST = '20.104.209.198'
        TEST_REPO_URL = 'https://github.com/tugcedemiray/simple-java-maven-app-test.git'
    }
    stages {
        stage('Maven Build & Unit Test') {
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
                sh "docker build -t ${env.DOCKER_IMAGE_NAME}:v${BUILD_NUMBER} ."
            }
        }
        stage('Push Docker Image to Dockerhub') {
            steps {
                withCredentials([usernameColonPassword(credentialsId: 'docker', variable: 'DOCKER_PASS')]) {
                    sh "docker push ${env.DOCKER_IMAGE_NAME}:v${BUILD_NUMBER}"
                }
            }
        }
        stage('Deploy Application') {
            steps {
                sh "docker stop hello_world_app | true"
                sh "docker rm hello_world_app | true"
                sh "docker run --name hello_world_app -d -p 8050:8080 ${env.DOCKER_IMAGE_NAME}:v${BUILD_NUMBER}"
            }
        }
        stage('Clone Test Repository') {
            steps {
                git url: "${env.TEST_REPO_URL}"
            }
        }
        stage('Test') {
            steps {
                script {
                    sh 'javac -cp lib/* CheckIPAccessibility.java'
                    sh 'java -cp .:lib/* CheckIPAccessibility'
                }
            }
        }
        stage('Docker Pull on Second VM') {
            steps {
                script {
                    sh "ssh ${env.REMOTE_USER}@${env.REMOTE_HOST} 'docker pull ${env.DOCKER_IMAGE_NAME}:v${BUILD_NUMBER}'"
                }
            }
        }
        stage('Docker Run on Second VM') {
            steps {
                script {
                    sh "ssh ${REMOTE_USER}@${REMOTE_HOST} 'docker stop hello_world_app || true && docker rm hello_world_app || true && docker run --name hello_world_app -d -p 8050:8080 ${DOCKER_IMAGE_NAME}:v${BUILD_NUMBER}'"
                }
            }
        }
    }
}