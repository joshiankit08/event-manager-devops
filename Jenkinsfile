pipeline {
  agent any

    stages {
        stage('Build&Test') {
            steps {
                sh "./mvnw clean install -DskipTests"
            }
        }
        stage('Push') {
            steps {
                sh "docker login -u fcastells -p Timao71910"
                sh "docker build -t event-manager:latest ."
                sh "docker push event-manager:latest"
            }
        }
        stage('Deploy') {
                    steps {
                        sh "docker run -d --name event-manager -p 8080:8080 event-manager:latest"
                    }
                }
    }
}
