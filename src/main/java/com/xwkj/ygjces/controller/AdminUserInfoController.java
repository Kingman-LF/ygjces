package com.xwkj.ygjces.controller;

import com.github.pagehelper.PageInfo;
import com.xwkj.ygjces.common.ResponseResult;
import com.xwkj.ygjces.common.ResponseResultEnum;
import com.xwkj.ygjces.model.OrganizationInfo;
import com.xwkj.ygjces.model.RoleInfo;
import com.xwkj.ygjces.model.UserInfo;
import com.xwkj.ygjces.service.UserInfoService;
import org.apache.catalina.User;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.config.TxNamespaceHandler;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 用户控制层数据
 */
@RestController
public class AdminUserInfoController {

    private static Log log = LogFactory.getLog(AdminUserInfoController.class);

    @Autowired
    private UserInfoService userInfoService;



    /**
     * 去往添加用户页面
     * @return
     */
    @GetMapping("/admin-add")
    @RequiresPermissions("addAdmin")
    public ModelAndView toAddUserInfo(Map<String ,Object> map){
        List<RoleInfo> roleInfoList = userInfoService.getRoleInfoList();
        map.put("roleInfoList",roleInfoList);
        return new ModelAndView("admin-add");
    }

    /**
     * 去往修改用户界面
     * @param map
     * @return
     */
    @GetMapping("/toEditAdminPage")
    @RequiresPermissions("updateAdmin")
    public ModelAndView toEditAdminPage(Map<String ,Object> map){
        List<RoleInfo> roleInfoList = userInfoService.getRoleInfoList();
        map.put("roleInfoList",roleInfoList);
        return new ModelAndView("admin-edit");
    }

    /**
     * 去往修改用户页面
     * @return
     */
    @GetMapping("/admin-edit")
    public ModelAndView toEditUserInfo(){
        return new ModelAndView("admin-edit");
    }


    /**
     * 获取用户列表可以通过用户名账号用户企业微信openId获取用户列表
     * 前端调用接口<code>showUserInfoList</code>
     * @param userInfo 用户信息
     * @param page  页数
     * @param limit 页面大小
     * @return 结果封装类ResponseResult
     */
    @PostMapping("/showUserInfoList")
    @RequiresPermissions("showAdmin")
    public ResponseResult showUserInfoList(UserInfo userInfo, Integer page, Integer limit){
        if(page == null){
            page = 1;
        }
        if(limit == null){
            limit = 10;
        }
        PageInfo<UserInfo> userInfos = userInfoService.getUserInfoList(userInfo, page, limit);
        return ResponseResult.success(ResponseResultEnum.SUCCESS.getCode(), userInfos, "获取用户列表成功！");
    }

    /**
     * 去往用户展示列表页面
     * @return
     */
    @GetMapping("/admin-list")
    @RequiresPermissions("showAdmin")
    public ModelAndView toUserInfoShowList(){
        return new ModelAndView("admin-list");
    }


    /**
     * 通过组织机构ID或用户真实姓名查询对应的用户列表
     * 前端调用接口<code>getUserInfoByOrganizationId</code>
     * @param organizationInfo 组织机构信息
     * @param tName 用户真实姓名
     * @param pageNum 页数
     * @param pageSize 页面大小
     * @return 结果封装类ResponseResult
     * @author zyh
     */
    @PostMapping("/getUserInfoByOrganizationId")
    public ResponseResult getUserInfoByOrganizationId(OrganizationInfo organizationInfo,String tName , Integer pageNum, Integer pageSize){
        ResponseResult responseResult = null;
        PageInfo<UserInfo> pageInfo = null;
        if(pageNum == null){
            pageNum = 1;
        }
        if(pageSize == null){
            pageSize = 10;
        }
        try {
            if (tName==null){
                pageInfo = userInfoService.getUserInfoByOrganizationId(organizationInfo, pageNum, pageSize);
            }else {
                pageInfo = userInfoService.getUserInfoByTrueName(tName,pageNum,pageSize);
            }
            log.info("获取对应用户列表成功");
            responseResult = ResponseResult.success(ResponseResultEnum.SUCCESS.getCode(),pageInfo,"获取对应用户列表成功");
        } catch(Exception e){
            log.error("获取对应用户列表失败");
            e.printStackTrace();
            responseResult = ResponseResult.failure("获取对应用户列表失败");
        }
        return responseResult;
    }

