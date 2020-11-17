package com.xwkj.ygjces.controller;

import com.github.pagehelper.PageInfo;
import com.xwkj.ygjces.common.ResponseResult;
import com.xwkj.ygjces.common.ResponseResultEnum;
import com.xwkj.ygjces.controller.vo.JcXmCategoryVo;
import com.xwkj.ygjces.controller.vo.JcXtcsXmWithSampleVo;
import com.xwkj.ygjces.service.JcXtcsXmService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;

/**
 * 检测类年度查询控制层
 */
@Controller
public class JcXtcsXmController {

	private static final Log logger = LogFactory.getLog(JcXtcsXmController.class);

	@Autowired
	JcXtcsXmService jcXtcsXmService;

	/**
	 *根据用户重载项目树
	 * */
	@PostMapping("/reloadJcXmZtree")
	@ResponseBody
	public ResponseResult reloadJcXmZtree(@RequestParam("id") String id){
		List<JcXmCategoryVo> xmCategory =  jcXtcsXmService.getJcXmCategoryByUserId(id);
		if(xmCategory != null){
			return ResponseResult.success(ResponseResultEnum.SUCCESS.getCode(),xmCategory,"返回用户绑定检测项树成功!");
		}
		return ResponseResult.failure(ResponseResultEnum.FAILURE.getCode(),"返回用户绑定检测项树失败!");
	}

	/**
	 * 跳转项目分类树页面
	 * @return
	 */
	@RequestMapping("/toJcXmCategoryPage")
	public String toJcXmCategoryPage(){return "statistics/jcXmCategory";}

	/**
	 * 跳转项目分类树页面
	 * @return
	 */
	@RequestMapping("/toJcXmReviewPage")
	public String toJcXmReviewPage(){return "statistics/jcXmReview";}

	/**
	 * 跳转详情页面检测类
	 * @return
	 * @author wanglei
	 */
	@RequestMapping("/toTypeWithMonthPage")
	public String toTypeWithMonthPage(){return "statistics/type_month";}


	/**
	 * 获取项目分类树
	 */
	@RequestMapping("getJcXmCategory")
	@ResponseBody
	public ResponseResult getJcXmCategory(){
		List<JcXmCategoryVo> xmCategory =  jcXtcsXmService.getJcXmCategory();
		if(xmCategory != null){
			return ResponseResult.success(ResponseResultEnum.SUCCESS.getCode(),xmCategory,"返回项目树成功!");
		}
		return ResponseResult.failure(ResponseResultEnum.FAILURE.getCode(),"返回项目树失败!");
	}

	/**
	 * 跳转详情页面检测类
	 * @return
	 * @author wanglei
	 */
	@RequestMapping("/toTypePage")
	public String toTypePage(){return "statistics/type";}

	/**
	 * 按照时间筛选两年检测项(直属所有项目)数量对比
	 * @param startYear
	 * @param endYear
	 * @return
	 * @author wanglei
	 */
	@PostMapping("getCountJcXtcsXm")
	@ResponseBody
	public ResponseResult getCountJcXtcsXm(Integer startYear,Integer endYear){
		//第一次没有筛选时间
		if(startYear == null && endYear == null){
			List<Object> requireList = jcXtcsXmService.getCountJcXtcsXm(null,null);
			return ResponseResult.success(ResponseResultEnum.SUCCESS.getCode(),requireList,"检测类统计查询成功!");
		}
		if(startYear != null && endYear != null){
			List<Object> requireList = jcXtcsXmService.getCountJcXtcsXm(startYear,endYear);
			return ResponseResult.success(ResponseResultEnum.SUCCESS.getCode(),requireList,"检测类统计查询成功!");
		}
		return ResponseResult.failure("检测类统计查询参数错误!");

	}

	/**
	 * 按照时间筛选两个月检测项(直属所有项目)数量对比
	 * @param start
	 * @param end
	 * @return
	 * @author wanglei
	 */
	@PostMapping("getCountJcXtcsXmWithMonth")
	@ResponseBody
	public ResponseResult getCountJcXtcsXmWithMonth(String start,String end){
		//第一次没有筛选时间
		if(start == null && end == null){
			List<Object> requireList = jcXtcsXmService.getCountJcXtcsXmWithMonth(null,null);
			return ResponseResult.success(ResponseResultEnum.SUCCESS.getCode(),requireList,"检测类月度统计查询成功!");
		}
		if(start != null && end != null){
			List<Object> requireList = jcXtcsXmService.getCountJcXtcsXmWithMonth(start,end);
			return ResponseResult.success(ResponseResultEnum.SUCCESS.getCode(),requireList,"检测类月度统计查询成功!");
		}
		return ResponseResult.failure("检测类月度统计查询参数错误!");

	}

