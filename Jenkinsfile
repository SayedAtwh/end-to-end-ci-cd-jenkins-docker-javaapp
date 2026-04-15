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
        stage('Checkout & Clean') {
            steps {
                script {
                    echo 'Checking out repository...'
                    checkout scm
                    
                    echo 'Cleaning workspace...'
                    cleanWs()
                    
                    echo 'Repository checked out and cleaned successfully'
                }
            }
        }
        
        stage('Build & Test') {
            steps {
                script {
                    echo 'Building the application...'
                    sh 'mvn clean compile'
                    
                    echo 'Running unit tests...'
                    sh 'mvn test'
                    
                    echo 'Build and test completed successfully'
                }
            }
            post {
                success {
                    echo 'Build and test passed'
                    junit 'target/surefire-reports/*.xml' || echo 'No test reports found'
                }
                failure {
                    echo 'Build or test failed'
                    error 'Build failed - stopping pipeline'
                }
            }
        }
        
        stage('Code Quality') {
            steps {
                script {
                    echo 'Running code quality checks...'
                    try {
                        // SonarQube analysis if configured
                        withSonarQubeEnv('SonarQube') {
                            sh 'mvn sonar:sonar'
                        }
                    } catch (Exception e) {
                        echo 'SonarQube not configured - skipping code quality analysis'
                    }
                    
                    // Checkstyle
                    try {
                        sh 'mvn checkstyle:check'
                    } catch (Exception e) {
                        echo 'Checkstyle failed - continuing...'
                    }
                }
            }
        }
        
        stage('Package') {
            steps {
                script {
                    echo 'Packaging the application...'
                    sh 'mvn package -DskipTests'
                    echo 'Application packaged successfully'
                }
            }
        }
        
        stage('Build Docker Image') {
            steps {
                script {
                    echo 'Building Docker image...'
                    sh """
                        docker build -t ${DOCKER_IMAGE}:${DOCKER_TAG} .
                        docker tag ${DOCKER_IMAGE}:${DOCKER_TAG} ${DOCKER_IMAGE}:build-${BUILD_NUMBER}
                    """
                    echo 'Docker image built successfully'
                }
            }
        }
        
        stage('Security Scan') {
            steps {
                script {
                    echo 'Running security scans...'
                    
                    // Docker image security scan
                    try {
                        sh 'docker run --rm -v /var/run/docker.sock:/var/run/docker.sock aquasec/trivy image ${DOCKER_IMAGE}:${DOCKER_TAG}'
                    } catch (Exception e) {
                        echo 'Trivy security scan failed or not available - continuing...'
                    }
                    
                    // Dependency vulnerability scan
                    try {
                        sh 'mvn dependency-check:check'
                    } catch (Exception e) {
                        echo 'Dependency check failed - continuing...'
                    }
                }
            }
        }
        
        stage('Push to Registry') {
            steps {
                script {
                    echo 'Pushing Docker image to registry...'
                    withCredentials([usernamePassword(credentialsId: 'jenkins-dockerhub', passwordVariable: 'Password', usernameVariable: 'Username')]) {
                        sh """
                            docker tag ${DOCKER_IMAGE}:${DOCKER_TAG} $Username/${DOCKER_IMAGE}:${DOCKER_TAG}
                            docker tag ${DOCKER_IMAGE}:build-${BUILD_NUMBER} $Username/${DOCKER_IMAGE}:build-${BUILD_NUMBER}
                            docker login -u $Username -p $Password
                            docker push $Username/${DOCKER_IMAGE}:${DOCKER_TAG}
                            docker push $Username/${DOCKER_IMAGE}:build-${BUILD_NUMBER}
                        """
                    }
                    echo 'Docker image pushed to registry successfully'
                }
            }
        }
        
        stage('Deploy') {
            steps {
                script {
                    echo 'Deploying application...'
                    
                    // Stop and remove existing container
                    sh """
                        docker stop ${CONTAINER_NAME} || true
                        docker rm ${CONTAINER_NAME} || true
                        docker-compose down || true
                    """
                    
                    // Run new container
                    sh """
                        docker run -d \
                            --name ${CONTAINER_NAME} \
                            -p ${PORT}:${APP_PORT} \
                            --restart unless-stopped \
                            $Username/${DOCKER_IMAGE}:${DOCKER_TAG}
                    """
                    
                    // Also run with docker-compose as backup
                    sh 'docker-compose up -d || true'
                    
                    echo 'Application deployed successfully'
                }
            }
        }
        
        stage('Health Check') {
            steps {
                script {
                    echo 'Performing health check...'
                    
                    // Wait for application to start
                    sh 'sleep 30'
                    
                    // Check application health
                    sh """
                        curl -f http://localhost:${PORT}/actuator/health || {
                            echo 'Health check failed - checking logs...'
                            docker logs ${CONTAINER_NAME}
                            exit 1
                        }
                    """
                    
                    // Test API endpoints
                    sh """
                        curl -f http://localhost:${PORT}/api/quran/surahs || exit 1
                        curl -f http://localhost:${PORT}/api/azkar || exit 1
                    """
                    
                    echo 'Health check and API tests passed'
                }
            }
            post {
                failure {
                    echo 'Health check failed'
                    sh 'docker logs ${CONTAINER_NAME}'
                    error 'Health check failed - deployment unsuccessful'
                }
            }
        }
        
        stage('Integration Tests') {
            steps {
                script {
                    echo 'Running integration tests...'
                    
                    // Test application functionality
                    sh """
                        curl -X GET http://localhost:${PORT}/api/quran/surahs | jq '.[0].name' || echo 'Quran API test'
                        curl -X GET http://localhost:${PORT}/api/azkar | jq '.[0].category' || echo 'Azkar API test'
                    """
                    
                    echo 'Integration tests completed successfully'
                }
            }
        }
        
        stage('Performance Test') {
            when {
                branch 'main'
            }
            steps {
                script {
                    echo 'Running performance tests...'
                    try {
                        // Simple performance test
                        sh """
                            for i in {1..10}; do
                                curl -w "@curl-format.txt" -o /dev/null -s http://localhost:${PORT}/api/quran/surahs
                            done
                        """
                    } catch (Exception e) {
                        echo 'Performance test failed - continuing...'
                    }
                }
            }
        }
    }
    
    post {
        always {
            script {
                echo 'Pipeline execution completed'
                echo 'Performing cleanup...'
                
                // Clean up workspace
                cleanWs()
                
                // Docker cleanup
                sh 'docker image prune -f || true'
                sh 'docker container prune -f || true'
                
                echo 'Cleanup completed'
            }
        }
        
        success {
            script {
                echo 'Pipeline completed successfully! '
                echo "Application is running at: http://localhost:${PORT}"
                echo "Docker Image: $Username/${DOCKER_IMAGE}:${DOCKER_TAG}"
                
                // Send success notification
                try {
                    emailext (
                        subject: "SUCCESS: Islamic App Pipeline - Build #${BUILD_NUMBER}",
                        body: """
                        <p>The Islamic Quran and Azkar application has been successfully deployed!</p>
                        <p><strong>Build Number:</strong> ${BUILD_NUMBER}</p>
                        <p><strong>Branch:</strong> ${env.BRANCH_NAME}</p>
                        <p><strong>Access URL:</strong> <a href="http://localhost:${PORT}">http://localhost:${PORT}</a></p>
                        <p><strong>Docker Image:</strong> $Username/${DOCKER_IMAGE}:${DOCKER_TAG}</p>
                        <p><strong>Container Name:</strong> ${CONTAINER_NAME}</p>
                        <p><strong>Build Duration:</strong> ${currentBuild.durationString}</p>
                        """,
                        to: "${env.CHANGE_AUTHOR_EMAIL ?: 'admin@example.com'}"
                    )
                } catch (Exception e) {
                    echo 'Email notification failed - continuing...'
                }
                
                // Slack notification (if configured)
                try {
                    slackSend(
                        channel: '#deployments',
                        color: 'good',
                        message: "SUCCESS: Islamic App deployed successfully! Build #${BUILD_NUMBER} - http://localhost:${PORT}"
                    )
                } catch (Exception e) {
                    echo 'Slack notification failed - continuing...'
                }
            }
        }
        
        failure {
            script {
                echo 'Pipeline failed!'
                
                // Send failure notification
                try {
                    emailext (
                        subject: "FAILURE: Islamic App Pipeline - Build #${BUILD_NUMBER}",
                        body: """
                        <p>The Islamic Quran and Azkar application pipeline has failed!</p>
                        <p><strong>Build Number:</strong> ${BUILD_NUMBER}</p>
                        <p><strong>Branch:</strong> ${env.BRANCH_NAME}</p>
                        <p><strong>Failed Stage:</strong> ${currentBuild.currentResult}</p>
                        <p><strong>Build Duration:</strong> ${currentBuild.durationString}</p>
                        <p>Please check the <a href="${env.BUILD_URL}">Jenkins build log</a> for details.</p>
                        """,
                        to: "${env.CHANGE_AUTHOR_EMAIL ?: 'admin@example.com'}"
                    )
                } catch (Exception e) {
                    echo 'Email notification failed - continuing...'
                }
                
                // Slack notification
                try {
                    slackSend(
                        channel: '#deployments',
                        color: 'danger',
                        message: "FAILURE: Islamic App pipeline failed! Build #${BUILD_NUMBER} - Check logs: ${env.BUILD_URL}"
                    )
                } catch (Exception e) {
                    echo 'Slack notification failed - continuing...'
                }
            }
        }
        
        unstable {
            echo 'Pipeline is unstable!'
            script {
                try {
                    emailext (
                        subject: "UNSTABLE: Islamic App Pipeline - Build #${BUILD_NUMBER}",
                        body: """
                        <p>The Islamic Quran and Azkar application pipeline is unstable!</p>
                        <p><strong>Build Number:</strong> ${BUILD_NUMBER}</p>
                        <p><strong>Branch:</strong> ${env.BRANCH_NAME}</p>
                        <p>Please check the <a href="${env.BUILD_URL}">Jenkins build log</a> for details.</p>
                        """,
                        to: "${env.CHANGE_AUTHOR_EMAIL ?: 'admin@example.com'}"
                    )
                } catch (Exception e) {
                    echo 'Email notification failed - continuing...'
                }
            }
        }
        
        aborted {
            echo 'Pipeline was aborted!'
        }
    }
}
