node{
	stage("test"){
		sh "echo testiing"
		checkout scm
	}
}
