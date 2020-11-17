package com.xwkj.ygjces.controller;

import com.xwkj.ygjces.common.ResponseResult;
import com.xwkj.ygjces.common.ResponseResultEnum;
import com.xwkj.ygjces.service.JcXmWithPersonService;
import com.xwkj.ygjces.service.JcXtcsXmService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 检测项和人员绑定控制层
 * @author wanglei
 */
@Controller
public class JcXmWithPersonController {

	@Autowired
	JcXtcsXmService jcXtcsXmService;

	@Autowired
	JcXmWithPersonService jcXmWithPersonService;

	/**
	 * 去往批准人绑定检测项的页面
	 */
	@RequestMapping("/toBindingJcXmAndApprovePage")
	@RequiresPermissions("auditorBindDetectionManager")
	public String toBindingJcXmAndApprovePage(){return "statistics/approver-jcxm";}

	/**
	 * 去往审核人绑定检测项的页面
	 */
	@RequestMapping("/toBindingJcXmAndReviewPage")
	@RequiresPermissions("approveBindDetectionManager")
	public String toBindingJcXmAndReviewPage(){return "statistics/review-jcxm";}

	/**
	 *审核人绑定检测项
	 * @param permIds
	 * @param permName
	 * @param checkedIds
	 * @return
	 * @author wanglei
	 * */
	@PostMapping("/bindJcXmWithReviewPerson")
	@RequiresPermissions("approveBindDetectionManager")
	@ResponseBody
	public ResponseResult bindJcXmWithReviewPerson(@RequestParam(value = "permIds") String permIds,@RequestParam(value = "permName") String permName,@RequestParam(value = "checkedIds") String checkedIds){
		try{
			jcXmWithPersonService.bindJcXmWithReviewPerson(permIds,permName,checkedIds);
			return ResponseResult.success(1,null,"绑定成功!");
		}catch (Exception e){
			e.printStackTrace();
			return ResponseResult.failure("绑定失败!");
		}
	}


	/**
	 *批准人绑定检测项
	 * @param permIds
	 * @param permName
	 * @param checkedIds
	 * @return
	 * @author wanglei
	 * */
	@PostMapping("bindJcXmWithApprovePerson")
	@RequiresPermissions("auditorBindDetectionManager")
	@ResponseBody
	public ResponseResult bindJcXmWithApprovePerson(@RequestParam(value = "permIds") String permIds,@RequestParam(value = "permName") String permName,@RequestParam(value = "checkedIds") String checkedIds){
//		//不是修改页面
//		if (!childPage.equals(1)){
//			if(permIds == null || permIds.equals("")){
//				return ResponseResult.failure(2,"请选择要绑定检测项!");
//			}
//		}
//		if(checkedIds == null || checkedIds.equals("")){
//			return ResponseResult.failure(3,"请选择要绑定审核人!");
//		}
		try{
			jcXmWithPersonService.bindJcXmWithApprovePerson(permIds,permName,checkedIds);
			return ResponseResult.success(1,null,"绑定成功!");
		}catch (Exception e){
			e.printStackTrace();
			return ResponseResult.failure("绑定失败!");
		}
	}
}
