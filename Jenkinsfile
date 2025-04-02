pipeline {
    agent any

    environment {
        IMAGE_NAME = "microservice-demo"
        DOCKER_HUB_USER = "federik1982"
    }



    stages {

        stage('Test Docker') {
            steps {
                script {
                    sh 'docker --version'
                }
            }
        }
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
                script {
                    // Usa Docker Pipeline per costruire l'immagine
                    docker.build("${DOCKER_HUB_USER}/${IMAGE_NAME}:latest")
                }
            }
        }

        stage('Push to Docker Hub') {
            steps {
                script {
                    // Effettua il login a Docker Hub e push dell'immagine
                    docker.withRegistry('', 'docker-hub-credentials') {
                        docker.image("${DOCKER_HUB_USER}/${IMAGE_NAME}:latest").push()
                    }
                }
            }
        }
    }
}
