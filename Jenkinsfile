pipeline {
    agent any
    stages {
        stage('Setup kubectl') {
            steps {
                script {
                    // Check if kubectl is present and install if not
                    if (sh(script: 'which kubectl', returnStatus: true) != 0) {
                        sh '''
                        curl -LO "https://dl.k8s.io/release/$(curl -L -s https://dl.k8s.io/release/stable.txt)/bin/linux/amd64/kubectl"
                        chmod +x ./kubectl
                        mv ./kubectl /usr/local/bin/kubectl
                        '''
                    }
                }
            }
        }
        stage('Deploy to Kubernetes') {
            steps {
                withKubeConfig(credentialsId: 'jenkins-k8s-sa', serverUrl: 'https://kubernetes.default.svc') {
                    sh 'kubectl apply -f myapi-kubernetes.yaml'
                }
            }
        }
    }
}
