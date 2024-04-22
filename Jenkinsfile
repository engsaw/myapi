pipeline {
    agent any
    environment {
        DOCKER_IMAGE = 'myrepo/myapp'
    }
    stages {
        stage('Setup kubectl') {
            steps {
                script {
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
        stage('Build and Push Docker Image') {
            steps {
                script {
                    docker.withRegistry('https://index.docker.io/v1/', 'dockerHubCreds') {
                        docker.build("${env.DOCKER_IMAGE}:${env.BUILD_ID}").push()
                    }
                }
            }
        }
        stage('Deploy to Kubernetes') {
            steps {
                script {
                    withKubeConfig(credentialsId: 'jenkins-k8s-sa', serverUrl: 'https://917f0e0a-ab07-4b94-a298-c72a87ee6446.k8s.ondigitalocean.com') {
                        sh '~/bin/kubectl apply -f myapi-kubernetes.yaml --validate=false'
                    }
                }
            }
        }
    }
}
