node{
	stage("test"){
		sh "echo testiin"
		checkout scm
	}
}
