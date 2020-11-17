package com.xwkj.ygjces.controller;

import com.xwkj.ygjces.common.ResponseResult;
import com.xwkj.ygjces.common.ResponseResultEnum;
import com.xwkj.ygjces.model.OrganizationInfo;
import com.xwkj.ygjces.service.OrganizationInfoService;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 组织机构控制层
 * @author zyh
 */
@Controller
public class OrganizationInfoController {

    private static Log log = LogFactory.getLog(OrganizationInfoController.class);

    @Autowired
    OrganizationInfoService organizationInfoService;

    /**
     * 去往组织结构页面
     * @return
     * @author zyh
     */
    @GetMapping("/toOrganizationPage")
    @RequiresPermissions("showDepartment")
    public String toOrganizationPage(){
        return "organization/organization-list";
    }

    /**
     *去往添加组织结构页面
     * @return
     * @author zyh
     */
    @GetMapping("/toAddOrganizationPage")
    @RequiresPermissions("addDepartment")
    public String toAddOrganizationPage(){
        return "organization/addOrganization";
    }

    /**
     * 去往修改组织结构页面
     * @return
     * @author zyh
     */
    @GetMapping("/toEditOrganizationPage")
    @RequiresPermissions("updateDepartment")
    public String toEditOrganizationPage(){
        return "organization/editOrganization";
    }


    /**
     * 去往向指定组织结构中添加人员的页面
     * @return
     * @author zyh
     */
    @GetMapping("/toAddUserToOrgPage")
    @RequiresPermissions("addUserDepartment")
    public String toAddUserToOrgPage(){
        return "organization/addUserToOrg";
    }


    /**
     * 去往修改指定人员所属组织结构的页面
     * @return
     * @author zyh
     */
    @GetMapping("/toEditUserFromOrgPage")
    @RequiresPermissions("updateUserDepartment")
    public String toEditUserFromOrgPage(){
        return "organization/editUserFromOrg";
    }

