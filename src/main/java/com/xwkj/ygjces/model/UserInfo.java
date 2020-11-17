package com.xwkj.ygjces.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
public class UserInfo {
    @Getter
    @Setter
    private Long id;
    @Getter
    @Setter
    private String account;
    @Getter
    @Setter
    private String password;
    @Getter
    @Setter
    private String trueName;
    @Getter
    @Setter
    private String qyOpenId;
    @Getter
    @Setter
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date createTime;
    @Getter
    @Setter
    private Long createUserId;
    @Getter
    @Setter
    private Date lastModifiedTime;
    @Getter
    @Setter
    private Long lastModifier;
    @Getter
    @Setter
    private Integer enable;
    @Getter
    @Setter
    private List<RoleInfo> roleInfos;
    @Getter
    @Setter
    private String orgNames;
    @Getter
    @Setter
    private String mobile;
    @Getter
    @Setter
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startTime;
    @Getter
    @Setter
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endTime;
    @Getter
    @Setter
    private Integer isLeader;
    @Getter
    @Setter
    private String leaders;
    @Setter
    @Getter
    private Integer isApprovelPerson;

    public byte[] getCredentialsSalt(){
        return String.valueOf(this.account + password).getBytes();
    }
}
