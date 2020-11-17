package com.xwkj.ygjces.controller;

import com.xwkj.ygjces.common.ResponseResult;
import com.xwkj.ygjces.common.ResponseResultEnum;
import com.xwkj.ygjces.service.JcCoreSampleService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class ReportController {

    private Log log = LogFactory.getLog(ReportController.class);

    @Autowired
    JcCoreSampleService jcCoreSampleService;

    /**
     * 按照月份统计当年报告
     * @return
     * @author wanglei
     */
    @PostMapping("reportCountByMonth")
    @ResponseBody
    public ResponseResult getReportCountByMonth(){
        log.info("按照月份统计当年报告!");
        try{
            List<Object> reportCountByMonth = jcCoreSampleService.getReportCountByMonth();
            return ResponseResult.success(ResponseResultEnum.SUCCESS.getCode(),reportCountByMonth,"按照月份统计当年报告数量成功！");
        }catch (Exception e){
            log.error("按照月份统计当年报告数量异常!");
            return ResponseResult.failure("按照月份统计当年报告数量异常!");
        }
    }

    /**
     * 查询当天打印报告量
     * @return
     * @author wanglei
     */
    @PostMapping("printReportCountWithOneDay")
    @ResponseBody
    public ResponseResult getprintReportCountWithOneDay(){
        log.info("查询当天打印报告量!");
        try{
            Integer printReportCountWithOneDay = jcCoreSampleService.getprintReportCountWithOneDay();
            if (printReportCountWithOneDay == null){
                printReportCountWithOneDay = 0;
            }
            return ResponseResult.success(ResponseResultEnum.SUCCESS.getCode(),printReportCountWithOneDay,"查询当天打印报告量成功！");
        }catch (Exception e){
            log.error("查询当天打印报告量失败！");
            return ResponseResult.failure("查询当天打印报告量失败！");
        }
    }

    /**
     * 查询当月打印报告量
     * @return
     * @author wanglei
     */
    @PostMapping("printReportCountWithOneMonth")
    @ResponseBody
    public ResponseResult getprintReportCountWithOneMonth(){
        log.info("查询当月打印报告量!  ");
        try{
            Integer printReportCountWithOneMonth = jcCoreSampleService.getprintReportCountWithOneMonth();
            if (printReportCountWithOneMonth == null){
                printReportCountWithOneMonth = 0;
            }
            return ResponseResult.success(ResponseResultEnum.SUCCESS.getCode(),printReportCountWithOneMonth,"查询当月打印报告量成功！");
        }catch (Exception e){
            log.error("查询当月打印报告量失败！");
            return ResponseResult.failure("查询当月打印报告量失败！");
        }
    }
}