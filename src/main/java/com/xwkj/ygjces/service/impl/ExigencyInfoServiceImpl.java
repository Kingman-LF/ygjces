package com.xwkj.ygjces.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xwkj.ygjces.Mapper.ExigencyInfoMapper;
import com.xwkj.ygjces.common.LoginUserInfoManager;
import com.xwkj.ygjces.model.ExigencyInfo;
import com.xwkj.ygjces.model.UserInfo;
import com.xwkj.ygjces.service.ExigencyInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * 紧急程度管理业务接口实现
 */
@Service
public class ExigencyInfoServiceImpl implements ExigencyInfoService {

	@Autowired
	ExigencyInfoMapper exigencyInfoMapper;

	/**
	 *紧急程度分页查询
	 * @param exName
	 * @param isEnable
	 * @param page
	 * @param limit
	 * @return
	 * @author wanglei
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public PageInfo<ExigencyInfo> getExigencyInfoPage(String exName,Integer isEnable,Integer page, Integer limit){
		PageHelper.startPage(page , limit);
		List<ExigencyInfo> exigencyInfoList = exigencyInfoMapper.getExigencyInfoList(exName,isEnable);
		return new PageInfo<ExigencyInfo>(exigencyInfoList);
	}

	/**
	 *修改启用状态
	 * @param pid
	 * @param isEnable
	 * @author wanglei
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateExigency(Long pid , Integer isEnable){
		//修改人,修改时间
		UserInfo userInfo = LoginUserInfoManager.getUserInfo();
		ExigencyInfo exigencyInfo = new ExigencyInfo();
		exigencyInfo.setPid(pid);
		exigencyInfo.setIsEnable(isEnable);
		exigencyInfo.setLastModifiedTime(new Date());
		exigencyInfo.setLastModifier(userInfo.getId());
		exigencyInfoMapper.updateExigency(exigencyInfo);
	}

	/**
	 * 添加紧急情况信息等级相同
	 * @param exName
	 * @param level
	 * @author wanglei
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void addExigencyInfoWithSameLevel(String exName,Integer level,Integer sameCode){
		//存放等级
		List<Integer> levels = new ArrayList<Integer>();
		//查询相同的等级信息
		List<ExigencyInfo> exigencyInfos = exigencyInfoMapper.getExigencyInfoList(null,null);
		for (ExigencyInfo exigencyInfo:exigencyInfos) {
			levels.add(exigencyInfo.getLevel());
		}
		Integer maxLevel = Collections.max(levels);
		UserInfo userInfo = LoginUserInfoManager.getUserInfo();
		if(sameCode.equals(1)){
			//升级成最高级
			ExigencyInfo exigencyInfo = new ExigencyInfo();
			exigencyInfo.setCreateUserId(userInfo.getId());
			exigencyInfo.setCreateTime(new Date());
			exigencyInfo.setExName(exName);
			exigencyInfo.setIsEnable(1);
			exigencyInfo.setLevel(maxLevel+1);
			exigencyInfoMapper.insertExigencyInfo(exigencyInfo);
		}
		if(sameCode.equals(2)){
			ExigencyInfo updatexigencyInfoByLevel = new ExigencyInfo();
			updatexigencyInfoByLevel.setLevel(level);
			updatexigencyInfoByLevel.setLastModifiedTime(new Date());
			updatexigencyInfoByLevel.setLastModifier(userInfo.getId());
			//级别大于等于级别加1
			exigencyInfoMapper.updateAddLevelByCurrLevel(updatexigencyInfoByLevel);
			//插入该级别
			ExigencyInfo exigencyInfo = new ExigencyInfo();
			exigencyInfo.setCreateUserId(userInfo.getId());
			exigencyInfo.setCreateTime(new Date());
			exigencyInfo.setExName(exName);
			exigencyInfo.setIsEnable(1);
			exigencyInfo.setLevel(level);
			exigencyInfoMapper.insertExigencyInfo(exigencyInfo);

		}
	}

	/**
	 * 添加紧急情况信息
	 * @param exName
	 * @param level
	 * @author wanglei
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void addExigencyInfo(String exName,Integer level){

		UserInfo userInfo = LoginUserInfoManager.getUserInfo();
		ExigencyInfo exigencyInfo = new ExigencyInfo();
		exigencyInfo.setCreateUserId(userInfo.getId());
		exigencyInfo.setCreateTime(new Date());
		exigencyInfo.setExName(exName);
		exigencyInfo.setIsEnable(1);
		exigencyInfo.setLevel(level);
		exigencyInfoMapper.insertExigencyInfo(exigencyInfo);
	}

	/**
	 * 根据等级查询
	 * @param level
	 * @return
	 * @author wanglei
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Integer countExigencyInfoByLevel(Integer level) {
		return exigencyInfoMapper.selectCountExigencyInfoByLevel(level);
	}


}
