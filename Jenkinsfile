pipeline {
    agent any
    environment {
        MAVEN_HOME = tool 'maven-3.9.5'
    }
    stages {
        stage('Maven Build & Unit Test') {
            steps {
                sh "${MAVEN_HOME}/bin/mvn clean install"
            }
        }
    }
}