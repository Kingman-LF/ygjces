package com.xwkj.ygjces.model;

/**
 * 绩效实体类
 * @author zyh
 */
public class Achievements {
    private Long id;
    private String orgName;
    private String leaderName;
    private String subordinateName;
    private String completionRate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getLeaderName() {
        return leaderName;
    }

    public void setLeaderName(String leaderName) {
        this.leaderName = leaderName;
    }

    public String getSubordinateName() {
        return subordinateName;
    }

    public void setSubordinateName(String subordinateName) {
        this.subordinateName = subordinateName;
    }

    public String getCompletionRate() {
        return completionRate;
    }

    public void setCompletionRate(String completionRate) {
        this.completionRate = completionRate;
    }
}
