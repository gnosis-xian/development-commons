
// server
node {

    maven_home='/home/gnosis/apache-maven-3.6.1/bin/mvn'
    maven_yto_setting='/home/gnosis/apache-maven-3.6.1/conf/yto/settings_yto_new.xml'

    stage('Compile') {
        echo '编译打包开始'
        sh "$maven_home clean package -Dmaven.test.skip=true --settings=$maven_yto_setting -P $env"
        echo '编译打包完成'
   }
}