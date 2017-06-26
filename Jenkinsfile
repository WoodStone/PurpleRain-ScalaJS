pipeline {
  agent any
  stages {
    stage('Opt') {
      steps {
        sh 'sbt fullOptJS'
        archiveArtifacts(artifacts: 'target/scala-2.12/purplerain-scalajs-opt.js', onlyIfSuccessful: true)
      }
    }
    stage('error') {
      steps {
        archiveArtifacts(artifacts: 'target/scala-2.12/purplerain-scalajs-opt.js', onlyIfSuccessful: true)
      }
    }
  }
}