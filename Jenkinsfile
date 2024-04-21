pipeline {
    agent any
    stages {
        stage('Checkout') {
            steps {
                // Checkout from SCM; this step is often implicit if Pipeline from SCM is configured
                checkout scm
            }
        }
        stage('Deploy to Kubernetes') {
            steps {
                script {
                    // Using the default service account to apply Kubernetes configurations
                    sh 'kubectl apply -f my-kubernetes-deployment.yaml'
                }
            }
        }
    }
}
