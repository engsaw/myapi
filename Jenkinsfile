pipeline {
    agent any
    stages {
        stage('Deploy to Kubernetes') {
            steps {
                withKubeConfig(credentialsId: 'jenkins-k8s-sa', serverUrl: 'https://kubernetes.default.svc') {
                    sh 'kubectl apply -f my-kubernetes-directory/'
                }
            }
        }
    }
}
