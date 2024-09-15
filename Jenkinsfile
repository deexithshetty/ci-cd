pipeline {
    agent any
    
    tools {
        maven 'maven3'
    }

    stages {
        stage('Checkoutcode') {
            steps {
                git branch: 'development', credentialsId: 'git-creds', url: 'https://github.com/deexithshetty/maven-web-application.git'
            }
        }
        stage('Complie') {
            steps {
                sh "mvn compile"
            }
        }
        stage('Test') {
            steps {
                sh "mvn test"
            }
        }        
        stage('File scan') {
            steps {
                    sh "trivy fs --format table -o trivy-fs-report.html ."
            }
        }
        stage('SonarQube report') {
            steps {
                sh "mvn sonar:sonar"
            }
        }
        stage('Build stage') {
            steps {
                sh "mvn package"
            }
        } 
        stage('Artifacts to Nexus') {
            steps {
                sh "mvn deploy"
            }
        } 
        stage('Build and tag docker image') {
            steps {
                withDockerRegistry(credentialsId: 'dockerhub-cred') {
                sh "docker build -t dockerdeexith/myapp:latest ."
             }
            }
        }
        stage('Scan docker image') {
            steps {
                sh "trivy image --format table -o trivy-image-report.html dockerdeexith/myapp:latest "
            }
        }
        stage('Push docker images') {
            steps {
                sh "docker push dockerdeexith/myapp:latest"
            }
        }
    }
}
