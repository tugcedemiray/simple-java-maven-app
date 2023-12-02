pipeline {
    agent any

    environment {
        MAVEN_HOME = tool 'maven-3.9.5'
    }

    stages {
        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('sonar-server') {
                    sh '${MAVEN_HOME}/bin/mvn clean verify sonar:sonar ' +
                       '-Dsonar.projectKey=simple-java-maven-app ' +
                       '-Dsonar.projectName=Maven_App ' +
                       '-Dsonar.sources=src/main ' +
                       '-Dsonar.sourceEncoding=UTF-8 ' +
                       '-Dsonar.language=java ' +
                       '-Dsonar.java.binaries=target/classes ' +
                       '-Dsonar.coverage.jacoco.xmlReportPaths=target/jacoco.xml'
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
    }
}