package com.xwkj.ygjces.controller;

import com.github.pagehelper.PageInfo;
import com.xwkj.ygjces.common.ResponseResult;
import com.xwkj.ygjces.common.ResponseResultEnum;
import com.xwkj.ygjces.model.Achievements;
import com.xwkj.ygjces.model.OrgItemIntermediate;
import com.xwkj.ygjces.model.SampleInfo;
import com.xwkj.ygjces.service.OrgAndItemService;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.awt.*;
import java.util.Date;
import java.util.List;

/**
 * 组织机构与检测项控制层
 * @author zyh
 */
@Controller
public class OrgAndItemController {

    private static Log log = LogFactory.getLog(OrgAndItemController.class);

    @Autowired
    OrgAndItemService orgAndItemService;

    /**
     * 去往组织机构绑定检测项的页面
     * @return
     */
    @GetMapping("/toBindingOrganizationAndItemsPage")
    @RequiresPermissions(value = {"detectionBindDetection","detectionUnBindDetection"},logical = Logical.OR)
    public String toBindingOrganizationAndItemsPage(){
        return "statistics/organization-items";
    }

    /**
     * 去往绩效页面
     * @return
     */
    @GetMapping("/toAchievementsPage")
    @RequiresPermissions("showPerformance")
    public String toAchievementsPage(){
        return "statistics/jixiao";
    }

    @GetMapping("/toOrgSamplePage")
    @RequiresPermissions("showPerformance")
    public String toOrgSamplePage(){
        return "statistics/jixiao_e";
    }

    /**
     * 给检测项目绑定组织机构
     * @param orgItemIntermediate
     * @author zyh
     */
    @PostMapping("/bindOrgAndItems")
    @RequiresPermissions("detectionBindDetection")
    @ResponseBody
    public ResponseResult bindOrgAndItems(OrgItemIntermediate orgItemIntermediate ){
        ResponseResult responseResult = null;
        try {
            orgAndItemService.bindOrgAndItems(orgItemIntermediate);
            log.info("给检测项绑定组织机构成功");
            responseResult = ResponseResult.success(ResponseResultEnum.SUCCESS.getCode(),null,"给检测项绑定组织机构成功");
        } catch(Exception e){
            e.printStackTrace();
            log.error("给检测项绑定组织机构失败");
            responseResult = ResponseResult.failure("给检测项绑定组织机构失败");
        }
        return responseResult;
    }

    /**
     * 根据组织机构id获取该组织机构绑定的项目列表和该组织机构没有绑定的项目列表
     * @param orgId
     * @return
     * @author zyh
     */
    @PostMapping("/getItemsByOrgId")
    @RequiresPermissions(value = {"detectionBindDetection","detectionUnBindDetection"},logical = Logical.OR)
    @ResponseBody
    public ResponseResult getItemsByOrgId(Long orgId){
        ResponseResult responseResult = null;
        try {
            List<Object> list = orgAndItemService.getItemsByOrgId(orgId);
            log.info("根据组织机构id获取该组织机构绑定的项目列表和该组织机构没有绑定的项目列表成功");
            responseResult = ResponseResult.success(ResponseResultEnum.SUCCESS.getCode(),list,"根据组织机构id获取该组织机构绑定的项目列表和该组织机构没有绑定的项目列表成功");
        } catch(Exception e){
            e.printStackTrace();
            log.error("根据组织机构id获取该组织机构绑定的项目列表和该组织机构没有绑定的项目列表失败");
            responseResult = ResponseResult.failure("根据组织机构id获取该组织机构绑定的项目列表和该组织机构没有绑定的项目列表失败");
        }
        return responseResult;
    }

    /**
     * 解除组织机构与检测项目之间的绑定
     * @param orgItemIntermediate
     * @author zyh
     */
    @PostMapping("/unboundOrgAndItems")
    @RequiresPermissions("detectionUnBindDetection")
    @ResponseBody
    public ResponseResult unboundOrgAndItems(OrgItemIntermediate orgItemIntermediate){
        ResponseResult responseResult = null;
        try {
            orgAndItemService.unboundOrgAndItems(orgItemIntermediate);
            log.info("解除组织机构与检测项目之间的绑定成功");
            responseResult = ResponseResult.success(ResponseResultEnum.SUCCESS.getCode(),null,"解除组织机构与检测项目之间的绑定成功");
        } catch(Exception e){
            e.printStackTrace();
            log.error("解除组织机构与检测项目之间的绑定失败");
            responseResult = ResponseResult.failure("解除组织机构与检测项目之间的绑定失败");
        }
        return responseResult;
    }

