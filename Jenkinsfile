
// server
node {

    stage('Compile') {
        echo '编译打包开始'
        sh "$maven_home clean package -Dmaven.test.skip=true --settings=$maven_yto_setting -P $env"
        echo '编译打包完成'
   }
}