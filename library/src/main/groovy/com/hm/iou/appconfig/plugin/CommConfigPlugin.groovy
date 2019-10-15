package com.hm.iou.appconfig.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * 组件通用配置插件，提供的功能有：
 *
 * 1. 强制使用相同的 compileSdkVersion、targetSdkVersion、minSdkVersion；
 * 2. 统一所有依赖的 com.android.support 库的版本号；
 * 3. 统一 kotlin、GSON、multidex 库的版本号；
 * 4. 统一 repositories 的路径为 libs 目录；
 * 5. 打包时移除 META-INF/** 文件，例如：META-INF/*.kotlin_module；
 * 6. 强制将 implementation 形式的依赖，改为 api 形式的依赖；
 * 7. 增加 git hooks，规范 commit 提交信息
 *
 */
class CommConfigPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {

        try {
            GitHooksUtil.checkGitHookFile(project)
        } catch (IOException e) {
            e.printStackTrace()
        }

        //统一第三方依赖库的版本号，常见的有 com.android.support 包等
        LibVersionUtil.checkLibVersion(project)

        //检测 android 编译配置等
        CompileConfigUtil.checkCompileConfig(project)

        //增加检测 ProGuard 的task
        ProguardRuleCheckUtil.checkProguardFile(project)
    }

}