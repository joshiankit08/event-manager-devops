pipeline {
    agent {
        docker { image 'node:7-alpine' }
    }
    stages {
        stage('Checkout'){
            steps {
                checkout([$class: 'GitSCM', branches: [[name: '*/develop']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[credentialsId: '2e56e149-1a09-4640-845b-0aeff4b48fc2', url: 'git@github.com:joshiankit08/event-manager-devops.git']]])
                
            }
        }


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