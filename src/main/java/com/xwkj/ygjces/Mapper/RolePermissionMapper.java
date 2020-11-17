package com.xwkj.ygjces.Mapper;

import com.xwkj.ygjces.model.Permission;
import com.xwkj.ygjces.model.RoleInfo;
import com.xwkj.ygjces.model.RolePermission;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface RolePermissionMapper {

    /**
     * 根据角色查询权限
     * @param roleId
     * @return
     */
    public List<Permission> findPermissionByRole(Long roleId);
    /**
     * 通过roleId删除
     * @param roleId
     * @author wanglei
     */
    public void deleteRolePermission(Long roleId);

    /**
     * 保存角色权限
     * @param roleInfo
     * @author wanglei
     */
    public void saveRolePermission(RoleInfo roleInfo);

    /**
     *通过roleId查询角色权限
     * @param roleId
     * @return
     * @author wanglei
     */
    public List<RolePermission> findRolePermission(Long roleId);
}
