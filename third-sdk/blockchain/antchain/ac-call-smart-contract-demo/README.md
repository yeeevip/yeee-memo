

//安装 SDK 到本地仓库
mvn install:install-file -Dfile=mychainx-sdk-0.10.2.12.jar -DgroupId=com.alipay.mychainx -DartifactId=mychainx-sdk -Dversion=0.10.2.12 -Dpackaging=jar

//安装 Netty 依赖到本地仓库，注意选择对应平台 netty-tcnative-openssl-static 版本，注意修改 classifier，macOS :osx-x86_64、linux:linux-x86_64、windows:windows-x86_64
mvn install:install-file -Dfile=netty-tcnative-openssl-static-2.0.17-Final-mychain-osx-x86_64.jar -DgroupId=io.netty -DartifactId=netty-tcnative-openssl-static -Dversion=2.0.17-Final-mychain -Dpackaging=jar -Dclassifier=osx-x86_64
