def call(){
   def  mvnHome = tool name: 'maven3' , type: 'maven'
   withSonarQubeEnv('mysonarqube'){
     sh "${mvnHome}/bin/mvn sonar:sonar"
   }
}
