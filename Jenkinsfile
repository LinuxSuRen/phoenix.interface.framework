node {
  def mvnHome
  
  stage('Preparation') {
    git 'https://github.com/LinuxSuRen/phoenix.interface.framework.git'
    mvnHome = tool 'M3'
  }
  
  stage('Clean') {
    if(isUnix()){
      sh "'${mvnHome}/bin/mvn' clean"
    }else{
      bat(/"${mvnHome}\bin\mvn" clean/)
    }
  }
  
  stage('JavaDoc') {
    if(isUnix()){
      sh "'${mvnHome}/bin/mvn' javadoc:jar -DdocSkip=false"
    }else{
      bat(/"${mvnHome}\bin\mvn" javadoc:jar -DdocSkip=false/)
    }
  }
  
  stage('Package') {
    if(isUnix()){
      sh "'${mvnHome}/bin/mvn' package"
    }else{
      bat(/"${mvnHome}\bin\mvn" package/)
    }
  }
  
  stage('Deploy') {
    if(isUnix()){
      sh "'${mvnHome}/bin/mvn' deploy -DsignSkip=false -DdocSkip=false"
    }else{
      bat(/"${mvnHome}\bin\mvn" deploy -DsignSkip=false -DdocSkip=false/)
    }
  }
}

properties([
    [
        $class: 'GithubProjectProperty',
        displayName: 'phoenix.interface.framework',
        projectUrlStr: 'https://github.com/LinuxSuRen/phoenix.interface.framework'
    ],
    buildDiscarder(
        logRotator(
            artifactDaysToKeepStr: '',
            artifactNumToKeepStr: '',
            daysToKeepStr: '7',
            numToKeepStr: '14'
        )
    ),
    pipelineTriggers([
        pollSCM('H/15 * * * *')
    ])
])