pipeline {
    agent {
        docker {
            image 'mcr.microsoft.com/playwright:v1.45.0-jammy'
            args '-u root' // Optional: Run Docker container as root if necessary
        }
    }
    stages {
        stage('Checkout') {
            steps {
                echo 'Checking out the repository...'
                checkout scm
            }
        }
        stage('Build') {
            steps {
                echo 'Installing npm dependencies...'
                sh 'npm ci'
            }
        }
        stage('Run Tests') {
            steps {
                echo 'Running Playwright tests...'
                sh 'npx playwright test'
            }
        }
    }
    post {
        always {
            echo 'Archiving test results...'
            junit 'target/surefire-reports/*.xml'
        }
    }
}