	/**
	 * 按照时间筛选检测项(所有项目下子项目)数量对比
	 * @param defaultDate 默认时间
	 * @param xmName 检测项名称
	 * @param start 检测项查询开始时间
	 * @param end 检测项结束时间
	 * @param pageNum
	 * @param pageSize
	 * @return
	 * @author wanglei
	 */
	@PostMapping("/getJcXtcsXmNameAndCount")
	@ResponseBody
	public ResponseResult getJcXtcsXmNameAndCount(String defaultDate,String xmName,@DateTimeFormat(pattern = "yyyy-MM-dd") Date start,@DateTimeFormat(pattern = "yyyy-MM-dd") Date end,Integer pageNum,Integer pageSize){

		ResponseResult responseResult = null;
		PageInfo<JcXtcsXmWithSampleVo> pageInfo = null;
		if(pageNum == null){
			pageNum = 1;
		}
		if(pageSize == null){
			pageSize = 10;
		}
		try{
			pageInfo =  jcXtcsXmService.getJcXtcsXmNameAndCount(defaultDate,xmName,start,end,pageNum,pageSize);
			responseResult = ResponseResult.success(ResponseResultEnum.SUCCESS.getCode(),pageInfo,"检测子类查询成功!");
		}catch(Exception e){
			e.printStackTrace();
			responseResult = ResponseResult.failure(ResponseResultEnum.FAILURE.getCode(),"检测子类查询失败!");
		}

		return responseResult;
	}

	/**
	 * 按照时间月份筛选检测项(所有项目下子项目)数量对比
	 * @param defaultDate 默认时间
	 * @param xmName 检测项名称
	 * @param start 检测项查询开始时间
	 * @param end 检测项结束时间
	 * @param pageNum
	 * @param pageSize
	 * @return
	 * @author wanglei
	 */
	@PostMapping("/getJcXtcsXmNameAndCountWithMonth")
	@ResponseBody
	public ResponseResult getJcXtcsXmNameAndCountWithMonth(String defaultDate,String xmName,@DateTimeFormat(pattern = "yyyy-MM-dd") Date start,@DateTimeFormat(pattern = "yyyy-MM-dd") Date end,Integer pageNum,Integer pageSize){

		ResponseResult responseResult = null;
		PageInfo<JcXtcsXmWithSampleVo> pageInfo = null;
		if(pageNum == null){
			pageNum = 1;
		}
		if(pageSize == null){
			pageSize = 10;
		}
		try{
			pageInfo =  jcXtcsXmService.getJcXtcsXmNameAndCountWithMonth(defaultDate,xmName,start,end,pageNum,pageSize);
			responseResult = ResponseResult.success(ResponseResultEnum.SUCCESS.getCode(),pageInfo,"检测子类查询成功!");
		}catch(Exception e){
			e.printStackTrace();
			responseResult = ResponseResult.failure(ResponseResultEnum.FAILURE.getCode(),"检测子类查询失败!");
		}

		return responseResult;
	}

	/**
	 * 按照时间筛选两年检测项(直属所有项目)金额
	 * @param startTime
	 * @param endTime
	 * @return
	 * @author wanglei
	 */
	@PostMapping("getCountJcXtcsXmAmount")
	@ResponseBody
	public ResponseResult getCountJcXtcsXmAmount(@DateTimeFormat(pattern = "yyyy-MM-dd") Date startTime,@DateTimeFormat(pattern = "yyyy-MM-dd")Date endTime){
		try{
			List<Object> requireList = jcXtcsXmService.getCountJcXtcsXmAmount(startTime,endTime);
			logger.info("检测项对应的金额统计成功！");
			return ResponseResult.success(ResponseResultEnum.SUCCESS.getCode(),requireList,"检测项对应的金额统计成功!");
		}catch (Exception e){
			e.printStackTrace();
			logger.error("检测项对应的金额统计失败！");
			return ResponseResult.failure("检测项对应的金额统计失败!");
		}

	}

}
