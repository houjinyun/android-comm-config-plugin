package com.hm.iou.appconfig.plugin

/**
 * 依赖树
 */
class DependencyInfo {

    private String displayName
    private List<DependencyInfo> subDependInfoList
    private int level = 0   //表示依赖的深度，顶层的依赖为0，次一级的依次递归+1

    DependencyInfo(String displayName) {
        this.displayName = displayName
    }

    void addSubDependInfo(DependencyInfo dependInfo) {
        if (subDependInfoList == null)
            subDependInfoList = new ArrayList<>()
        subDependInfoList.add(dependInfo)
    }

    void setLevel(int level) {
        this.level = level
    }

    int getLevel() {
        return level
    }

    @Override
    String toString() {
        StringBuilder sb = new StringBuilder()
        for (int i = 0; i < level; i++) {
            sb.append("|---")
        }
        sb.append(displayName).append("\n")
        if (subDependInfoList != null && !subDependInfoList.isEmpty()) {
            for (DependencyInfo dr : subDependInfoList) {
                sb.append(dr.toString())
            }
        }
        return sb.toString()
    }
}
