pipeline {
  agent any
  stages {
    stage('Build WAR') {
      steps {
        // echo "Step 1"
       sh 'mvn clean install package'
      }
    }
    stage('Deploy Production App') {
      steps {
        withCredentials([
            usernamePassword(credentialsId: 'digiocean_creds', usernameVariable: 'DIGIOCEAN_UN', passwordVariable: 'DIGIOCEAN_PW'),
            string(credentialsId:'meals_dns', variable: 'DNS')
        ]) {
            sh "scp target/meals.war $DIGIOCEAN_UN@$DNS:/opt/tomcat/webapps/ROOT.war"
            sh "sleep 3"
            sh "ssh $DIGIOCEAN_UN@$DNS -o StrictHostKeyChecking=no 'systemctl restart tomcat.service'"
        }
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