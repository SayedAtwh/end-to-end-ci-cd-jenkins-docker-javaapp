pipeline {
    agent {
        label 'slave1'
    }
    
    environment {
        DOCKER_IMAGE = 'islamic-app'
        DOCKER_TAG = 'latest'
        CONTAINER_NAME = 'islamic-quran-azkar-app'
        PORT = '10000'
        APP_PORT = '8080'
    }
    
    stages {
        stage('Build') {
            steps {
               script{
                 withCredentials([usernamePassword(credentialsId: 'jenkins-dockerhub', passwordVariable: 'Password', usernameVariable: 'Username')]) {
                 sh 'docker build -t islamic-app .'
                 sh 'docker tag islamic-app $Username/islamic-app:latest'
                 sh 'docker login -u $Username -p $Password'
                 sh 'docker push $Username/islamic-app:latest'
                }
               }
            }
        }
        stage('Deploy') {
            steps {
               script{
                   sh 'docker stop islamic-app-container || true'
                   sh 'docker rm islamic-app-container || true'
                   sh 'docker run -d --name islamic-app-container -p 10000:8080 islamic-app:latest'
                   sh 'docker-compose up -d || true'
                }
            }
        }
    }
    post {
        success {
            echo 'Pipeline completed successfully! '
        }
        failure {
            echo 'Pipeline failed!'
        }
    }
}
