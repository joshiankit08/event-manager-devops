pipeline {
  agent any

    stages {
        stage('Build&Test') {
            steps {
                sh "chmod 777 mvnw; ./mvnw clean install -DskipTests"
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
                        sh "ssh -i labkey ubuntu@34.218.255.158 docker run -d --name event-manager -p 8080:8080 event-manager:latest"
                    }
                }
    }
}
