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
    stage('Checkout Demo Config') {
      steps {
        sh '''
          git fetch
          git checkout origin/demo -- src/main/java/com/sjwi/meals/config/LandingPageSEssionInitializer.java
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