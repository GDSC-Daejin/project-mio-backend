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
        sh '''
        git fetch
        git pull origin master
        '''
      }
      post {
        success {
          echo 'Git Pull - Success'
        }
      }
    }

    stage('build') {
      steps {
        echo 'Build Start'
        sh '''
        chmod 755 ./gradlew
        ./gradlew clean build
        '''
      }
    }

    stage('deploy') {
      steps {
        echo 'Deploy Start'
        sh '''
        if [ $(docker ps -q -f name=${CONTAINER_NAME}) ]; then
          docker stop ${CONTAINER_NAME}
          docker rm ${CONTAINER_NAME}
        fi
        if [ $(docker images -q ${IMAGE_NAME}) ]; then
          docker rmi ${IMAGE_NAME}
        fi
        docker build -t ${IMAGE_NAME} .
        docker run -d --name ${CONTAINER_NAME} -p 8080:8080 -e HOST=221.145.28.69 -e PORT=3306 -e NAME=mioserver -e USER=mio -e PASSWORD=0194 -e CLIENT=975290935363-39a6c8jmq1s3psk5fd57m9t7f5r81noq.apps.googleusercontent.com -e JWT=kajshdfklhasdkflhlsifyiqewyfiadhsfjahdsfksgjhsfklafdhkelfsodusehrkjdfgrsejkuseghfsadfasdfasdfasdfasdf -e DISCORD=https://discord.com/api/webhooks/1209355197739180042/us9PD2B7Mdjxr8NXprPMl-MV2PCKRlduMF5IJHj2pwHPth1zWHnAfo7c03UyAr12TqJh -e SSLPWD = 019499 -v /home/jenkins:/var/jenkins_home ${IMAGE_NAME}
        '''
      }

    }
  }
}