    /**
     * 根据组织机构id查询不在当前点击的组织机构内的用户
     * 前端调用接口<code>getNotInThisOrgUserInfoByOrganizationId</code>
     * @param tid 组织机构id
     * @return 结果封装类ResponseResult
     * @author zyh
     */
    @PostMapping("/getNotInThisOrgUserInfoByOrganizationId")
    @ResponseBody
    public ResponseResult getNotInThisOrgUserInfoByOrganizationId(Long tid){
        ResponseResult responseResult = null;
        try {
            List<UserInfo> list = userInfoService.getNotInThisOrgUserInfoByOrganizationId(tid);
            log.info("获取用户列表成功");
            responseResult = ResponseResult.success(ResponseResultEnum.SUCCESS.getCode(),list,"获取用户列表成功");
        } catch(Exception e){
            e.printStackTrace();
            log.error("获取用户列表失败");
            responseResult = ResponseResult.failure("获取用户列表失败");
        }
        return responseResult;
    }

    /**
     * 给组织机构添加用户
     * 前端调用接口<code>addUserToOrg</code>
     * @param tId 组织机构主键
     * @param users 用户主键
     * @return 结果封装类ResponseResult
     * @author zyh
     */
    @PostMapping("/addUserToOrg")
    @RequiresPermissions("addUserDepartment")
    @ResponseBody
    public ResponseResult addUserToOrg(Long tId , Long users){
        ResponseResult responseResult = null;
        try {
            userInfoService.addUserToOrg(tId,users);
            log.info("给用户添加组织机构信息成功");
            responseResult = ResponseResult.success(ResponseResultEnum.SUCCESS.getCode(),null,"给用户添加组织机构信息成功");
        } catch(Exception e){
            e.printStackTrace();
            log.error("给用户添加组织机构信息失败");
            responseResult = ResponseResult.failure("给用户添加组织机构信息失败");
        }
        return responseResult;
    }

    /**
     * 修改用户所属的组织机构
     * 前端调用接口<code>updateUserFromOrg</code>
     * @param orgIds 用户所属的所有组织机构的id的数组
     * @param userId 用户主键
     * @return 结果封装类ResponseResult
     * @author zyh
     */
    @PostMapping("updateUserFromOrg")
    @RequiresPermissions("updateUserDepartment")
    @ResponseBody
    public ResponseResult updateUserFromOrg(Long userId , String orgIds){
        ResponseResult responseResult = null;
        try {
            userInfoService.updateUserFromOrg(orgIds,userId);
            log.info("修改用户所属的组织机构成功");
            responseResult = ResponseResult.success(ResponseResultEnum.SUCCESS.getCode(),null,"修改用户所属的组织机构成功");
        } catch(Exception e){
            e.printStackTrace();
            log.error("修改用户所属的组织机构失败");
            responseResult = ResponseResult.failure("修改用户所属的组织机构失败");
        }
        return responseResult;
    }

    /**
     * 添加用户
     * 前端调用接口<code>addAdmin</code>
     * @param userInfo 用户信息
     * @param roles 选中的角色id数组
     * @param orgIds 组织结构主键组成的字符串
     * @return 结果封装类ResponseResult
     * @author zyh
     */
    @PostMapping("/addAdmin")
    @RequiresPermissions("addAdmin")
    @ResponseBody
    public ResponseResult addAdmin(UserInfo userInfo,@RequestParam("roles") String[] roles,String orgIds){
        ResponseResult responseResult = null;
        try {
            int i = userInfoService.addAdmin(userInfo, roles, orgIds);
            if(i==1){
                log.info("添加用户成功");
                responseResult = ResponseResult.success(ResponseResultEnum.SUCCESS.getCode(),null,"添加用户成功");
            }else {
                log.error("添加失败，用户名重复或手机号重复");
                responseResult = ResponseResult.failure("添加失败，用户名重复或手机号重复");
            }

        } catch(Exception e){
            e.printStackTrace();
            log.error("添加用户失败");
            responseResult = ResponseResult.failure("添加用户失败");
        }
        return responseResult;
    }

