pipeline {
    agent any
    environment {
        DOCKER_COMPOSE_PATH = "/var/lib/jenkins/workspace/604ound/docker-compose.yml"
        BACKEND_JAR_PATH = "build/libs/Classik_Backend-0.0.1-SNAPSHOT.jar"
    }
    stages {
        stage('Clone Repository') {
            steps {
                // GitLab에서 소스 코드 가져오기
                checkout scm
            }
        }
        stage('Build Spring Boot') {
            steps {
                dir('Classik_Backend') {
                    // Gradle로 빌드
                    sh './gradlew build'
                }
            }
        }
        stage('Test Spring Boot') {
            steps {
                dir('Classik_Backend') {
                    // Gradle로 테스트 실행
                    sh './gradlew test'
                }
            }
        }
        stage('Deploy with Docker Compose') {
            steps {
                // 기존 컨테이너 종료 및 새 컨테이너 실행
                sh '''
                docker-compose -f $DOCKER_COMPOSE_PATH stop
                docker-compose -f $DOCKER_COMPOSE_PATH up -d --build
                '''
            }
        }
    }
    post {
        always {
            echo 'Pipeline execution completed.'
        }
        success {
            echo 'Pipeline succeeded. Application is running successfully.'
        }
        failure {
            echo 'Pipeline failed. Check the logs for errors.'
        }
    }
}
