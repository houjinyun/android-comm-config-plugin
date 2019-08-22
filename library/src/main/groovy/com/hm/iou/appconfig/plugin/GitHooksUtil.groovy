package com.hm.iou.appconfig.plugin

import org.gradle.api.Project

/**
 * 为 git 自动添加 git hooks
 */
class GitHooksUtil {

    /**
     * 对 git 提交信息提交时进行检测，采用 groovy 脚本来运行，需要支持 groovy 运行环境
     * TODO 改成bash脚本，运行效率更好
     */
    private static final String GIT_COMMIT_MSG_CONFIG = '''#!/usr/bin/env groovy
import static java.lang.System.exit

//要提交的信息保存在该文件里
def commitMsgFileName = args[0]
def msgFile = new File(commitMsgFileName)
//读出里面的提交信息
def commitMsg = msgFile.text

//对要提交的信息做校验，如果不符合要求的，不允许提交
def reg = ~"^(fix:|add:|update:|refactor:|perf:|style:|test:|docs:|revert:|build:)[\\\\w\\\\W]{5,100}"
if (!commitMsg.matches(reg)) {
    StringBuilder sb = new StringBuilder()
    sb.append("================= Commit Error =================\\n")
    sb.append("===>Commit 信息不规范，描述信息字数范围为[5, 100]，具体格式请按照以下规范：\\n")
    sb.append("    fix: 修复某某bug\\n")
    sb.append("    add: 增加了新功能\\n")
    sb.append("    update: 更新某某功能\\n")
    sb.append("    refactor: 某个已有功能重构\\n")
    sb.append("    perf: 性能优化\\n")
    sb.append("    style: 代码格式改变\\n")
    sb.append("    test: 增加测试代码\\n")
    sb.append("    docs: 文档改变\\n")
    sb.append("    revert: 撤销上一次的commit\\n")
    sb.append("    build: 构建工具或构建过程等的变动\\n")
    sb.append("================================================")
    println(sb.toString())
    exit(1)    
}

exit(0)
'''

    /**
     * 检查 git hook 文件
     *
     * @param project
     */
    static void checkGitHookFile(Project project) throws IOException {
        //在根目录的 .git/hooks 目录下，存在很多 .sample 文件，把相应的 .sample 后缀去掉，git hook 就生效了
        File rootDir = project.rootProject.getProjectDir()
        File gitHookDir = new File(rootDir, ".git/hooks")

        //如果该目录存在
        if (gitHookDir.exists()) {
            //将 commit-msg.sample 文件的后缀名去掉，git hook 就会生效
            File commitMsgSampleFile = new File(gitHookDir, "commit-msg.sample")
            File commitMsgFile = new File(gitHookDir, "commit-msg")
            if (!commitMsgFile.exists() && commitMsgSampleFile.exists()) {
                //重命名的方式，自己创建的文件可能没有可执行权限，需要手动加权限，故采用重命名原文件的方式，省去手动加权限的操作
                commitMsgSampleFile.renameTo(commitMsgFile)
                commitMsgFile.setText(GIT_COMMIT_MSG_CONFIG)
                println("-----自动配置 git hook 成功-----")
            } else {
                println("-----git hook 已经启用-----")
            }
        } else {
            println("-----没有找到.git目录----")
        }
    }

}