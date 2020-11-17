package com.xwkj.ygjces.controller;

import com.github.pagehelper.PageInfo;
import com.xwkj.ygjces.common.ResponseResult;
import com.xwkj.ygjces.common.ResponseResultEnum;
import com.xwkj.ygjces.controller.vo.JcCoreWtInfoVo;
import com.xwkj.ygjces.controller.vo.WtArrearsDetailsVo;
import com.xwkj.ygjces.model.JcCoreWtInfo;
import com.xwkj.ygjces.service.JcCoreSampleService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.CollectionUtils;
import com.xwkj.ygjces.service.JcCoreWtInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;

@Controller
public class JcCoreWtInfoController {

    private static Log log = LogFactory.getLog(JcCoreWtInfoController.class);

    @Autowired
    JcCoreWtInfoService jcCoreWtInfoService;

    @Autowired
    JcCoreSampleService jcCoreSampleService;

    /**
     * 跳转区域委托详情页面
     * @return
     * @author wanglei
     */
    @GetMapping("/toArrearsDetailsPage")
    public String toArrearsDetailsPage(){
        return "statistics/arrearsDetails";
    }

    /**
     * 跳转区域委托详情页面
     * @return
     * @author wanglei
     */
    @GetMapping("/toAchartsPage")
    public String toAchartsPage(){
        return "statistics/Acharts";
    }

    @GetMapping("/toBchartsPage")
    public String toBchartsPage(){
        return "statistics/Bcharts";
    }

    /**
     * 根据合同编号查找对应委托单列表
     * @param wtUnit 委托单位
     * @param pageNum 页数
     * @param pageSize 页面大小
     * @return
     * @author zyh
     */
    @PostMapping("/getWtArrearsDetails")
    @ResponseBody
    public ResponseResult getWtArrearsDetails(String wtUnit ,@DateTimeFormat(pattern = "yyyy-MM-dd")Date startTime,@DateTimeFormat(pattern = "yyyy-MM-dd")Date endTime, Integer pageNum, Integer pageSize){
        if(pageNum == null){
            pageNum = 1;
        }
        if(pageSize == null){
            pageSize = 10;
        }
        try {
            PageInfo<WtArrearsDetailsVo> pageInfo = jcCoreSampleService.getWtArrearsDetails(wtUnit,startTime,endTime,pageNum,pageSize);
            log.info("返回欠费信息成功");
            return ResponseResult.success(ResponseResultEnum.SUCCESS.getCode(),pageInfo,"返回欠费信息成功!");
        } catch(Exception e){
            log.error("返回欠费信息失败");
            return ResponseResult.failure("返回欠费信息失败!");
        }
    }

    /**
     * 跳转区域委托详情页面
     * @return
     * @author wanglei
     */
    @GetMapping("/toMapByArea")
    public String toMapByArea(){
        return "statistics/map";
    }

    /**
     * 根据合同编号查询对应委托单时间和总数
     * @param contractCode 合同编号
     * @return
     * @author wanglei
     */
    @PostMapping("/getJcCoreWtInfoByContractCode")
    @ResponseBody
    public ResponseResult getJcCoreWtInfoByContractCode(String contractCode){
        List<Object> list = jcCoreWtInfoService.getJcCoreWtInfoByContractCode(contractCode);
        if (CollectionUtils.isEmpty((List<Object>)list.get(0)) || CollectionUtils.isEmpty((List<Object>)list.get(1))){
            log.error("无查询结果");
            return ResponseResult.failure(ResponseResultEnum.FAILURE.getCode(),"无查询结果!");
        }
        log.info("查询成功");
        return ResponseResult.success(ResponseResultEnum.SUCCESS.getCode(),list,"查询成功!");
    }


    /**
     * 根据合同编号查找对应委托单列表
     * @param contractCode 合同编号
     * @param pageNum 页数
     * @param pageSize 页面大小
     * @return
     * @author zyh
     */
    @PostMapping("/getJcCoreWtInfoListByContractCode")
    @ResponseBody
    public ResponseResult getJcCoreWtInfoListByContractCode(String contractCode , Integer pageNum, Integer pageSize){
        ResponseResult responseResult = null;
        PageInfo<JcCoreWtInfo> pageInfo = null;
        if(pageNum == null){
            pageNum = 1;
        }
        if(pageSize == null){
            pageSize = 10;
        }
        try {
            pageInfo = jcCoreWtInfoService.getJcCoreWtInfoListByContractCode(contractCode, pageNum, pageSize);
            log.info("获取委托单列表成功");
            responseResult = ResponseResult.success(ResponseResultEnum.SUCCESS.getCode(),pageInfo,"获取委托单列表成功");
        } catch(Exception e){
            e.printStackTrace();
            log.error("获取委托单列表失败");
            responseResult = ResponseResult.failure("获取委托单列表失败");
        }
        return responseResult;
    }

