package com.hm.iou.appconfig.plugin

import com.android.build.gradle.AppExtension
import com.android.build.gradle.BaseExtension
import com.android.build.gradle.api.ApplicationVariant
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.Task

/**
 * Proguard 配置规则自动检测
 */
class ProguardRuleCheckUtil {

    /**
     * 检测工程里配置的 Proguard 规则
     *
     * @param project
     */
    static void checkProguardFile(Project project) {
        project.afterEvaluate {
            BaseExtension android = project.extensions.getByName("android")
            if (!(android instanceof AppExtension)) {
                return
            }
            android.applicationVariants.all { ApplicationVariant variant ->
                if (!variant.buildType.minifyEnabled) {
                    println("检测到没有开启混淆，不检查 ProGuard 混淆配置文件")
                    return
                }

                String compileTaskName = "compile${variant.name.capitalize()}JavaWithJavac"
                Task compileTask = project.tasks.getByName(compileTaskName)
                if (compileTask != null) {
                    Task checkTask = project.task("hm${variant.name.capitalize()}CheckProguardRule")
                    checkTask.doLast {
                        println("在 application 中，开始检测 proguard 文件配置规则")
                        Collection<File> list = variant.buildType.proguardFiles
                        StringBuilder sb = new StringBuilder()
                        if (list != null) {
                            for (File file : list) {
                                if (file.exists())
                                    sb.append(file.text)
                            }
                        }

                        analyseProguardText(sb.toString())
                    }
                    compileTask.dependsOn(checkTask)
                }
            }
        }
    }

    /**
     * 逐行做检查，看看有没有不符合规范的规则
     *
     * @param content
     */
    static void analyseProguardText(String content) throws GradleException {
        String[] lines = content.split("\n")
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i].trim()
            if (line == null || line.length() == 0 || line.startsWith("#")) {
                continue
            }

            if (line.matches(~"[\\s]*-ignorewarnings.*")) {
                throw new GradleException("违反 Proguard 规则：\n禁止使用 -ignorewarnings ")
            }
            if (line.matches(~"[\\s]*-dontshrink[\\s]*")) {
                throw new GradleException("违反 Proguard 规则：\n禁止使用 -dontshrink ")
            }

            //该选项如果开启，编译会很慢
            /*if (line.matches(~"[\\s]*-dontoptimize[\\s]*")) {
                throw new GradleException("违反 Proguard 规则：\n禁止使用 -dontoptimize ")
            }*/

            if (line.matches(~"[\\s]*-dontwarn[\\s]*\\*\\*[\\s]*")) {
                throw new GradleException("违反 Proguard 规则：\n禁止使用 -dontwarn ** ")
            }
            if (line.matches(~"[\\s]*-dontwarn[\\s]*(com|com\\.hm|com\\.hm\\.iou)\\.\\*\\*.*")) {
                throw new GradleException("违反 Proguard 规则：\n-dontwarn 错误，当前规则为: ${line}，禁止使用 ** 的包为 [com | com.hm | com.hm.iou]")
            }
            if (line.matches(~"[\\s]*-keep[\\s]*class[\\s]*\\*\\*[\\s]*")) {
                throw new GradleException("违反 Proguard 规则：\n-keep 错误，当前规则为: ${line}，禁止使用 **")
            }
            if (line.matches(~"[\\s]*-keep[\\s]*[public]*[\\s]*class[\\s]*(com|com\\.hm|com\\.hm\\.iou)\\.\\*\\*.*")) {
                throw new GradleException("违反 Proguard 规则：\n-keep 错误，当前规则为: ${line}，禁止使用 ** 的包为 [com | com.hm | com.hm.iou]")
            }

        }
    }
}