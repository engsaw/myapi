pipeline {
    agent none // No global agent specified
    stages {
        stage('Build and Deploy') {
            steps {
                // Using the Kubernetes plugin's fluent API to configure the pod
                script {
                    kubernetes.pod('kubectl-pod').withPrivileged(true).withImage('lachlanevenson/k8s-kubectl').inside {
                        stage('Pre-Check') {
                            // Checking kubectl client version
                            sh 'kubectl version --client'
                        }
                        stage('Checkout') {
                            // Cloning the SCM
                            git 'https://github.com/your-repository-url/your-repo.git'
                        }
                        stage('Deploy to Kubernetes') {
                            // Applying Kubernetes deployment YAML
                            sh 'kubectl apply -f my-kubernetes-deployment.yaml'
                        }
                    }
                }
            }
        }
    }
}
