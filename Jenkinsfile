pipeline {
    agent any

    options {
        timeout(time: 30, unit: 'MINUTES')
        disableConcurrentBuilds()
    }

    environment {
        IMAGE = "192.168.0.108:5000/qa-tests:${BUILD_NUMBER}"
        K8S_JOB_NAME = "playwright-tests-${BUILD_NUMBER}"
        SONAR_URL = "http://sonarqube:9000"
        KUBECONFIG = "/var/jenkins_home/.kube/config"
        SONAR_KEY = "ai-cloud-native-test-automation"
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

        stage('Push image to registry') {
            steps {
                sh 'docker push $IMAGE'
            }
        }

        stage('Run Playwright tests on Kubernetes') {
            steps {
                sh '''
                    cat > playwright-job.yaml <<EOF
apiVersion: batch/v1
kind: Job
metadata:
  name: ${K8S_JOB_NAME}
  namespace: qa-automation
spec:
  backoffLimit: 0
  template:
    spec:
      restartPolicy: Never
      containers:
        - name: playwright-tests
          image: ${IMAGE}
          imagePullPolicy: Always
          volumeMounts:
            - name: test-reports
              mountPath: /app/target
      volumes:
        - name: test-reports
          emptyDir: {}
EOF

                    kubectl apply -f playwright-job.yaml

                    kubectl wait --for=condition=complete job/${K8S_JOB_NAME} \
                        -n qa-automation --timeout=10m
                '''
            }
        }

        stage('Collect Kubernetes test logs') {
            steps {
                sh '''
                    kubectl logs job/${K8S_JOB_NAME} -n qa-automation
                '''
            }
        }

        stage('SonarQube analysis + quality gate') {
            steps {
                withCredentials([string(credentialsId: 'sonar-token', variable: 'SONAR_TOKEN')]) {
                    sh '''
                        docker run --rm --network infra_default \
                        -e SONAR_TOKEN $IMAGE \
                        sh -c "mvn -B -q test-compile org.sonarsource.scanner.maven:sonar-maven-plugin:3.11.0.3922:sonar -Dsonar.host.url=$SONAR_URL -Dsonar.token=\\$SONAR_TOKEN -Dsonar.projectKey=$SONAR_KEY -Dsonar.qualitygate.wait=true"
                    '''
                }
            }
        }
    }

    post {
        always {
            sh '''
                kubectl delete job ${K8S_JOB_NAME} -n qa-automation --ignore-not-found=true || true
                docker rmi $IMAGE || true
            '''
        }
    }
}
