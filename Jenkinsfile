pipeline {
  agent any
  stages {
    stage('Compile') {
      steps {
        parallel(
          "Compile": {
            sh 'sbt fullOptJS'
            
          },
          "HTML": {
            sh '''mkdir tmp
cp app.html tmp/
sed 's/<script>*/<script type="text/javascript" src="purplerain-scalajs-opt.js">/' tmp/app.html > tmp/app2.html'''
            
          }
        )
      }
    }
    stage('Artifact') {
      steps {
        archiveArtifacts(artifacts: 'target/scala-2.12/purplerain-scalajs-opt.js', onlyIfSuccessful: true)
        archiveArtifacts(artifacts: 'tmp/app2.html', onlyIfSuccessful: true)
      }
    }
  }
}