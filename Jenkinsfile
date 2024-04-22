pipeline {
    agent any
    environment {
        DOCKER_IMAGE = 'sherifs82/myapi:v1'
        DOCKER_REGISTRY = 'https://index.docker.io/v1/'
        DOCKER_REGISTRY_CREDENTIALS_ID = 'dockerHubCreds'
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
        stage('Build and Publish Docker Image') {
            steps {
                dockerBuildAndPublish {
                    registryUrl("${DOCKER_REGISTRY}")
                    registryCredentialsId("${DOCKER_REGISTRY_CREDENTIALS_ID}")
                    repositoryName("${DOCKER_IMAGE}")
                    tag("${env.BUILD_ID}")
                    dockerfile("Dockerfile")
                    buildContext(".")
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
