package com.xwkj.ygjces.service;


public interface ApproverInfoService {

	/**
	 * 返回统计该用户是不是审批人
	 * * @param id
	 * @return
	 */
	public Integer selectCountApproverInfoByUserId(Long id);

	/**
	 * 设置审批人功能
	 * @param userId
	 */
	public void addApprovalInfo(Long userId);

	/**
	 * 取消审批人功能
	 * @param userId
	 */
	public void removeApprovalInfoByUserId(Long userId);

}
