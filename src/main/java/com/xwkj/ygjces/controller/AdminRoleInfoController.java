package com.xwkj.ygjces.controller;

import com.github.pagehelper.PageInfo;
import com.xwkj.ygjces.common.LoginUserInfoManager;
import com.xwkj.ygjces.common.ResponseResult;
import com.xwkj.ygjces.common.ResponseResultEnum;
import com.xwkj.ygjces.model.RoleInfo;
import com.xwkj.ygjces.model.UserInfo;
import com.xwkj.ygjces.service.RoleInfoService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
public class AdminRoleInfoController {
    private static Log log = LogFactory.getLog(AdminRoleInfoController.class);
    @Autowired
    private RoleInfoService roleInfoService;


    /**
     * 删除角色信息
     * @param id
     * @author wanglei
     */
    @PostMapping("/deleteRole")
    @RequiresPermissions("deleteRole")
    public ResponseResult deleteRoleInfo(Long id,HttpServletResponse response){
        log.info("删除角色");

        if (id == null ){
            log.error("删除角色失败!");
            return ResponseResult.failure(ResponseResultEnum.FAILURE.getCode(), "删除角色失败！");
        }
        //删除该角色表信息
        roleInfoService.deleteRoleInfo(id);
        return ResponseResult.success();
    }

    /**
     * 更新角色状态
     * @param
     * @author wanglei
     */
    @PostMapping("/updateRole")
    @RequiresPermissions("editRole")
    public ResponseResult updateRole(RoleInfo roleInfo, HttpServletResponse response){
        log.info("角色改变状态!");
        roleInfoService.updateRoleInfo(roleInfo);
        return ResponseResult.success();
    }

    /**
     * 编辑角色信息
     * @param roleInfo
     * @param response
     * @return
     */
    @PostMapping("/editRoleInfo")
    @RequiresPermissions("editRole")
    public ResponseResult editRoleInfo(@RequestBody RoleInfo roleInfo, HttpServletResponse response){
        if(roleInfo == null){
            log.error("编辑角色标识为空！");
            return ResponseResult.failure(ResponseResultEnum.FAILURE.getCode(), "编辑角色失败！");
        }
        roleInfo.setEnable(1);
        roleInfoService.updateRoleInfo(roleInfo);
        return ResponseResult.success();
    }

    /**
     * 去往编辑角色页面
     * @return
     * @author wanglei
     */
    @GetMapping("/toEditRoleInfo")
    @RequiresPermissions("editRole")
    public ModelAndView toEditRoleInfo(){
        return new ModelAndView("role-edit");
    }

    /**
     * 添加角色信息
     * @param roleInfo
     * @param response
     * @return
     */
    @PostMapping("/addRoleInfo")
    @RequiresPermissions("addRole")
    public ResponseResult addRoleInfo(@RequestBody RoleInfo roleInfo, HttpServletResponse response){
        log.info(roleInfo.toString());
        try {
            roleInfo.setCreateTime(new Date());
            UserInfo userInfo = LoginUserInfoManager.getUserInfo();
            roleInfo.setCreateUserId(userInfo.getId());
            roleInfo.setEnable(1);
            roleInfoService.saveRoleInfo(roleInfo);
        }catch (Exception e){
            log.error("保存角色信息失败！", e);
            return ResponseResult.failure(ResponseResultEnum.FAILURE.getCode(),"保存角色信息失败!");
        }
        return ResponseResult.success();
    }

    /**
     * 去往添加角色的页面
     * @return
     */
    @GetMapping("/toRoleInfoAdd")
    @RequiresPermissions("addRole")
    public ModelAndView toRoleInfoAdd(){
        return new ModelAndView("roleAdd");
    }

    /**
     * 获取角色分页列表
     * 角色是否启用必填
     * @param roleInfo
     * @param page
     * @param limit
     * @return
     */
    @PostMapping("/showRoleInfoPageList")
    public ResponseResult showRoleInfoPageList(String start ,String end , RoleInfo roleInfo, Integer page, Integer limit){
        try {
            roleInfo.setStartDate(start);
            roleInfo.setEndDate(end);
            PageInfo<RoleInfo> roleList = roleInfoService.getRoleInfoPage(roleInfo, page, limit);
            return ResponseResult.success(ResponseResultEnum.SUCCESS.getCode(), roleList, "获取角色分页列表成功");
        } catch (Exception e) {
            return ResponseResult.failure(ResponseResultEnum.FAILURE.getCode(),"角色查询失败!");
        }
    }

    /**
     * 去往角色展示列表页面
     * @return
     */
    @GetMapping("admin-role")
    @RequiresPermissions("showRoleInfoList")
    public ModelAndView toRoleInfoShowList(){
        return new ModelAndView("admin-role");
    }

    public RoleInfoService getRoleInfoService() {
        return roleInfoService;
    }

    public void setRoleInfoService(RoleInfoService roleInfoService) {
        this.roleInfoService = roleInfoService;
    }
}