    /**
     * 根据合同编号获取对应已结算委托单数量和未结算委托单数量的列表
     * @param contractCode 合同编号
     * @return
     * @author zyh
     */
    @PostMapping("/getJsAndWjsCountListByContractCode")
    @ResponseBody
    public ResponseResult getJsAndWjsCountListByContractCode(String contractCode){
        ResponseResult responseResult = null;
        try {
            List<Object> list = jcCoreWtInfoService.getJsAndWjsCountListByContractCode(contractCode);
            log.info("获取对应已结算委托单数量和未结算委托单数量的列表成功");
            responseResult = ResponseResult.success(ResponseResultEnum.SUCCESS.getCode(),list,"获取对应已结算委托单数量和未结算委托单数量的列表成功");
        } catch(Exception e){
            e.printStackTrace();
            log.error("获取对应已结算委托单数量和未结算委托单数量的列表失败");
            responseResult = ResponseResult.failure("获取对应已结算委托单数量和未结算委托单数量的列表失败");
        }
        return responseResult;
    }

    /**
     * 根据城市区域按照区域统计委托单
     * @param city 城市
     * @param area 区域
     * @param startTime,endTime 筛选时间
     * @return
     * @author wanglei
     */
    @PostMapping("/getMapCityAreaWtCount")
    @ResponseBody
    public ResponseResult getMapCityAreaWtCount(String city, String area, @DateTimeFormat(pattern = "yyyy-MM")Date startTime, @DateTimeFormat(pattern = "yyyy-MM")Date endTime){
        if (city != null && area != null){
            //区域统计委托单
            Integer wtCountByCityArea = jcCoreWtInfoService.getMapCityAreaWtCount(city,area,startTime,endTime);
            if (wtCountByCityArea == null){
                //无委托单记录
                return ResponseResult.success(ResponseResultEnum.SUCCESS.getCode(),0,"该区域无委托单记录!");
            }
            //返回统计委托单数量
            return ResponseResult.success(ResponseResultEnum.SUCCESS.getCode(),wtCountByCityArea,"区域统计委托单成功!");
        }
        return ResponseResult.failure(ResponseResultEnum.FAILURE.getCode(),"区域统计委托单参数错误!");
    }

    /**
     * 根据城市区域时间查询委托单详情
     * @param wt_num 委托单号
     * @param city 城市
     * @param area 区域
     * @param startTime,endTime 筛选时间
     * @return
     * @author wanglei
     */
    @PostMapping("/getMapCityAreaWtInfo")
    @ResponseBody
    public ResponseResult getMapCityAreaWtInfo(String wt_num,String city, String area, @DateTimeFormat(pattern = "yyyy-MM")Date startTime, @DateTimeFormat(pattern = "yyyy-MM")Date endTime,Integer pageNum, Integer pageSize){

	    ResponseResult responseResult = null;
    	PageInfo<JcCoreWtInfoVo> pageInfo = null;
	    if(pageNum == null){
		    pageNum = 1;
	    }
	    if(pageSize == null){
		    pageSize = 10;
	    }
	    try {
		    pageInfo =  jcCoreWtInfoService.getMapCityAreaWtInfo(wt_num,city,area,startTime,endTime,pageNum,pageSize);
		    log.info("获取区域委托单列表成功");
		    responseResult = ResponseResult.success(ResponseResultEnum.SUCCESS.getCode(),pageInfo,"获取区域委托单列表成功!");
	    } catch(Exception e){
		    e.printStackTrace();
		    log.error("获取区域委托单列表失败");
		    responseResult = ResponseResult.failure(ResponseResultEnum.FAILURE.getCode(),"获取区域委托单列表失败!");
	    }
	    return responseResult;
    }
    /**
     * 查询当天委托单金额
     * @return
     */
    @PostMapping("wtRealMoneyWithOneDay")
    @ResponseBody
    public ResponseResult getWtRealMoneyWithOneDay(){
        log.info("查询当天委托单金额!");
        try{
            Integer wtRealMoneyWithOneDay = jcCoreWtInfoService.getWtRealMoneyWithOneDay();
            if (wtRealMoneyWithOneDay == null){
                wtRealMoneyWithOneDay = 0;
            }
            return ResponseResult.success(ResponseResultEnum.SUCCESS.getCode(),wtRealMoneyWithOneDay,"查询当天委托单金额成功！");
        }catch (Exception e){
            log.error("查询当天委托单金额异常!");
            return ResponseResult.failure("查询当天委托单金额异常!");
        }
    }

