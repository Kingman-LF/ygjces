package com.xwkj.ygjces.model;

import me.chanjar.weixin.cp.bean.WxCpUser;

public class MyWxCpUser extends WxCpUser {
    public Integer[] getIs_leader_in_dept() {
        return is_leader_in_dept;
    }

    public void setIs_leader_in_dept(Integer[] is_leader_in_dept) {
        this.is_leader_in_dept = is_leader_in_dept;
    }

    private Integer[] is_leader_in_dept;

}
