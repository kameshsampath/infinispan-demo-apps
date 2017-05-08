node('maven') {
  stage('Checkout') {
    git url: "https://github.com/kameshsampath/infinispan-demo-apps.git"
  }
  
  def isCanary = input(
    id: 'canaryDeployment', message: 'Is it a canary release ?', parameters: [
    [$class: 'BooleanParameterDefinition', defaultValue: false, description: '', name: 'Is this a canary release ?']
  ])

  stage('Deploy') {
     dir('popular-movie-store')
     if(isCanary){
        echo "Doing Canary Release"
        sh "mvn -Pcanary clean fabric8:deploy"
     }else{
        echo "Doing pre-canary Release"
        sh "mvn clean fabric8:deploy"
     }
  }

}