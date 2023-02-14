def checkout(Map Stageparams){
git branch: 'master', credentialsId: 'githublogin', url: 'git@github.com:satyam146/test2.git'
}

def build(){
 sh "mvn clean install package -DskipTests"
    }

def test(){
 junit(testResults: 'target/surefire-reports/*.xml', allowEmptyResults : true)
}
def sonarscanner(){
                 sh '''${scannerHome}/bin/sonar-scanner -Dsonar.projectKey=CI_Jenkins \
                   -Dsonar.projectName=CI-CD_Jenkins \
                   -Dsonar.projectVersion=1.0 \
                   -Dsonar.sources=src/ \
                   -Dsonar.java.binaries=/var/lib/jenkins/workspace/CI-CD/target/test-classes/com/example/demo '''
               }

def upartifacts(){
  rtUpload (
                    // Obtain an Artifactory server instance, defined in Jenkins --> Manage Jenkins --> Configure System:
                    serverId: SERVER_ID,
                    spec: """{
                            "files": [
                                    {
                                        "pattern": "/var/lib/jenkins/workspace/CI-CD/target/*.war",
                                        "target": "demo/"
                                      
                                    }
                                ]
                            }""",
    buildName: '${env.BUILD_NUMBER}',
    buildNumber: '${env.BUILD_TIMESTAMP}'
                  )
}

def deploy(){
   deploy adapters: 
  [tomcat9(credentialsId: 'tomcat',
 
   url: 'http://3.109.183.122:8006')],
   contextPath: 'demo',
   onFailure: false,
   war: '**/target/*.war' 
  
}
