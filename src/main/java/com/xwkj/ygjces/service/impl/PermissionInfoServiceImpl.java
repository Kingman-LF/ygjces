package com.xwkj.ygjces.service.impl;

import com.xwkj.ygjces.Mapper.PermissionMapper;
import com.xwkj.ygjces.Mapper.RoleInfoMapper;
import com.xwkj.ygjces.Mapper.RolePermissionMapper;
import com.xwkj.ygjces.controller.vo.PermissionVo;
import com.xwkj.ygjces.model.Permission;
import com.xwkj.ygjces.model.RolePermission;
import com.xwkj.ygjces.model.UserInfo;
import com.xwkj.ygjces.service.PermissionInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.management.relation.Role;
import java.util.List;

@Service
public class PermissionInfoServiceImpl implements PermissionInfoService {
    @Autowired
    private PermissionMapper permissionMapper;

    @Autowired
    private RoleInfoMapper roleInfoMapper;

    @Autowired
    private RolePermissionMapper rolePermissionMapper;
    /**
     * 权限树查询
     * @param permission id 添加功能返回所有权限 编辑根据角色ID返回该角色对应的权限
     * @author wanglei
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Permission> getPermissionInfoVoList(Permission permission){
        //查询权限表所有的权限
        List<Permission> Permission=  permissionMapper.getPermissionList(permission);
        if(null != permission && permission.getId()!=null){
            //从权限角色中间表查询角色权限
            List<RolePermission> RolePermission=rolePermissionMapper.findRolePermission(permission.getId());
            for (int i =0 ; i<RolePermission.size();i++){
                for(int j =0 ; j<Permission.size();j++){
                    if(RolePermission.get(i).getPermId().equals(Permission.get(j).getId())){
                        Permission.get(j).setChecked(true);
                    }
                }
            }
        }
        return Permission;
    }

    /**
     * 权限树查询
     * @param
     * @return
     *
     */
//    @Override
//    public List<PermissionVo> getPermissionInfoVoShowList(Permission permission){
//        List<PermissionVo> permissionVoList = permissionMapper.getPermissionVoList(permission);
//        return permissionVoList;
//    }

    public List<PermissionVo> getPermissionInfoVoShowList(UserInfo userInfo){
        return null;
    }

}
