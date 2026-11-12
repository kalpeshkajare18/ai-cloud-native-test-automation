pipeline {
    agent any

    options {
        timeout(time: 30, unit: 'MINUTES')
        disableConcurrentBuilds()
    }

    environment {
        IMAGE       = "qa-tests:${env.BUILD_NUMBER}"
        CONTAINER   = "qa-run-${env.BUILD_NUMBER}"
        // Docker network created by docker compose in /infra (folder name + _default)
        NETWORK     = "infra_default"
        SONAR_URL   = "http://sonarqube:9000"
        SONAR_KEY   = "ai-cloud-native-test-automation"
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build test image') {
            steps {
                sh 'docker build -t $IMAGE .'
            }
        }

        stage('Run Playwright tests') {
            steps {
                sh '''
                    set +e
                    docker run --name $CONTAINER $IMAGE
                    RC=$?
                    mkdir -p reports traces
                    docker cp $CONTAINER:/app/target/surefire-reports/. reports/
                    docker cp $CONTAINER:/app/target/traces/. traces/
                    docker rm $CONTAINER
                    exit $RC
                '''
            }
        }

        stage('SonarQube analysis + quality gate') {
            steps {
                withCredentials([string(credentialsId: 'sonar-token', variable: 'SONAR_TOKEN')]) {
                    sh '''
                        docker run --rm --network $NETWORK -e SONAR_TOKEN $IMAGE sh -c "mvn -B -q test-compile org.sonarsource.scanner.maven:sonar-maven-plugin:3.11.0.3922:sonar -Dsonar.host.url=$SONAR_URL -Dsonar.token=\\$SONAR_TOKEN -Dsonar.projectKey=$SONAR_KEY -Dsonar.qualitygate.wait=true"
                    '''
                }
            }
        }
    }

    post {
        always {
            junit allowEmptyResults: true, testResults: 'reports/*.xml'
            archiveArtifacts artifacts: 'traces/**', allowEmptyArchive: true
            sh 'docker rm -f $CONTAINER || true'
            sh 'docker rmi $IMAGE || true'
        }
    }
}
