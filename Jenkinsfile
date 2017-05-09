#!/usr/bin/groovy

def userInput = input(
id: 'userInput', message: 'Build needs your attention', parameters: [
[$class: 'BooleanParameterDefinition', defaultValue: false, description: 'Canary release ?', name: 'canary'],
[$class: 'TextParameterDefinition', defaultValue: 'blahblahblah', description: 'MovieDB API Key', name: 'apiKey']
])

def isCanary = userInput['canary']
def apiKey = userInput['apiKey']

node('maven') {
  stage('Checkout') {
    git url: "https://github.com/kameshsampath/infinispan-demo-apps.git"
  }

  if(isCanary){
     stage('Canary Release'){
       sh "./mvnw -DapiKey=${apiKey} -Pcanary clean fabric8:deploy"
     }
  }else{
    stage('Release'){
       sh "./mvnw -DapiKey=${apiKey} clean fabric8:deploy"
    }
  }

}