pipeline {
  agent any
  stages {
    stage('Clean') {
      steps {
        sh 'rm -rf tmp'
      }
    }
    stage('Init') {
      steps {
        sh '''rm -rf tmp
mkdir tmp
cp app.html tmp/'''
      }
    }
    stage('Compile') {
      steps {
        parallel(
          "Compile": {
            sh 'sbt fullOptJS'
            
          },
          "error": {
            sh '''sed 's/<script.*/<script type="text\\/javascript" src="purplerain-scalajs-opt.js">/' tmp/app.html > tmp/app2.html
rm app.html
mv app2.html app.html'''
            
          }
        )
      }
    }
    stage('Copy') {
      steps {
        sh 'cp target/scala-2.12/purplerain-scalajs-opt.js tmp/'
      }
    }
    stage('Artifact') {
      steps {
        archiveArtifacts(artifacts: 'tmp/purplerain-scalajs-opt.js', onlyIfSuccessful: true)
        archiveArtifacts(artifacts: 'tmp/app.html', onlyIfSuccessful: true)
      }
    }
  }
}