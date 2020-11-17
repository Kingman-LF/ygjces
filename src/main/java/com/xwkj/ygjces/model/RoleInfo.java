package com.xwkj.ygjces.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class RoleInfo {
    @Setter
    @Getter
    private Long id;
    @Getter
    @Setter
    private String roleName;
    @Getter
    @Setter
    private String note;
    @Setter
    @Getter
    private Integer enable;
    @Getter
    @Setter
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date createTime;
    @Setter
    @Getter
    private Long createUserId;
    @Getter
    @Setter
    private Long[] permIds;
    @Getter
    @Setter
    private List<Permission> permissions;
    @Getter
    @Setter
    private String startDate;
    @Getter
    @Setter
    private String endDate;

   public List<RolePermission> getRolePermissionList(){
       List<RolePermission> list = null;
       if(permissions != null) {
           list = new ArrayList<>();
           for (Permission permission : this.getPermissions()) {
               RolePermission rp = new RolePermission();
               rp.setChecked(Boolean.TRUE);
               rp.setPermId(permission.getId());
               rp.setRoleId(this.getId());
               list.add(rp);
           }
       }
       return list;
   }
}
