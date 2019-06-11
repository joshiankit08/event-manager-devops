pipeline {
    agent {
        docker { image 'node:7-alpine' }
    }
    stages {
        stage('Test') {
            steps {
                sh 'node test'
            }
        }

        stage('Code Quality') {
            steps {
                sh './mvnw -Pprod clean verify sonar:sonar'
            }
        }

        stage('Install') {
            steps {
                sh 'node install'
            }
        }
    }
}