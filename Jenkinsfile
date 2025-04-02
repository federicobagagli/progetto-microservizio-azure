pipeline {
    agent any

    environment {
        IMAGE_NAME = "microservice-demo"
        DOCKER_HUB_USER = "federik1982"
    }

    stages {
        stage('Checkout') {
            steps {
                git credentialsId: 'github-ssh-key', url: 'https://github.com/federicobagagli/microservice-demo.git', branch: 'master'
            }
        }

        stage('Build') {
            steps {
                script {
                    // Usa un'immagine Docker Maven ufficiale per eseguire il build
                    docker.image('maven:3.8.1-jdk-11').inside {
                        sh 'mvn clean package'
                    }
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                sh "docker build -t ${DOCKER_HUB_USER}/${IMAGE_NAME}:latest ."
            }
        }

        stage('Push to Docker Hub') {
            steps {
                withDockerRegistry([credentialsId: 'docker-hub-credentials', url: '']) {
                    sh "docker push ${DOCKER_HUB_USER}/${IMAGE_NAME}:latest"
                }
            }
        }
    }
}
