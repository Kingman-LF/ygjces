package com.xwkj.ygjces.service.impl;

import com.xwkj.ygjces.Mapper.ApproverItemRelatedMapper;
import com.xwkj.ygjces.Mapper.AuditorItemRelatedMapper;
import com.xwkj.ygjces.Mapper_oracle.JcXtcsXmCategoryMapperManual;
import com.xwkj.ygjces.model.ApproverItemRelated;
import com.xwkj.ygjces.model.AuditorItemRelated;
import com.xwkj.ygjces.service.JcXmWithPersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.Transient;

@Service
public class JcXmWithPersonServiceImpl implements JcXmWithPersonService {

	@Autowired
	JcXtcsXmCategoryMapperManual jcXtcsXmCategoryMapperManual;

	@Autowired
	ApproverItemRelatedMapper approverItemRelatedMapper;

	@Autowired
	AuditorItemRelatedMapper auditorItemRelatedMapper;

	/**
	 * 检测项和审核人绑定
	 * @param permIds
	 * @param permName
	 * @param checkedIds
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void bindJcXmWithReviewPerson(String permIds,String permName,String checkedIds){
		//检测项Id
		String[] categoryIds = permIds.split(",");
		//检测项名字
		String[] categoryNames = permName.split(",");
		//审核人Id
		String[] userIds = checkedIds.split(",");

		//根据Id查询是否是父节点
		for (int i = 0;i < categoryIds.length; i++) {
			Integer count  = jcXtcsXmCategoryMapperManual.selectExistById(categoryIds[i]);
			if(count > 0){
				//移除索引i元素
				categoryIds[i] = null;
				categoryNames[i] = null;
			}
		}
		//首先删除这个审批之前绑定检测项
		for (int j = 0; j < userIds.length ; j++) {
			//查询是否有
			Integer userBindCount = auditorItemRelatedMapper.selectByUserId(userIds[j]);
			if(userBindCount > 0){
				auditorItemRelatedMapper.deleteByUserId(userIds[j]);
			}
		}

		for (int i = 0; i <categoryIds.length ; i++) {
			if (categoryIds[i]!=null && (!categoryIds[i].equals(""))){
				//检测项插入
				for (int j = 0; j < userIds.length ; j++) {
					//审批人绑定
					AuditorItemRelated auditorItemRelated = new AuditorItemRelated();
					auditorItemRelated.setItemId(categoryIds[i]);
					auditorItemRelated.setItemName(categoryNames[i]);
					auditorItemRelated.setUserId(userIds[j]);
					auditorItemRelatedMapper.insertAuditorItemRelated(auditorItemRelated);
				}
			}
		}

	}

	/**
	 * 检测项和批准人绑定
	 * @param permIds
	 * @param permName
	 * @param checkedIds
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void bindJcXmWithApprovePerson(String permIds,String permName,String checkedIds){
		//检测项Id
		String[] categoryIds = permIds.split(",");
		//检测项名字
		String[] categoryNames = permName.split(",");
		//批准人Id
		String[] userIds = checkedIds.split(",");

		//根据Id查询是否是父节点
		for (int i = 0;i < categoryIds.length; i++) {
			Integer count  = jcXtcsXmCategoryMapperManual.selectExistById(categoryIds[i]);
			if(count > 0){
				//移除索引i元素
				categoryIds[i] = null;
				categoryNames[i] = null;
			}
		}
		//首先删除这个审批之前绑定检测项
		for (int j = 0; j < userIds.length ; j++) {
			//查询是否有
			Integer userBindCount = approverItemRelatedMapper.selectByUserId(userIds[j]);
			if(userBindCount > 0){
				approverItemRelatedMapper.deleteByUserId(userIds[j]);
			}
		}

		for (int i = 0; i <categoryIds.length ; i++) {
			if (categoryIds[i]!=null && (!categoryIds[i].equals(""))){
				//检测项插入
				for (int j = 0; j < userIds.length ; j++) {
					//审批人绑定
					ApproverItemRelated approverItemRelated = new ApproverItemRelated();
					approverItemRelated.setItemId(categoryIds[i]);
					approverItemRelated.setItemName(categoryNames[i]);
					approverItemRelated.setUserId(userIds[j]);
					approverItemRelatedMapper.insertApproverItemRelated(approverItemRelated);
				}
			}
		}

	}
}