    /**
     * 获取可用的组织机构树形列表
     * 前端调用接口<code>getOrganziationInfoList</code>
     * @return 结果封装类ResponseResult
     * @author zyh
     */
    @PostMapping("/getOrganziationInfoList")
    @RequiresPermissions(value = {"showDepartment","addAdmin","updateAdmin"},logical = Logical.OR)
    @ResponseBody
    public ResponseResult getOrganziationInfoList() {
        ResponseResult responseResult = null;
        try {
            List<OrganizationInfo> list = organizationInfoService.getOrganizationInfoList();
            log.info("组织机构列表获取成功");
            responseResult=ResponseResult.success(ResponseResultEnum.SUCCESS.getCode(),list,"组织机构列表获取成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("组织机构列表获取失败");
            responseResult = ResponseResult.failure("组织机构列表获取失败");
        }
        return responseResult;
    }

    /**
     * 通过ID获取组织机构信息
     * 前端调用接口<code>getOrganizationInfoById</code>
     * @param id 组织机构主键
     * @return 结果封装类ResponseResult
     * @author zyh
     */
    @PostMapping("/getOrganizationInfoById")
    @ResponseBody
    public ResponseResult getOrganizationInfoById(Long id){
        ResponseResult responseResult = null;
        try {
            OrganizationInfo organizationInfo = organizationInfoService.getOrganizationInfoById(id);
            log.info("组织机构获取成功");
            responseResult=ResponseResult.success(ResponseResultEnum.SUCCESS.getCode(),organizationInfo,"组织机构获取成功");
        } catch(Exception e){
            e.printStackTrace();
            log.error("组织机构获取失败");
            responseResult=ResponseResult.failure("组织机构获取失败");

        }
        return responseResult;
    }

    /**
     * 根据ID删除组织机构信息
     * 前端调用接口<code>deleteOrganizationInfoById</code>
     * @param idList 当前组机构机构及其子组织机构的主键数组
     * @return 结果封装类ResponseResult
     * @author zyh
     */
    @PostMapping("/deleteOrganizationInfoById")
    @RequiresPermissions("deleteDepartment")
    @ResponseBody
    public ResponseResult deleteOrganizationInfoById(String idList){
        ResponseResult responseResult=null;
        try {
            String[] ids = idList.split(",");
            boolean b = organizationInfoService.deleteOrganizationInfoById(ids);
            if (b){
                log.info("删除组织机构信息成功");
                responseResult=ResponseResult.success(ResponseResultEnum.SUCCESS.getCode(),null,"删除组织机构信息成功");
            }else {
                log.error("无法删除组织结构，组织结构或子组织结构下有人员");
                responseResult=ResponseResult.failure("无法删除组织结构，组织结构或子组织结构下有人员");
            }

        } catch(Exception e){
            e.printStackTrace();
            log.error("删除组织机构信息失败");
            responseResult=ResponseResult.failure("删除组织机构信息失败");
        }
        return responseResult;
    }

    /**
     * 插入组织结构信息并返回插入信息的主键
     * 前端调用接口<code>insertOrganizationInfo</code>
     * @param organizationInfo 组织机构信息
     * @return 结果封装类ResponseResult
     * @author zyh
     */
    @PostMapping("/insertOrganizationInfo")
    @ResponseBody
    public ResponseResult insertOrganizationInfo(OrganizationInfo organizationInfo){
        ResponseResult responseResult=null;
        try {
            Long id = organizationInfoService.insertOrganizationInfo(organizationInfo);
            log.info("组织结构信息插入成功");
            responseResult = ResponseResult.success(ResponseResultEnum.SUCCESS.getCode(),id,"组织结构信息插入成功");
        } catch(Exception e){
            e.printStackTrace();
            log.error("组织结构信息插入失败");
            responseResult = ResponseResult.failure("组织结构信息插入失败");
        }
        return  responseResult;
    }

    /**
     * 修改组织机构信息
     * 前端调用接口<code>updateOrganizationInfo</code>
     * @param organizationInfo 组织机构信息
     * @return 结果封装类ResponseResult
     * @author zyh
     */
    @PostMapping("/updateOrganizationInfo")
    @RequiresPermissions("updateDepartment")
    @ResponseBody
    public ResponseResult updateOrganizationInfo(OrganizationInfo organizationInfo){
        ResponseResult responseResult=null;
        try {
            organizationInfoService.updateOrganizationInfo(organizationInfo);
            log.info("修改组织机构信息成功");
            responseResult=ResponseResult.success(ResponseResultEnum.SUCCESS.getCode(),null,"修改组织机构信息成功");
        } catch(Exception e){
            e.printStackTrace();
            log.error("修改组织机构信息失败");
            responseResult=ResponseResult.failure("修改组织机构信息失败");
        }
        return responseResult;
    }

    /**
     * 根据用户id获取组织结构信息
     * 前端调用接口<code>getOrganizationInfoByUid</code>
     * @param id 用户id
     * @return 结果封装类ResponseResult
     * @author zyh
     */
    @PostMapping("/getOrganizationInfoByUid")
    @ResponseBody
    public ResponseResult getOrganizationInfoByUid(Long id){
        ResponseResult responseResult = null;
        try {
            List<OrganizationInfo> list = organizationInfoService.getOrganizationInfoByUid(id);
            log.info("获取组织结构信息成功");
            responseResult = ResponseResult.success(ResponseResultEnum.SUCCESS.getCode(),list,"获取组织结构信息成功");
        } catch(Exception e){
            e.printStackTrace();
            log.error("获取组织结构信息失败");
            responseResult = ResponseResult.failure("获取组织结构信息失败");
        }
        return responseResult;
    }

    /**
     * 给组织机构添加领导
     * @param uId
     * @param oId
     * @author zyh
     */
    @PostMapping("/addLeaderToOrganization")
    @RequiresPermissions("setDepartmentLeader")
    @ResponseBody
    public ResponseResult addLeaderToOrganization(Long uId,Long oId){
        ResponseResult responseResult = null;
        try {
            organizationInfoService.addLeaderToOrganization(uId,oId);
            log.info("设置上级成功");
            responseResult = ResponseResult.success(ResponseResultEnum.SUCCESS.getCode(),null,"设置上级成功");
        } catch(Exception e){
            e.printStackTrace();
            log.error("设置上级失败");
            responseResult = ResponseResult.failure("设置上级失败");
        }
        return responseResult;
    }

    /**
     * 给组织机构移除领导
     * @param uId
     * @param oId
     * @author zyh
     */
    @PostMapping("/removeLeaderFromOrganization")
    @RequiresPermissions("removeDepartmentLeader")
    @ResponseBody
    public ResponseResult removeLeaderFromOrganization(Long uId,Long oId){
        ResponseResult responseResult = null;
        try {
            organizationInfoService.removeLeaderFromOrganization(uId,oId);
            log.info("移除上级成功");
            responseResult = ResponseResult.success(ResponseResultEnum.SUCCESS.getCode(),null,"移除上级成功");
        } catch(Exception e){
            e.printStackTrace();
            log.info("移除上级失败");
            responseResult = ResponseResult.failure("移除上级失败");
        }
        return responseResult;
    }
}
