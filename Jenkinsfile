#!/usr/bin/groovy

def isCanary = input(
id: 'canaryDeployment', message: 'Is it a canary release ?', parameters: [
[$class: 'BooleanParameterDefinition', defaultValue: false, description: '', name: 'Is this a canary release ?']
])

node('maven') {
  stage 'Checkout' {
    git url: "https://github.com/kameshsampath/infinispan-demo-apps.git"
  }
  
  if(isCanary){
      stage 'Canary Release' {
       dir('popular-movie-store') {
         sh "./mvnw -DapiKey=xxsysysysys -Pcanary clean fabric8:deploy"
       }
      }
  }else{
      stage 'Release'  {
       dir('popular-movie-store') {
          sh "./mvnw -DapiKey=xxsysysysys clean fabric8:deploy"
       }
     }
  }

}