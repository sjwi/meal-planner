pipeline {
  agent any
  stages {
    stage('Build WAR') {
      steps {
        sh 'mvn clean install package'
      }
    }
    stage('Deploy Production App') {
      steps {
        sh 'sudo cp target/meals.war /opt/tomcat/webapps'
      }
    }
    stage('Setup Git config') {
      steps {
        withCredentials([
          usernamePassword(credentialsId:'github_token', usernameVariable: 'USER', passwordVariable: 'TOKEN')
        ]) {
          sh "git remote set-url origin https://$TOKEN@github.com/sjwi/meal-planner.git"
        }
      }
    }
    stage('Checkout Demo Config') {
      steps {
        sh '''
          git fetch
          git checkout origin/demo -- src/main/java/com/sjwi/meals/config/LandingPageSessionInitializer.java
          git checkout origin/demo -- src/main/resources/application.properties
        '''
      }
    }
    stage('Build Demo WAR') {
      steps {
        sh 'mvn clean install package'
      }
    }
    stage('Deploy Demo App') {
      steps {
        sh 'sudo cp target/meals.war /opt/tomcat/webapps/meals-demo.war'
      }
    }
  }
}