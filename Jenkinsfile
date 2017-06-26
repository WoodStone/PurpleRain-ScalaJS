pipeline {
  agent any
  stages {
    stage('Opt') {
      steps {
        sh 'sbt fullOptJS'
      }
    }
    stage('') {
      steps {
        archiveArtifacts(artifacts: 'target/scala-2.12/purplerain-scalajs-opt.js', onlyIfSuccessful: true)
      }
    }
  }
}