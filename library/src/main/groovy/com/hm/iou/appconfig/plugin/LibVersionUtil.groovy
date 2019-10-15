package com.hm.iou.appconfig.plugin

import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.ConfigurationContainer
import org.gradle.api.artifacts.ResolutionStrategy

/**
 * 统一第三方库的版本号
 */
class LibVersionUtil {

    static def SUPPORT_VERSION = "28.0.0"
    static def MULTIDEX_VERSION = "1.0.2"
    static def GSON_VERSION = "2.8.5"
    static def KOTLIN_VERSION = "1.3.50"

    static void checkLibVersion(Project project) {
        ConfigurationContainer container = project.configurations
        container.all { Configuration conf ->
            ResolutionStrategy rs = conf.resolutionStrategy
            rs.force 'com.google.code.findbugs:jsr305:2.0.1'
            //统一第三方库的版本号
            rs.eachDependency { details ->
                def requested = details.requested
                if (requested.group == "com.android.support") {
                    //强制所有的 com.android.support 库采用固定版本
                    if (requested.name.startsWith("multidex")) {
                        details.useVersion(MULTIDEX_VERSION)
                    } else {
                        details.useVersion(SUPPORT_VERSION)
                    }
                } else if (requested.group == "com.google.code.gson") {
                    //统一 Gson 库的版本号
                    details.useVersion(GSON_VERSION)
                } else if (requested.group == "org.jetbrains.kotlin") {
                    //统一内部 kotlin 库的版本
                    details.useVersion(KOTLIN_VERSION)
                } else if (requested.group == "com.heima.iou") {
                    //内部的库版本
                    details.useVersion("1.0.0")
                }
            }
        }
    }

}