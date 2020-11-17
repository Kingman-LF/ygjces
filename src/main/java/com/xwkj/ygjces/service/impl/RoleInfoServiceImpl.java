package com.xwkj.ygjces.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xwkj.ygjces.Mapper.RoleInfoMapper;
import com.xwkj.ygjces.Mapper.RolePermissionMapper;
import com.xwkj.ygjces.model.Permission;
import com.xwkj.ygjces.model.RoleInfo;
import com.xwkj.ygjces.model.RolePermission;
import com.xwkj.ygjces.service.RoleInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class RoleInfoServiceImpl implements RoleInfoService {
    @Autowired
    private RoleInfoMapper roleInfoMapper;
    @Autowired
    private RolePermissionMapper rolePermissionMapper;

    /**
     * 根据角色id查询权限
     * @param roleId
     * @return
     * @author wanglei
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Permission> findPermissionByRoleId(Long roleId){
        return rolePermissionMapper.findPermissionByRole(roleId);
    }

    /**
     *删除角色信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRoleInfo(Long id){
        //删除角色表信息
        roleInfoMapper.deleteRoleInfoById(id);
        //删除角色权限关联表信息
        rolePermissionMapper.deleteRolePermission(id);
    };

    /**
     * 修改角色和角色权限中间表信息
     * @param roleInfo
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRoleInfo(RoleInfo roleInfo) {
        //更新角色信息
        roleInfoMapper.updateRoleInfo(roleInfo);
        if (roleInfo.getPermIds() != null){
            //删除角色权限中间表信息
            rolePermissionMapper.deleteRolePermission(roleInfo.getId());
            //重新保持角色权限中间表信息
            rolePermissionMapper.saveRolePermission(roleInfo);
        }
    }

    /**
     * 序列化角色和权限信息
     * @param roleInfo
     * @author wanglei
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRoleInfo(RoleInfo roleInfo) {
        //保存到角色表
        roleInfoMapper.saveRoleInfo(roleInfo);
        //保存到角色权限关系表
        rolePermissionMapper.saveRolePermission(roleInfo);
    }

    /**
     * 获取角色分页列表
     * 是否启用必填
     * @param roleInfo
     * @param page
     * @param limit
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public PageInfo<RoleInfo> getRoleInfoPage(RoleInfo roleInfo, Integer page, Integer limit){
        PageHelper.startPage(page -1, limit);
        List<RoleInfo> list = roleInfoMapper.getRoleInfoList(roleInfo);
        return new PageInfo<RoleInfo>(list);
    }

    public RoleInfoMapper getRoleInfoMapper() {
        return roleInfoMapper;
    }

    public void setRoleInfoMapper(RoleInfoMapper roleInfoMapper) {
        this.roleInfoMapper = roleInfoMapper;
    }
}