    /**
     * 查询当月委托单金额
     * @return
     */
    @PostMapping("wtRealMoneyWithOneMonth")
    @ResponseBody
    public ResponseResult getWtRealMoneyWithOneMonth(){
        log.info("查询当月委托单金额!");
        try{
            Integer wtRealMoneyWithOneMonth = jcCoreWtInfoService.getWtRealMoneyWithOneMonth();
            if (wtRealMoneyWithOneMonth == null){
                wtRealMoneyWithOneMonth = 0;
            }
            return ResponseResult.success(ResponseResultEnum.SUCCESS.getCode(),wtRealMoneyWithOneMonth,"查询当月委托单金额成功！");
        }catch (Exception e){
            log.error("查询当月委托单金额异常!");
            return ResponseResult.failure("查询当月委托单金额异常!");
        }
    }

    /**
     * 查询当天委托单的数量
     * @return
     */
    @PostMapping("wtNumWithOneDay")
    @ResponseBody
    public ResponseResult getWtNumWithOneDay(){
        log.info("查询当天委托单数量!");
        try{
            Integer wtCountWithOneDay = jcCoreWtInfoService.getWtCountWithOneDay();
            if (wtCountWithOneDay == null){
                wtCountWithOneDay = 0;
            }
            return ResponseResult.success(ResponseResultEnum.SUCCESS.getCode(),wtCountWithOneDay,"委托单一天的数量");
        }catch (Exception e){
            log.error("查询当天委托单数量异常!");
            return ResponseResult.failure("查询当天委托单数量异常!");
        }
    }

    /**
     * 查询当月委托单的数量
     * @return
     */
    @PostMapping("wtNumWithOneMonth")
    @ResponseBody
    public ResponseResult getWtNumWithOneMonth(){
        log.info("查询当月委托单数量!");
        try{
            Integer wtCountWithOneDay = jcCoreWtInfoService.getWtCountWithOneMonth();
            if (wtCountWithOneDay == null){
                wtCountWithOneDay = 0;
            }
            return ResponseResult.success(ResponseResultEnum.SUCCESS.getCode(),wtCountWithOneDay,"委托单当月的数量");
        }catch (Exception e){
            log.error("查询当月委托单数量异常!");
            return ResponseResult.failure("查询当月委托单数量异常!");
        }
    }

    /**
     * 条件获取委托单位的应收金额、实收金额、已收金额
     * @param wtUnit 委托单位名称
     * @param sfStatus 收费状态
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     */
    @PostMapping("/getWtUnitAmount")
    @ResponseBody
    public ResponseResult getWtUnitAmount(String wtUnit,String sfStatus,@DateTimeFormat(pattern = "yyyy-MM-dd") Date startTime,@DateTimeFormat(pattern = "yyyy-MM-dd")Date endTime){
        try{
            if (sfStatus!=null && sfStatus.equals("未收费")){
                sfStatus = "已计价";
            }
            List<Object> list = jcCoreSampleService.getWtUnitAmount(wtUnit,sfStatus,startTime,endTime);
            log.info("获取委托单位的应收金额,实收金额,已收金额成功！");
            return ResponseResult.success(ResponseResultEnum.SUCCESS.getCode(),list,"获取委托单位的应收金额,实收金额,已收金额成功！");
        }catch(Exception e){
            e.printStackTrace();
            log.error("获取委托单位的应收金额,实收金额,已收金额失败！");
            return ResponseResult.failure(ResponseResultEnum.FAILURE.getCode(),"获取委托单位的应收金额,实收金额,已收金额失败！");
        }
    }

}
