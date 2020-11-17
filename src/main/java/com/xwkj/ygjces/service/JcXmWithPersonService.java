package com.xwkj.ygjces.service;


public interface JcXmWithPersonService {
	/**
	 * 批准人绑定检测项
	 * @param permName
	 * @param permIds
	 * @param checkedIds
	 * @author wanglei
	 */
	public void bindJcXmWithApprovePerson(String permIds,String permName,String checkedIds);

	/**
	 * 审核人绑定检测项
	 * @param permName
	 * @param permIds
	 * @param checkedIds
	 * @author wanglei
	 */
	public void bindJcXmWithReviewPerson(String permIds,String permName,String checkedIds);

}
