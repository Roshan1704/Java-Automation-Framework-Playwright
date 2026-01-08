pipeline {
    agent any
    
    options {
        buildDiscarder(logRotator(numToKeepStr: '10'))
        timestamps()
        timeout(time: 1, unit: 'HOURS')
    }

    parameters {
        choice(
            name: 'ENVIRONMENT',
            choices: ['qa', 'uat', 'prod'],
            description: 'Test environment'
        )
        choice(
            name: 'SUITE',
            choices: ['smoke', 'regression', 'full'],
            description: 'Test suite to run'
        )
        booleanParam(
            name: 'PARALLEL',
            defaultValue: true,
            description: 'Run tests in parallel'
        )
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
                echo "Checked out source code from SCM"
            }
        }

        stage('Build') {
            steps {
                script {
                    echo "Building Playwright framework..."
                    sh 'mvn clean compile -DskipTests'
                }
            }
        }

        stage('Run Tests') {
            steps {
                script {
                    echo "Running ${params.SUITE} tests on ${params.ENVIRONMENT} environment"
                    def testCommand = "mvn clean test " +
                        "-Denvironment=${params.ENVIRONMENT} " +
                        "-DsuiteXmlFile=src/test/resources/testng-${params.SUITE}.xml"
                    
                    if (params.PARALLEL) {
                        testCommand += " -DthreadCount=4"
                    }
                    
                    sh testCommand
                }
            }
        }

        stage('Generate Reports') {
            steps {
                script {
                    echo "Generating Allure reports..."
                    sh 'mvn allure:report'
                    sh 'mvn allure:serve &'
                }
            }
        }

        stage('Publish Results') {
            steps {
                script {
                    // Archive test results
                    junit 'target/surefire-reports/**/*.xml'
                    
                    // Archive screenshots
                    archiveArtifacts artifacts: 'target/screenshots/**/*.png', 
                        allowEmptyArchive: true
                    
                    // Archive traces
                    archiveArtifacts artifacts: 'target/traces/**/*.zip', 
                        allowEmptyArchive: true
                    
                    // Publish Allure results
                    publishHTML([
                        reportDir: 'target/site/allure-maven-plugin',
                        reportFiles: 'index.html',
                        reportName: 'Allure Test Report'
                    ])
                }
            }
        }

        stage('Notify') {
            steps {
                script {
                    def buildStatus = currentBuild.result ?: 'SUCCESS'
                    def message = "Playwright Tests - ${params.SUITE} on ${params.ENVIRONMENT}: ${buildStatus}"
                    
                    // Slack notification
                    echo "Sending ${buildStatus} notification to Slack"
                    
                    // Email notification
                    emailext(
                        subject: message,
                        body: """
                        Build: ${env.BUILD_NUMBER}
                        Environment: ${params.ENVIRONMENT}
                        Suite: ${params.SUITE}
                        Status: ${buildStatus}
                        
                        Allure Report: ${env.BUILD_URL}allure
                        """,
                        to: '${DEFAULT_RECIPIENTS}'
                    )
                }
            }
        }
    }

    post {
        always {
            cleanWs()
            echo "Pipeline completed"
        }
        success {
            echo "All tests passed successfully!"
        }
        failure {
            echo "Tests failed. Check logs and artifacts."
        }
    }
}
