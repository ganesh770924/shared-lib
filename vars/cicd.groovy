def checkout(Closure body={}){
git branch: 'master', credentialsId: 'cicddemoaccesstoken', url: 'https://github.com/ganesh770924/CICDrepo.git'
}

def build(Closure body={}){
 sh "mvn clean install package -DskipTests"
    }

def test(Closure body={}){
 junit(testResults: 'target/surefire-reports/*.xml', allowEmptyResults : true)
}
def sonarscanner(Closure body={}){
                 sh '''${scannerHome}/bin/sonar-scanner -Dsonar.projectKey=CI_Jenkins \
                   -Dsonar.projectName=CI-CD_Jenkins \
                   -Dsonar.projectVersion=1.0 \
                   -Dsonar.sources=src/ \
                   -Dsonar.java.binaries=/var/lib/jenkins/workspace/cicdpipeline/target'''
               }

def upartifacts(Closure body={}){
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

def deploy(Closure body={}){
   deploy adapters: 
  [tomcat9(credentialsId: 'tomcat',
 
   url: 'http://15.206.167.30:8080')],
   contextPath: 'demo',
   onFailure: false,
   war: '**/target/*.war' 
  
}
