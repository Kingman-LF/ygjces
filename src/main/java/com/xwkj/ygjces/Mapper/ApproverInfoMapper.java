package com.xwkj.ygjces.Mapper;

import org.springframework.stereotype.Repository;

@Repository
public interface ApproverInfoMapper {
	/**
	 *统计审批人是够存在
	 * @param id
	 * @return
	 */
	public Integer selectCountApproverInfoByUserId(Long id);

	/**
	 * 设置审批人
	 * @param userId
	 */
	public void insertApprovalInfo(Long userId);

	/**
	 * 取消审批人
	 * @param userId
	 */
	public void removeApprovalInfo(Long userId);


}
