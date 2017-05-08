node('maven') {
  stage('Build') {
    git url: "https://github.com/kameshsampath/infinispan-demo-apps.git"
    sh "cd popular-movie-store"
  }
  
  def isCanary = input(
    id: 'canaryDeployment', message: 'Is it a canary release ?', parameters: [
    [$class: 'BooleanParameterDefinition', defaultValue: false, description: '', name: 'Is this a canary release ?']
  ])

  stage('Deploy') {
     if(isCanary){
        echo "Doing Canary Release"
        sh "./mvnw -Pcanary clean fabric8:deploy"
     }else{
        echo "Doing Canary Release"
        sh "./mvnw clean fabric8:deploy"
     }
  }

}