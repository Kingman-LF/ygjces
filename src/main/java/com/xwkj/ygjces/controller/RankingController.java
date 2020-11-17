package com.xwkj.ygjces.controller;

import com.github.pagehelper.PageInfo;
import com.xwkj.ygjces.common.ResponseResult;
import com.xwkj.ygjces.model.SampleInfo;
import com.xwkj.ygjces.service.OvertimeModelService;
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

@Controller
public class RankingController {

    private static Log log = LogFactory.getLog(RankingController.class);

    @Autowired
    OvertimeModelService overtimeModelService;

    /**
     * 去往排名页面
     * @return
     * @author zyh
     */
    @GetMapping("/toRankingPage")
    public String toRankingPage(){
        return "ranking";
    }

    @GetMapping("/toRankingInfoPage")
    public String toRankingInfoPage(){
        return "ranking-info";
    }

    /**
     * 获取排名数据列表
     * @return
     * @author zyh
     */
    @PostMapping("/getOverTimeRankingDataList")
    @ResponseBody
    public ResponseResult getOverTimeRankingDataList(@DateTimeFormat(pattern = "yyyy-MM-dd") Date startTime, @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime, Integer stage, Integer mode, Double percentage){
        ResponseResult responseResult = null;
        try {
            List<Object> list = overtimeModelService.getRankingDataList(startTime,endTime,stage,mode,percentage);
            log.info("获取超时排名数据列表成功");
            responseResult = ResponseResult.success(ResponseResult.success().getCode(),list,"获取超时排名数据列表成功");
        } catch(Exception e){
            e.printStackTrace();
            log.error("超时排名数据列表失败");
            responseResult = ResponseResult.failure("超时排名数据列表失败");
        }
        return responseResult;
    }

    /**
     * @Description : 获取排名详情信息
     * @methodName : getRankingInfoList
     * @param pageNum :
     * @param pageSize :
     * @param stage :
     * @param mode :
     * @param percentage :
     * @param itemName :
     * @return : com.xwkj.ygjces.common.ResponseResult
     * @exception :
     * @author : zyh
     */
    @PostMapping("/getRankingInfoList")
    @ResponseBody
    public ResponseResult getRankingInfoList(@DateTimeFormat(pattern = "yyyy-MM-dd") Date startTime, @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime,Integer pageNum,Integer pageSize,Integer stage, Integer mode, Double percentage,String itemName){
        ResponseResult responseResult = null;
        PageInfo<SampleInfo> pageInfo = null;
        try {
            pageInfo = overtimeModelService.getRankingInfoList(startTime,endTime,pageNum,pageSize,stage,mode,percentage,itemName);
            log.info("获取排名详情信息成功");
            responseResult = ResponseResult.success(ResponseResult.success().getCode(),pageInfo,"获取超时排名数据列表成功");
        } catch(Exception e){
            e.printStackTrace();
            log.error("获取排名详情信息失败");
            responseResult = ResponseResult.failure("获取排名详情信息失败");
        }
        return responseResult;
    }

}
