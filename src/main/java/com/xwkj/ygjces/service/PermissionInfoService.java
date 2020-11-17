package com.xwkj.ygjces.service;

import com.xwkj.ygjces.controller.vo.PermissionVo;
import com.xwkj.ygjces.model.Permission;

import java.util.List;

public interface PermissionInfoService {
    /**
     * 返回权限树
     * @param permission
     * @return
     */
//    public List<PermissionVo> getPermissionInfoVoShowList(Permission permission);

    /**
     * 业务层返回权限树接口
     * @param permission id 添加功能返回所有权限 编辑根据角色ID返回该角色对应的权限
     * @return
     * @author wanglei
     */
    public List<Permission> getPermissionInfoVoList(Permission permission);
}
