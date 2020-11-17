package com.xwkj.ygjces.model;

import lombok.Data;

@Data
public class RolePermission {
    private Long id;
    private Long roleId;
    private Long permId;
    private Boolean checked;

}
