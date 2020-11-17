package com.xwkj.ygjces.controller;

import com.xwkj.ygjces.common.ResponseResult;
import com.xwkj.ygjces.common.ResponseResultEnum;
import com.xwkj.ygjces.controller.vo.PermissionVo;
import com.xwkj.ygjces.model.Permission;
import com.xwkj.ygjces.model.RolePermission;
import com.xwkj.ygjces.service.PermissionInfoService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PermissionInfoController {

    private static Log log = LogFactory.getLog(PermissionInfoController.class);

   @Autowired
    private PermissionInfoService permissionInfoService;

    /**
     * 添加编辑返回权限树接口
     * @param permission id 添加功能返回所有权限 编辑根据角色ID返回该角色对应的权限
     * @return
     */

    @PostMapping("/selectPermissionInfoList")
    public ResponseResult selectPermissionInfoList(Permission permission){
        log.info("查询显示权限树信息!");
        List<Permission> permissionInfoList = permissionInfoService.getPermissionInfoVoList(permission);
        if(permissionInfoList == null){
            log.error("返回权限树失败!");
            return ResponseResult.failure("查询权限树失败!");
        }
        return ResponseResult.success(ResponseResultEnum.SUCCESS.getCode(),permissionInfoList,"返回权限成功!");
    }
}
