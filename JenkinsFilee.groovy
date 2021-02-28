// Import the Shared Libraries to be used in this Pipeline. To configure shared Libraries refer to the confluence page - https://confluence.cms.gov/display/CBCSHAREDLIB/CB+Core+Master+Configuration+Changes#CBCoreMasterConfigurationChanges-CBCoreSet-upforSharedLibraries

/*
* This JenkinsFile will provide the minimum required steps to Implement npm install/npm ci.
* This JenkinsFile Uses the Map based Input
* Referer to the readme for additional Details - https://github.cms.gov/CVPWQC/CI-Shared-Libraries/blob/v1.2.2/vars/jfrogNpmInstall.md
*/

// fill out the below map with the appropriate Values for your Usecase

Map jfrogConfig=[jfrog:[artifactory:[credentialId:'nimbus-artifactory']]]

Map jfrognpmConfig=[
  jfrog:[
    artifactory:[
      servername:'rt-server-prod',
      buildName:'Nimbus-Npm-Map-example',
      npm:[
        resolveRepo:'nimbus-npm'
        ]
    ]
],
    app:[
      rootDir:'app',platform:[
        nodejs:[
          version:'v12',isNpmCi:'']
               ]
        ]
    ]

// the pod template should consists of the image which is nvm.
pipeline {
    agent {
    kubernetes {
        yaml '''
apiVersion: v1
kind: Pod
spec:
  containers:
    - name: jfrog-nvm
      image: "centos:centos7"
      tty: true
      command: ["tail", "-f", "/dev/null"]
      imagePullPolicy: Always
        '''
        }
    }

  environment {
    JFROG_CLI_HOME_DIR = "${env.WORKSPACE}/.jfrog"
    JFROG_CLI_OFFER_CONFIG=false
}

    options {
    skipDefaultCheckout true
  }

  stages {
      stage('Config-JfrogCli'){
          steps{
              container('jfrog-nvm'){
                withCredentials([usernamePassword(credentialsId: 'Jay-Github-Public', usernameVariable: 'GITHUB_APP', passwordVariable: 'GITHUB_ACCESS_TOKEN')]) {
                sh """
                curl \
                    -X POST \
                    -H "Content-Type: application/json" \
                    -H "Accept: application/vnd.github.v3+json" \
                    -H "authorization: ${GITHUB_ACCESS_TOKEN}" \
                    https://api.github.com/repos/jayadeepGit/test-npm-mod/check-runs \
                    -d '{"name":"First CheckRun","head_sha":"6a6292523a62ac14b57815b744941bcdc1bc9ffc"}'
                """
                
                
              }
              }
          }
      }
    }
	}
