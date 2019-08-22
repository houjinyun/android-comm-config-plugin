在Android开发中，通用配置 Gradle 插件。

#### 1.插件用途
当我们开发 Android 项目时，随着组织庞大以后，现在一般会采用组件化架构的开发模式，
不同的人负责不同的组件，这样会造成很多问题，常见的有：

1. 第三方依赖库版本号不统一，合并打包时造成冲突；
2. 滥用第三方依赖库；
3. 编译时所用 sdk 版本不统一，合并打包时出现莫名其妙问题；

除此之外，我们还可以通过插件做很多通用配置，这样一是统一各种规范，二是强制所有开发人员遵循这一规范。

#### 2.主要功能

1. 强制使用相同的 compileSdkVersion、targetSdkVersion、minSdkVersion；
2. 统一所有依赖的 com.android.support 库的版本号；
3. 统一 kotlin、GSON、multidex 库的版本号；
4. 统一 repositories 的路径为 libs 目录；
5. 打包时移除 META-INF/** 文件，例如：META-INF/*.kotlin_module；
6. 强制将 implementation 形式的依赖，改为 api 形式的依赖；
7. 增加 git hooks，规范 commit 提交信息
