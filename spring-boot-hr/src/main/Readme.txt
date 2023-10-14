一、SpringBoot 源码编译注意事项
------------ 该事项仅对自己编译 SpringBoot 源码有效，如果直接拉取本项目则仅需要下载 gradle 并配置相应的位置 -----------
**环境条件
 1. jdk 1.8
 2. gradle 6.9
 	下载：https://services.gradle.org/distributions/
 3. springBoot源码
 	https://github.com/spring-projects/spring-boot/tree/2.3.x
 4. idea 配置
 	1) 配置 jvm 内存，C:\Program Files\JetBrains\IntelliJ IDEA 2019.1.3\bin\idea64.exe.vmoptions
 		-Xms2048m
		-Xmx4096m
		-XX:ReservedCodeCacheSize=1024m
	2) C:\Users\{userName}\.IntelliJIdea2019.1\config\idea64.exe.vmoptions
	3) idea settings --> gradle --> 配置 gradle home --> 配置 gradle jvm
		--> Gradle VM options: -XX:MaxPermSize=8192m -Xmx8192m -XX:MaxHeapSize=8192m
	4) idea settings --> Compiler --> Build Process heap size --> 10000

** 导入 springBoot 源码修改配置文件
1. gradle\wrapper\gradle-wrapper.properties
	修改 gradle 为本地的地址，避免每次都要下载 --> distributionUrl=file:///f:gradle/gradle-6.9-all.zip
**************************** 注意: 无论时那种方式编译方式都需要将这里的位置替换为自己 gradle 所在的目录 *****************
2. 修改 build.gradle 文件（根目录和 buildSrc 目录）
  1) buildSrc --> build.gradle
	repositories {
		maven { url 'http://maven.aliyun.com/nexus/content/groups/public/' }
		maven { url 'http://maven.aliyun.com/nexus/content/repositories/jcenter' }
		maven { url "https://repo.spring.io/plugins-release" }
		mavenCentral()
		gradlePluginPortal()
		maven { url "https://repo.spring.io/release" }
	}
  2) build.gradle
  	buildscript {
		repositories {
			maven { url 'http://maven.aliyun.com/nexus/content/groups/public/' }
			maven { url 'http://maven.aliyun.com/nexus/content/repositories/jcenter' }
			maven { url "https://repo.spring.io/plugins-release" }
		}
	}
	repositories {
		maven { url 'http://maven.aliyun.com/nexus/content/groups/public/' }
		maven { url 'http://maven.aliyun.com/nexus/content/repositories/jcenter' }
		mavenCentral()
		if (!version.endsWith('RELEASE')) {
			maven { url "https://repo.spring.io/milestone" }
		}
		if (version.endsWith('BUILD-SNAPSHOT')) {
			maven { url "https://repo.spring.io/snapshot" }
		}
	}
3. 修改 setting.gradle 文件（根目录和 buildSrc 目录）
  1) buildSrc --> settings.gradle
	repositories {
		maven { url 'http://maven.aliyun.com/nexus/content/groups/public/' }
		maven { url 'http://maven.aliyun.com/nexus/content/repositories/jcenter' }
		maven { url "https://repo.spring.io/plugins-release" }
		mavenCentral()
		gradlePluginPortal()
	}
  2) settings.gradle
  	repositories {
		maven { url 'http://maven.aliyun.com/nexus/content/groups/public/' }
		maven { url 'http://maven.aliyun.com/nexus/content/repositories/jcenter' }
		mavenCentral()
		gradlePluginPortal()
		maven {
			url 'https://repo.spring.io/plugins-release'
		}
		if (version.endsWith('BUILD-SNAPSHOT')) {
			maven { url "https://repo.spring.io/snapshot" }
		}
	}
    如果导入前已经修改了上面的文件，导入时会自动编译，等待编译完成即可。如果在导入前没有修改配置，在导入时收到停止编译，等上面的配置修改完成后再手动编译即可

二、SpringBoot 源码调试顺序
