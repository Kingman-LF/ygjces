package com.xwkj.ygjces.service;

import com.github.pagehelper.PageInfo;
import com.xwkj.ygjces.model.Permission;
import com.xwkj.ygjces.model.RoleInfo;

import java.util.Date;
import java.util.List;

/**
 * 获取角色服务层信息
 */
public interface RoleInfoService {

    /**
     * 根据角色id查询权限
     * @param roleId
     * @return
     * @author wanglei
     */
    public List<Permission> findPermissionByRoleId(Long roleId);

    /**
     * 删除角色信息
     * @param id
     * @author wanglei
     */
    public void deleteRoleInfo(Long id);

    /**
     * 修改角色信息失败
     * @param roleInfo
     */
    public void updateRoleInfo(RoleInfo roleInfo);

    /**
     * 保存角色
     * @param roleInfo
     */
    public void saveRoleInfo(RoleInfo roleInfo);

    /**
     * 获取角色分页列表
     * 是否启用必填
     * @param roleInfo
     * @param page
     * @param limit
     * @return
     */
    public PageInfo<RoleInfo> getRoleInfoPage(RoleInfo roleInfo, Integer page, Integer limit);
}
