pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                echo 'Checking out the repository...'
                checkout scm
            }
        }
        stage('Build') {
            steps {
                echo 'Cleaning and testing Maven project...'
                sh 'mvn clean test'
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
