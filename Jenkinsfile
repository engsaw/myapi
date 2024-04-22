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
            steps {
                script {
                    // Use credentials to log in to Docker
                    withCredentials([usernamePassword(credentialsId: 'dockerHubCreds', passwordVariable: 'DOCKER_PASS', usernameVariable: 'DOCKER_USER')]) {
                        sh 'echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin'
                        sh 'docker build -t myrepo/myapp:$BUILD_ID .'
                        sh 'docker push myrepo/myapp:$BUILD_ID'
                    }
                }
            }
        stage('Deploy to Kubernetes') {
            steps {
                script {
                    // Configure kubectl with the Kubernetes API server URL and Jenkins service account credentials
                    withKubeConfig(credentialsId: 'jenkins-k8s-sa', serverUrl: 'https://917f0e0a-ab07-4b94-a298-c72a87ee6446.k8s.ondigitalocean.com') {
                        // Use kubectl to apply the YAML file with validation disabled
                        sh '~/bin/kubectl apply -f myapi-kubernetes.yaml --validate=false'
                    }
                }
            }
        }
    }
}
