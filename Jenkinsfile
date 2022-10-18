pipeline {
    agent any
    tools {
        jdk 'jdk8'
        maven 'maven_3_6_3'
    }
    environment {
        PRIVATE_KEY = 't7uuuGJCeWH2LFcmDKTgpt3Y4faebukr'
        TOKEN_LIFE    = '86400000'
        PG_DB_USER = 'postgres'
        PG_DB_PASSWORD = 'qub3dl@b123*'
        registry = "qubedprince/qubedprince/retrospecs-permission-service-image"
        registryCredential = 'dockerHub'
    }
    stages {
        stage ('Version'){
            steps {
                sh 'mvn --version'
            }
        }
        stage ('Dependency'){
            steps {
                sh 'mvn dependency:tree'
            }
        }
        stage ('Test') {
            steps {
               sh 'mvn test -Dspring.profiles.active=test'
            }
        }
        stage ('Verify and LoadTest'){
            steps {
               sh 'mvn clean verify -Dspring.profiles.active=test'
            }
         }

        stage ('Build') {
            steps {
                sh 'mvn package'
            }
        }
		    // Building Docker images
	    stage('Building image') {
	      steps{
		script {
		  dockerImage = docker.build registry
		}
	      }
	    }

	    	     // Uploading Docker images into Docker Hub
	    stage('Upload Image') {
	     steps{
		 script {
		    docker.withRegistry( '', registryCredential ) {
		    dockerImage.push()
		    }
		}
	      }
	    }

        stage ('Deploy on Server & Cleanup'){
        steps{
              script {
               sshPublisher(
                continueOnError: false, failOnError: true,
                publishers: [
                 sshPublisherDesc(
                  sshCredentials(
                      username: "${env.RETRO_USER}",
                      encryptedPassphrase: "${env.RETRO_PASSWORD}",
                  ),
                  verbose: true,
                  transfers: [
                   sshTransfer(
                    execCommand: "echo test"
                   )
                  ])
                ])
              }
            }
        }

    }
    post {
        failure {
           telegramSend(message: "*JOB* : *${env.JOB_NAME}*\n*Report* : ${env.BUILD_TAG} \n*Branch*: main \n*Build* : ${env.BUILD_ID} \n*Status*: _FAILED_ \n*Vist* : ${env.BUILD_URL} for more informantion\n.Happy Coding ", chatId: 717316992 )
        }
         success {
            telegramSend(message: "*JOB* : *${env.JOB_NAME}*\n*Report* : ${env.BUILD_TAG} \n*Branch*: main \n*Build* : ${env.BUILD_ID} \n*Status*: _SUCCESS_ \n*Vist* : ${env.BUILD_URL} for more informantion\n", chatId: 717316992)

        }
    }
}