    /**
     * 根据id查询指定的用户信息
     * 前端调用接口<code>getUserInfoById</code>
     * @param id 用户主键
     * @return 结果封装类ResponseResult
     * @author zyh
     */
    @PostMapping("/getUserInfoById")
    @ResponseBody
    public ResponseResult getUserInfoById(Long id){
        ResponseResult responseResult = null;
        try {
            ArrayList<Object> list = userInfoService.getUserInfoById(id);
            log.info("根据id查询指定的用户信息成功");
            responseResult = ResponseResult.success(ResponseResultEnum.SUCCESS.getCode(),list,"根据id查询指定的用户信息成功");
        } catch(Exception e){
            e.printStackTrace();
            log.error("根据id查询指定的用户信息失败");
            responseResult = ResponseResult.failure("根据id查询指定的用户信息失败");
        }
        return responseResult;
    }

    /**
     * 修改用户
     * 前端调用接口<code>editAdmin</code>
     * @param userInfo 用户信息
     * @param roles 选中的角色id数组
     * @param orgIds 组织结构主键组成的字符串
     * @return 结果封装类ResponseResult
     * @author zyh
     */
    @PostMapping("/editAdmin")
    @RequiresPermissions("updateAdmin")
    @ResponseBody
    public ResponseResult updateUserInfo(UserInfo userInfo, @RequestParam("roles") String[] roles,String orgIds){
        ResponseResult responseResult = null;
        try {
            int i = userInfoService.updateUserInfo(userInfo, roles, orgIds);
            if(i==1){
                log.info("修改用户成功");
                responseResult = ResponseResult.success(ResponseResultEnum.SUCCESS.getCode(),null,"修改用户成功");
            }else {
                log.error("手机号已被占用，修改用户失败");
                responseResult = ResponseResult.failure("手机号已被占用，修改用户失败");
            }
        } catch(Exception e){
            e.printStackTrace();
            log.error("修改用户失败");
            responseResult = ResponseResult.failure("修改用户失败");
        }
        return responseResult;
    }

    /**
     * 修改用户启用状态
     * 前端调用接口<code>updateUserEnable</code>
     * @param userInfo 用户信息
     * @return 结果封装类ResponseResult
     * @author zyh
     */
    @PostMapping("/updateUserEnable")
    @RequiresPermissions("updateAdmin")
    @ResponseBody
    public ResponseResult updateUserEnable(UserInfo userInfo){
        ResponseResult responseResult = null;
        try {
            userInfoService.updateUserEnable(userInfo);
            log.info("修改用户启用状态成功");
            responseResult = ResponseResult.success(ResponseResultEnum.SUCCESS.getCode(),null,"修改用户启用状态成功");
        } catch(Exception e){
            e.printStackTrace();
            log.error("修改用户启用状态失败");
            responseResult = ResponseResult.failure("修改用户启用状态失败");
        }
        return responseResult;
    }

    /**
     * 重置用户密码，默认为123456
     * @param id
     * @author zyh
     */
    @PostMapping("/resetPassword")
    @RequiresPermissions("updateAdmin")
    @ResponseBody
    public ResponseResult resetPassword(Long id){
        ResponseResult responseResult = null;
        try {
            userInfoService.resetPassword(id);
            log.info("重置密码成功");
            responseResult = ResponseResult.success(ResponseResultEnum.SUCCESS.getCode(),null,"重置密码成功");
        } catch(Exception e){
            e.printStackTrace();
            log.error("重置密码失败");
            responseResult = ResponseResult.failure("重置密码失败");
        }
        return responseResult;
    }
}
