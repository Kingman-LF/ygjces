package com.xwkj.ygjces.controller;

import com.github.pagehelper.PageInfo;
import com.xwkj.ygjces.common.ResponseResult;
import com.xwkj.ygjces.common.ResponseResultEnum;
import com.xwkj.ygjces.model.JcCoreWtInfo;
import com.xwkj.ygjces.service.IntegrativeStatisticsService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;

/**
 * 操作统计信息的控制层
 */
@Controller
public class IntegrativeStatisticsController {

    private static Log log = LogFactory.getLog(IntegrativeStatisticsController.class);

    @Autowired
    IntegrativeStatisticsService integrativeStatisticsService;


    /**
     * 去往综合统计页面
     * @return
     */
    @GetMapping("/toIntegrativeStatisticsPage")
    public String toIntegrativeStatisticsPage(){
        return "statistics/duoecharts";
    }

    /**
     * 去往地图页面
     * @return
     */
    @GetMapping("toMapPage")
    public String toMapPage(){
        return "statistics/map";
    }

    /**
     * 去往统计合同的页面
     * @return
     */
    @GetMapping("/toIntegrativeContractPage")
    public String toIntegrativeContractPage(){
        return "statistics/contract";
    }

    /**
     * 去往合同名称相关委托单页面
     * @return
     */
    @GetMapping("/toContractInfoPage")
    public String toContractInfoPage(){
        return "statistics/contractInfo";
    }

    /**
     * 去往年度委托单详情页面
     * @return
     */
    @GetMapping("/toEntrustPage")
    public String toEntrustPage(){
        return "statistics/entrust";
    }



    /**
     * 获取当年或者指定时间段的委托单统计信息
     * @param startTime 指定的开始时间
     * @param endTime   指定的结束时间
     * @return
     * @author zyh
     */
    @PostMapping("/getJcCoreWtInfoInOneYear")
    @ResponseBody
    public ResponseResult getJcCoreWtInfoInOneYear(@DateTimeFormat(pattern = "yyyy-MM") Date startTime,@DateTimeFormat(pattern = "yyyy-MM") Date endTime){
        ResponseResult responseResult = null;
        try {
            List<Object> list = integrativeStatisticsService.getJcCoreWtInfoInOneYear(startTime, endTime);
            log.info("获取委托单统计信息成功");
            responseResult = ResponseResult.success(ResponseResultEnum.SUCCESS.getCode(),list,"获取委托单统计信息成功");
        } catch(Exception e){
            e.printStackTrace();
            log.error("获取委托单统计信息失败");
            responseResult = ResponseResult.failure("获取委托单统计信息失败");
        }
        return responseResult;
    }

    /**
     * 根据日期查找对应的委托单信息列表并且可以根据条件筛选得到的委托单信息列表
     * @param jcCoreWtInfo 委托单信息
     * @param pageNum 页数
     * @param pageSize 页面大小
     * @return
     * @author zyh
     */
    @PostMapping("/getJcCoreWtListInfoByDate")
    @ResponseBody
    public ResponseResult getJcCoreWtListInfoByDate(JcCoreWtInfo jcCoreWtInfo, Integer pageNum, Integer pageSize){
        ResponseResult responseResult = null;
        PageInfo<JcCoreWtInfo> pageInfo = null;
        try {
            pageInfo = integrativeStatisticsService.getJcCoreWtListInfoByDate(jcCoreWtInfo,pageNum,pageSize);
            log.info("获取委托单信息列表成功");
            responseResult = ResponseResult.success(ResponseResultEnum.SUCCESS.getCode(),pageInfo,"获取委托单信息列表成功");
        } catch(Exception e){
            e.printStackTrace();
            log.error("获取委托单信息列表失败");
            responseResult = ResponseResult.failure("获取委托单信息列表失败");
        }
        return responseResult;
    }
}
