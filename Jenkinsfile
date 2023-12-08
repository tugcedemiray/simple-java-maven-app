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
                       '-Dsonar.projectName=Java_Maven_App ' +
                       '-Dsonar.coverage.jacoco.xmlReportPaths=**/jacoco.xml'
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