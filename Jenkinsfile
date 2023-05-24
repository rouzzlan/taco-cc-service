pipeline {
    agent any
    tools {
        jdk 'graalvm-ee-java17-22.3.1'
    }
    environment {
        DOCKER_TAG = 'harbour.739.net/taco-cloud/cc-service-service:0.0.3'
    }
    stages {
        stage('Setup') {
            steps {
                sh '''
                    ./gradlew clean buildDependents
                '''
            }
        }
        stage('Build') {
            steps {
                sh '''
                    ./gradlew build
                '''
            }
            post {
                always {
                    archiveArtifacts 'build/*'
                }
            }

        }
        stage('Docker Build') {
            when {
            expression {
                env.BRANCH_NAME == 'master'
              }
            }
            steps {
                sh '''
                    docker build -f src/main/docker/Dockerfile.jvm -t ${DOCKER_TAG} .
                    docker login harbour.739.net -u="rouslan" -p="50m9FiD3"
                    docker push ${DOCKER_TAG}
                '''
            }
        }
    }
}