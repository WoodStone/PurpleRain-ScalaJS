pipeline {
  agent any
  stages {
    stage('Opt') {
      steps {
        sh 'sbt fullOptJS'
      }
    }
  }
  environment {
    jioj = 'j'
  }
}