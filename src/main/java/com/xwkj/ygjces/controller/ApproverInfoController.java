package com.xwkj.ygjces.controller;

import com.github.pagehelper.PageInfo;
import com.xwkj.ygjces.common.ResponseResult;
import com.xwkj.ygjces.common.ResponseResultEnum;
import com.xwkj.ygjces.model.UserInfo;
import com.xwkj.ygjces.service.ApproverInfoService;
import com.xwkj.ygjces.service.UserInfoService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ApproverInfoController {

	private static Log log= LogFactory.getLog(ApproverInfoController.class);

	@Autowired
	UserInfoService userInfoService;

	@Autowired
	ApproverInfoService approverInfoService;
	/**
	 * 跳转绑定审批人页面
	 * @return
	 * @author wanglei
	 */
	@RequestMapping("/toBindingApproverPersonPage")
	public String toBindingApproverPersonPage(){
		return "statistics/BindApproverPerson";
	}

	/**
	 * 返回用户信息并查看是够是审批人
	 *
	 * * @return
	 */
	@PostMapping("/showUserInfoListWithUserId")
	@ResponseBody
	public ResponseResult showUserInfoListWithUserId(UserInfo userInfo, Integer page, Integer limit){
		if(page == null){
			page = 1;
		}
		if(limit == null){
			limit = 10;
		}
		PageInfo<UserInfo> userInfos = userInfoService.getUserInfoByApprovelId(userInfo,page,limit);
		return ResponseResult.success(ResponseResultEnum.SUCCESS.getCode(), userInfos, "获取用户列表成功！");
	}

	/**
	 * 设置为审批人
	 * @param userId
	 * @return
	 */
	@PostMapping("/addApprovalInfo")
	@ResponseBody
	public ResponseResult addApprovalInfo(Long userId){
		try {
			log.info("设置审批人!");
			approverInfoService.addApprovalInfo(userId);
			return ResponseResult.success(ResponseResultEnum.SUCCESS.getCode(),null,"设置审批人成功!");
		}catch (Exception e){
			log.error("设置审批人失败!");
			return ResponseResult.failure("设置审批人失败!");
		}
	}

	/**
	 * 取消审批人
	 * @param userId
	 * @return
	 */
	@PostMapping("/removeApprovalInfoByUserId")
	@ResponseBody
	public ResponseResult removeApprovalInfoByUserId(Long userId){
		try {
			log.info("取消审批人!");
			approverInfoService.removeApprovalInfoByUserId(userId);
			return ResponseResult.success(ResponseResultEnum.SUCCESS.getCode(),null,"取消审批人成功!");
		}catch (Exception e){
			log.error("取消审批人失败!");
			return ResponseResult.failure("取消审批人失败!");
		}
	}


}
