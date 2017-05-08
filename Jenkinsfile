node('maven') {
  stage('Checkout') {
    git url: "https://github.com/kameshsampath/infinispan-demo-apps.git"
  }
  
  def isCanary = input(
    id: 'canaryDeployment', message: 'Is it a canary release ?', parameters: [
    [$class: 'BooleanParameterDefinition', defaultValue: false, description: '', name: 'Is this a canary release ?']
  ])

  if(isCanary){
      stage('Canary Deploy') {
        dir('popular-movie-store')
        sh "mvn -DapiKey=xxsysysysys -Pcanary clean fabric8:deploy"
      }
  }else{
      stage('Deploy') {
        dir('popular-movie-store')
        sh "mvn -DapiKey=xxsysysysys clean fabric8:deploy"
     }
  }

}