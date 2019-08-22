package com.hm.iou.appconfig.plugin

import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.result.DependencyResult
import org.gradle.api.artifacts.result.ResolvedDependencyResult
import org.gradle.api.tasks.TaskAction

/**
 * 分析依赖树
 */

class AnalyseDependencyTask extends DefaultTask {

    Project target
    List dependencyList = new ArrayList()

    AnalyseDependencyTask() {
    }

    @TaskAction
    void action() {
        Configuration apiConf = target.configurations.getByName("api")

        apiConf.incoming.resolutionResult.root.dependencies.each { DependencyResult dr ->
            collectDependencyInfo(dr, null)
        }
        //打印出所有库的依赖图
        dependencyList.each { DependencyInfo dependencyInfo ->
            println(dependencyInfo)
        }
    }

    /**
     * 递归收集依赖信息
     *
     * @param dr
     * @param info
     */
    private void collectDependencyInfo(DependencyResult dr, DependencyInfo info) {
        DependencyInfo dependInfo = new DependencyInfo(dr.requested.displayName)
        if (info == null) {
            dependencyList.add(dependInfo)
        } else {
            info.addSubDependInfo(dependInfo)
            dependInfo.setLevel(info.getLevel() + 1)
        }
        if (dr instanceof ResolvedDependencyResult) {
            ((ResolvedDependencyResult) dr).selected.dependencies.each { DependencyResult subDr ->
                collectDependencyInfo(subDr, dependInfo)
            }
        }
    }

}