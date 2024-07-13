pipeline {
  agent any
  environment {
    CONTAINER_NAME = 'mio-con'
    IMAGE_NAME = 'mio-img'
  }

  stages {
    stage('git pull') {
      steps {
        echo 'Git Pull - Start'
        sh """
        git fetch
        git pull origin master
        """
      }
      post {
        success {
          echo 'Git Pull - Success'
        }
        failure {
          error 'Git Pull - Failure'
        }
      }
    }

    stage('build') {
      steps {
        echo 'Build Start'
        sh """
        chmod 755 ./gradlew
        ./gradlew clean build
        """
      }
      post {
        success {
          echo 'Build Success'
        }
        failure {
          error 'Build Failure -> Stop'
          mail to: 'anes53027@gmail.com',
               subject: "Jenkins Failure Build",
               body: "Build Failed."
        }
      }
    }

    stage('deploy') {
      steps {
        echo 'Deploy Start'
        sh """
        if [ \$(docker ps -q -f name=${CONTAINER_NAME}) ]; then
          docker stop ${CONTAINER_NAME}
          docker rm ${CONTAINER_NAME}
        fi
        if [ \$(docker images -q ${IMAGE_NAME}) ]; then
          docker rmi ${IMAGE_NAME}
        fi
        docker build -t ${IMAGE_NAME} .
        docker run -d --name ${CONTAINER_NAME} -p 8080:8080 -v /home/jenkins:/var/jenkins_home ${IMAGE_NAME}
        """
      }
      post {
        success {
          mail to: 'anes53027@gmail.com',
               subject: "Jenkins Success Deploy",
               body: "Deploy Success"
        }
        failure {
          error 'Deploy Failure -> Stop'
          mail to: 'anes53027@gmail.com',
               subject: "Jenkins Failure Deploy",
               body: "Deploy Failed."
        }
      }
    }
  }
}