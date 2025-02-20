pipeline {
    agent any

    tools {
        jdk 'java21'
        maven 'maven'
    }

    environment {
        DOCKER_IMAGE = 'employee-manager-app'
        DOCKER_REGISTRY = 'ghcr.io/nikolanede'
        GIT_COMMIT_SHORT = sh(script: 'git rev-parse --short HEAD', returnStdout: true).trim()
    }

    stages {
        stage('Checkout') {
            steps {
                script {
                    echo 'Cloning repository...'
                    git 'https://github.com/your-repo.git'
                }
            }
        }

        stage('Build') {
            steps {
                script {
                    echo 'Building the application...'
                    sh 'mvn clean package -DskipTests=false'
                }
            }
        }

        stage('Code Quality Check') {
            steps {
                script {
                    echo 'Running static analysis...'
                    sh 'mvn verify' // Runs checks like Checkstyle, PMD, SpotBugs
                }
            }
        }

        stage('Test') {
            steps {
                script {
                    echo 'Running unit tests...'
                    sh 'mvn test'
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    echo "Building Docker image ${DOCKER_IMAGE}:${GIT_COMMIT_SHORT}"
                    docker.build("${DOCKER_REGISTRY}/${DOCKER_IMAGE}:${GIT_COMMIT_SHORT}")
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                script {
                    docker.withRegistry("https://${DOCKER_REGISTRY}", 'docker-credentials-id') {
                        docker.image("${DOCKER_REGISTRY}/${DOCKER_IMAGE}:${GIT_COMMIT_SHORT}").push()
                        docker.image("${DOCKER_REGISTRY}/${DOCKER_IMAGE}:${GIT_COMMIT_SHORT}").push("latest")
                    }
                }
            }
        }

        stage('Deploy') {
            steps {
                script {
                    echo 'Deploying the application...'
                    sh 'echo "Application deployed successfully!"' // Placeholder, replace with real deployment steps
                }
            }
        }
    }

    post {
        success {
            script {
                echo "🎉 SUCCESS: Build #${env.BUILD_NUMBER} completed successfully!"
                sh 'curl -X POST -H "Content-Type: application/json" -d \'{"text":"✅ Build SUCCESS: ${env.JOB_NAME} #${env.BUILD_NUMBER}"}\' https://your-slack-webhook-url'
            }
        }

        failure {
            script {
                echo "❌ FAILURE: Build #${env.BUILD_NUMBER} failed!"
                sh 'curl -X POST -H "Content-Type: application/json" -d \'{"text":"🚨 Build FAILED: ${env.JOB_NAME} #${env.BUILD_NUMBER}"}\' https://your-slack-webhook-url'
            }
        }

        always {
            script {
                echo "🔄 Build completed. Cleaning up workspace..."
                sh 'docker system prune -f'
            }
        }
    }
}
