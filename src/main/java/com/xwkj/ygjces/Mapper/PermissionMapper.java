package com.xwkj.ygjces.Mapper;

import com.xwkj.ygjces.controller.vo.PermissionVo;
import com.xwkj.ygjces.model.Permission;
import com.xwkj.ygjces.model.RoleInfo;
import com.xwkj.ygjces.model.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface PermissionMapper {

    /**
     * 获取用户的有的权限树形列表
     * @param roleInfo
     * @return
     */
//    public List<PermissionVo> getPermissionVoListByUserInfo(RoleInfo roleInfo);

    /**
     * 返回权限树
     * @return
     */
//    public List<PermissionVo> getPermissionVoList(Permission permission);

    /**
     * 返回权限树
     * @param permission id 添加功能返回所有权限 编辑根据角色ID返回该角色对应的权限
     * @return
     * @author wanglei
     */
    public List<Permission> getPermissionList(Permission permission);
}
