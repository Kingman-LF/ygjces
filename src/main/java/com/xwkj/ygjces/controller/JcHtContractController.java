package com.xwkj.ygjces.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.pagehelper.PageInfo;
import com.xwkj.ygjces.common.ResponseResult;
import com.xwkj.ygjces.common.ResponseResultEnum;
import com.xwkj.ygjces.model.JcHtContract;
import com.xwkj.ygjces.model.UserInfo;
import com.xwkj.ygjces.service.JcHtContractService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
public class JcHtContractController {

    private static Log log = LogFactory.getLog(JcHtContractController.class);

    @Autowired
    JcHtContractService jcHtContractService;

    /**
     * 跳转统计下综合统计页面
     * @author wanglei
     * @return
     */
    @RequestMapping("/toStatisticsPage")
    public String toStatisticsPage(){
        return "statistics/duoecharts";
    }

    /**
     *根据开始时间结束时间查询该时间段合同数量变化
     * @param jcHtContract startTime 开始时间 endTime 结束时间
     * @author wanglei
     */
    @PostMapping("/Calculation_contract")
    @ResponseBody
    public ResponseResult getContractList(JcHtContract jcHtContract){
        //根据时间查询,默认当前系统时间
        List<Object> list = jcHtContractService.getCalculationList(jcHtContract);
        if (list.isEmpty()){
            return ResponseResult.failure(ResponseResultEnum.FAILURE.getCode(),"无查询结果!");
        }
        return ResponseResult.success(ResponseResultEnum.SUCCESS.getCode(),list,"查询成功!");
    }

    /**
     * 跳转统计下综合统计当月合同数据页面
     * @author wanglei
     * @return
     */
    @GetMapping("/toComprehensive_contract")
    public String toComprehensive_contract(){
        return "statistics/contract";
    }

    /**
     * 跳转统计下综合统计当月合同下委托单数据页面
     * @author wanglei
     * @return
     */
    @GetMapping("/toComprehensive_contractInfo")
    public String toComprehensive_contractInfo(){
        return "statistics/contractInfo";
    }

    /**
     * 去往显示合同信息的页面
     * @return
     */
    @GetMapping("/toContractPage")
    public String toContractPage(){
        return "statistics/jine";
    }

    /**
     * 去往显示合同对应的委托单的页面
     * @return
     */
    @GetMapping("/toLimitPage")
    public String toLimitPage(){
        return "statistics/jine_e";
    }

    /**
     * 获取合同列表
     * @param jcHtContract 合同信息对象
     * @param pageNum 页数
     * @param pageSize 页面大小
     * @return
     * @author zyh
     */
    @PostMapping("/showContractList")
    @ResponseBody
    public ResponseResult getContractList( JcHtContract jcHtContract,Integer pageNum,Integer pageSize){
        ResponseResult responseResult = null;
        PageInfo<JcHtContract> pageInfo = null;
        if(pageNum == null){
            pageNum = 1;
        }
        if(pageSize == null){
            pageSize = 10;
        }
        try {
            pageInfo = jcHtContractService.getContractList(jcHtContract,pageNum,pageSize);
            log.info("获取合同列表成功");
            responseResult = ResponseResult.success(ResponseResultEnum.SUCCESS.getCode(),pageInfo,"获取合同列表成功");
        } catch(Exception e){
            e.printStackTrace();
            log.error("获取合同列表失败");
            responseResult = ResponseResult.failure("获取合同列表失败");
        }
        return responseResult;
    }

    /**
     * 根据当月时间段年月日获取合同列表
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @param contractName 合同名称
     * @param pageNum 页数
     * @param pageSize 页面大小
     * @return
     * @author wanglei
     */
    @PostMapping("/showContractListByOneMonth")
    @ResponseBody
    public ResponseResult getContractList(@DateTimeFormat(pattern = "yyyy-MM-dd")Date startDate , @DateTimeFormat(pattern = "yyyy-MM-dd")Date endDate ,String contractName, @RequestParam(value = "page") Integer pageNum, @RequestParam(value = "limit") Integer pageSize){
        ResponseResult responseResult = null;
        PageInfo<JcHtContract> pageInfo = null;
        if(pageNum == null){
            pageNum = 1;
        }
        if(pageSize == null){
            pageSize = 10;
        }
        try {
            pageInfo = jcHtContractService.getContractListByOneMonth(startDate,endDate,contractName,pageNum,pageSize);
            log.info("获取合同列表成功");
            responseResult = ResponseResult.success(ResponseResultEnum.SUCCESS.getCode(),pageInfo,"获取合同列表成功");
        } catch(Exception e){
            e.printStackTrace();
            log.error("获取合同列表失败");
            responseResult = ResponseResult.failure("获取合同列表失败");
        }
        return responseResult;
    }

    /**
     * 按照月份统计当年合同数量
     * @return
     */
    @PostMapping("htCountByMonth")
    @ResponseBody
    public ResponseResult getHtCountByMonth(){
        log.info("按照月份统计当年合同数量!");
        try{
            List<Integer> htCountByMonth = jcHtContractService.getHtCountByMonth();
            return ResponseResult.success(ResponseResultEnum.SUCCESS.getCode(),htCountByMonth,"按照月份统计当年合同数量成功！");
        }catch (Exception e){
            log.error("按照月份统计当年合同数量异常!");
            return ResponseResult.failure("按照月份统计当年合同数量异常!");
        }
    }

    /**
     * 查询当天合同的数量
     * @return
     */
    @PostMapping("htNumWithOneDay")
    @ResponseBody
    public ResponseResult gethtNumWithOneDay(){
        log.info("查询当天合同数量!");
        try{
            Integer htCountWithOneDay = jcHtContractService.getContractCountWithOneDay();
            if (htCountWithOneDay == null){
                htCountWithOneDay = 0;
            }
            return ResponseResult.success(ResponseResultEnum.SUCCESS.getCode(),htCountWithOneDay,"合同一天的数量");
        }catch (Exception e){
            log.error("查询当天合同数量异常!");
            return ResponseResult.failure("查询当天合同数量异常!");
        }
    }

    /**
     * 查询当月合同的数量
     * @return
     */
    @PostMapping("htNumWithOneMonth")
    @ResponseBody
    public ResponseResult gethtNumWithOneMonth(){
        log.info("查询当月合同数量!");
        try{
            Integer htCountWithOneDay = jcHtContractService.getContractCountWithOneMonth();
            if (htCountWithOneDay == null){
                htCountWithOneDay = 0;
            }
            return ResponseResult.success(ResponseResultEnum.SUCCESS.getCode(),htCountWithOneDay,"合同当月的数量");
        }catch (Exception e){
            log.error("查询当月合同数量异常!");
            return ResponseResult.failure("查询当天合同数量异常!");
        }
    }

}