    /**
     * 获取绩效信息
     * @param orgName
     * @param pageNum
     * @param pageSize
     * @return
     * @author zyh
     */
    @PostMapping("/getAchievementsInfo")
    @RequiresPermissions("showPerformance")
    @ResponseBody
    public ResponseResult getAchievementsInfo(@DateTimeFormat(pattern = "yyyy-MM") Date startTime,@DateTimeFormat(pattern = "yyyy-MM") Date endTime, String orgName, Integer pageNum, Integer pageSize){
        ResponseResult responseResult = null;
        PageInfo<Achievements> pageInfo = null;
        try {
            pageInfo = orgAndItemService.getAchievementsInfoList(startTime,endTime,orgName,pageNum,pageSize);
            log.info("获取绩效信息成功");
            responseResult = ResponseResult.success(ResponseResultEnum.SUCCESS.getCode(),pageInfo,"获取绩效信息成功");
        } catch(Exception e){
            e.printStackTrace();
            log.error("获取绩效信息失败");
            responseResult = ResponseResult.failure("获取绩效信息失败");
        }
        return responseResult;
    }

    /**
     * 根据项目id列表获取检测样品信息
     * @param pageNum
     * @param pageSize
     * @param startTime
     * @param endTime
     * @param orgId
     * @return
     * @author zyh
     */
    @PostMapping("/getSampleInfoByOrgId")
    @ResponseBody
    public ResponseResult getSampleInfoByOrgId(Integer pageNum, Integer pageSize,@DateTimeFormat(pattern = "yyyy-MM")Date startTime,@DateTimeFormat(pattern = "yyyy-MM") Date endTime, Long orgId){
        ResponseResult responseResult = null;
        PageInfo<SampleInfo> pageInfo = null;
        try {
            pageInfo = orgAndItemService.getSampleInfoByOrgId(pageNum,pageSize,startTime,endTime,orgId);
            log.info("根据项目id列表获取检测样品信息成功");
            responseResult = ResponseResult.success(ResponseResultEnum.SUCCESS.getCode(),pageInfo,"根据项目id列表获取检测样品信息成功");
        } catch(Exception e){
            e.printStackTrace();
            log.error("根据项目id列表获取检测样品信息失败");
            responseResult = ResponseResult.failure("根据项目id列表获取检测样品信息失败");
        }
        return responseResult;
    }

    /**
     * 根据部门id获取及时的检测样品和不及时的检测样品的数量
     * @param startTime
     * @param endTime
     * @param orgId
     * @return
     * @author zyh
     */
    @PostMapping("/getOnTimeAndOvertimeSampleCountByOrgId")
    @RequiresPermissions("showPerformance")
    @ResponseBody
    public ResponseResult getOnTimeAndOvertimeSampleCountByOrgId(@DateTimeFormat(pattern = "yyyy-MM")Date startTime,@DateTimeFormat(pattern = "yyyy-MM") Date endTime, Long orgId){
        ResponseResult responseResult = null;
        try {
            List<Object> count = orgAndItemService.getOnTimeAndOvertimeSampleCountByOrgId(startTime, endTime, orgId);
            log.info("根据部门id获取及时的检测样品和不及时的检测样品的数量成功");
            responseResult = ResponseResult.success(ResponseResultEnum.SUCCESS.getCode(),count,"根据部门id获取及时的检测样品和不及时的检测样品的数量成功");
        } catch(Exception e){
            e.printStackTrace();
            log.error("根据部门id获取及时的检测样品和不及时的检测样品的数量失败");
            responseResult = ResponseResult.failure("根据部门id获取及时的检测样品和不及时的检测样品的数量失败");
        }
        return responseResult;
    }
}
