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
                        mkdir -p ~/bin
                        mv ./kubectl ~/bin/
                        '''
                    }
                }
            }
        }
        stage('Deploy to Kubernetes') {
            steps {
                script {
                    // Configure kubectl with the Jenkins service account credentials
                    withKubeConfig(credentialsId: 'jenkins-k8s-sa', serverUrl: 'https://kubernetes.default.svc') {
                        // Use kubectl to apply the YAML file
                        sh '~/bin/kubectl apply -f myapi-kubernetes.yaml'
                    }
                }
            }
        }
    }
}
