package com.xwkj.ygjces.service.impl;

import com.xwkj.ygjces.Mapper.ApproverInfoMapper;
import com.xwkj.ygjces.service.ApproverInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.Transient;

@Service
public class ApproverInfoServiceImpl implements ApproverInfoService {

	@Autowired
	ApproverInfoMapper approverInfoMapper;

	/**
	 * 返回统计该用户是不是审批人
	 * * @param id
	 * @return
	 * @author wanglei
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Integer selectCountApproverInfoByUserId(Long id){
		return approverInfoMapper.selectCountApproverInfoByUserId(id);
	}

	/**
	 * 设置审批人功能实现
	 * @param userId
	 * @author wanglei
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void addApprovalInfo(Long userId){
		approverInfoMapper.insertApprovalInfo(userId);
	}

	/**
	 * 设置审批人功能实现
	 * @param userId
	 * @author wanglei
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void removeApprovalInfoByUserId(Long userId){
		approverInfoMapper.removeApprovalInfo(userId);
	}

}
