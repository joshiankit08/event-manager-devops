pipeline {
    agent {
        docker { image 'njhipster/jhipster:v6.0.1'
        args '-u jhipster -e MAVEN_OPTS="-Duser.home=./"' }
    }
    stages {
        stage('Checkout'){
            steps {
                checkout([$class: 'GitSCM', branches: [[name: '*/develop']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[credentialsId: '2e56e149-1a09-4640-845b-0aeff4b48fc2', url: 'git@github.com:joshiankit08/event-manager-devops.git']]])

            }
        }
         stage('check java') {
             steps{
                sh "java -version"
             }
            
        }
        stage('clean') {
            steps{
            sh "chmod +x mvnw"
            sh "./mvnw clean"
            }
        }
        stage('Install') {
            steps {
                sh 'npm install'
            }
        }

        stage('Test') {
            steps {
                sh 'npm test'
            }
        }

        stage('Code Quality') {
            steps {
                sh './mvnw -Pprod clean verify sonar:sonar'
            }
        }

        
    }
}