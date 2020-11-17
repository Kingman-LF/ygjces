package com.xwkj.ygjces.controller;

import com.github.pagehelper.PageInfo;
import com.xwkj.ygjces.common.ResponseResult;
import com.xwkj.ygjces.common.ResponseResultEnum;
import com.xwkj.ygjces.model.ExigencyInfo;
import com.xwkj.ygjces.service.ExigencyInfoService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

/**
 * 紧急程度管理
 */
@Controller
public class ExigencyController {

	private static Log log = LogFactory.getLog(ExigencyController.class);


	@Autowired
	ExigencyInfoService exigencyInfoService;

	/**
	 * 跳转紧急程度管理页面
	 * @return
	 */
	@GetMapping("/toExigencyListPage")
	@RequiresPermissions("selectEmergencyLevel")
	public String toExigencyListPage(){return "statistics/exigency-list";}

	/**
	 * 显示紧急程度管理
	 * @param exName
	 * @param isEnable
	 * @param page
	 * @param limit
	 * @return
	 */
	@PostMapping("/showExigencyInfoPageList")
	@RequiresPermissions("selectEmergencyLevel")
	@ResponseBody
	public ResponseResult showExigencyInfoPageList(String exName,Integer isEnable,Integer page, Integer limit){
		try {
			log.info("显示紧急程度列表!");
			PageInfo<ExigencyInfo> exigencyInfoList = exigencyInfoService.getExigencyInfoPage(exName,isEnable,page, limit);
			return ResponseResult.success(ResponseResultEnum.SUCCESS.getCode(), exigencyInfoList, "获取紧急程度分页列表成功");
		} catch (Exception e) {
			log.error("参数错误查询紧急程度列表失败!");
			return ResponseResult.failure(ResponseResultEnum.FAILURE.getCode(),"紧急程度表查询失败!");
		}
	}

	/**
	 * 去往紧急程度添加的页面
	 * @return
	 */
	@GetMapping("/toExigencyInfoAdd")
	@RequiresPermissions("addEmergencyLevel")
	public String toRoleInfoAdd(){
		return "statistics/exigencyAdd";
	}

	/**
	 * 更改是否启用状态
	 * @param pid
	 * @param isEnable
	 * @return
	 * @author wanglei
	 */
	@PostMapping("/updateExigency")
	@RequiresPermissions("updateEmergencyLevel")
	@ResponseBody
	public ResponseResult updateExigency(Long pid , Integer isEnable){
		try{
			log.info("修改启用状态!");
			exigencyInfoService.updateExigency(pid,isEnable);
			return ResponseResult.success(ResponseResultEnum.SUCCESS.getCode(),"","修改成功!");
		}catch(Exception e){
			log.error("更改启用状态失败!");
			return ResponseResult.failure(ResponseResultEnum.FAILURE.getCode(),"修改失败!");
		}
	}


	/**
	 *添加紧急情况
	 * @param exName
	 * @param level
	 * @return
	 */
	@PostMapping("/addExigencyInfo")
	@RequiresPermissions("addEmergencyLevel")
	@ResponseBody
	public ResponseResult addExigencyInfo(String exName,Integer level){

		try{
			log.info("无重复等级添加紧急情况!");
			Integer exigencyInfoCountByLevel = exigencyInfoService.countExigencyInfoByLevel(level);
			if (exigencyInfoCountByLevel > 0){
				return ResponseResult.success(1,"添加紧急等级已存在!","请选择添加到最后或者将等级大于等于级别加1");
			}
			exigencyInfoService.addExigencyInfo(exName,level);
			return ResponseResult.success(2,"","添加成功!");
		}catch(Exception e){
			log.error("无重复等级添加紧急情况失败!");
			return ResponseResult.failure(0,"添加失败!");
		}
	}

	/**
	 *添加紧急情况等级相同
	 * @param exName
	 * @param level
	 * @return
	 * @author wanglei
	 */
	@PostMapping("/addExigencyInfoWithSameLevel")
	@RequiresPermissions("addEmergencyLevel")
	@ResponseBody
	public ResponseResult addExigencyInfoWithSameLevel(String exName,Integer level,Integer sameCode){

		try{
			log.info("有重复等级添加紧急情况!");
			exigencyInfoService.addExigencyInfoWithSameLevel(exName,level,sameCode);
			return ResponseResult.success(ResponseResultEnum.SUCCESS.getCode(),"","添加成功!");
		}catch(Exception e){
			log.error("有重复等级添加紧急情况失败!");
			return ResponseResult.failure(ResponseResultEnum.FAILURE.getCode(),"添加失败!");
		}
	}


}
