pipeline {
    agent any
    environment {
        // Assuming kubectl is installed in /usr/local/bin
        PATH = "${env.PATH}:/usr/local/bin"
    }
    stages {
        stage('Pre-Check') {
            steps {
                script {
                    sh 'kubectl version --client'
                }
            }
        }
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        stage('Deploy to Kubernetes') {
            steps {
                script {
                    sh 'kubectl apply -f my-kubernetes-deployment.yaml'
                }
            }
        }
    }
}
