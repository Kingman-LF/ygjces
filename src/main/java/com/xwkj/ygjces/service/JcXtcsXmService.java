package com.xwkj.ygjces.service;


import com.github.pagehelper.PageInfo;
import com.xwkj.ygjces.controller.vo.JcXmCategoryVo;
import com.xwkj.ygjces.controller.vo.JcXtcsXmWithSampleVo;

import java.util.Date;
import java.util.List;

/**
 * 检测项功能层
 */
public interface JcXtcsXmService {

	/**
	 * 返回用户绑定项目树
	 *@author wanglei
	 */
	public List<JcXmCategoryVo> getJcXmCategoryByUserId(String id);

	/**
	 * 返回项目树
	 * @return
	 * @author wanglei
	 */
	public List<JcXmCategoryVo> getJcXmCategory();

	/**
	 * 检测项两年的数量统计功能层
	 * @param startYear
	 * @param endYear
	 * @return
	 * @author wanglei
	 */
	public List<Object> getCountJcXtcsXm(Integer startYear,Integer endYear);

	/**
	 *检测项统计所有子项数量功能层
	 * @param defaultDate
	 * @param xmName
	 * @param start
	 * @param end
	 * @param pageNum
	 * @param pageSize
	 * @return
	 * @author wanglei
	 */
	public PageInfo<JcXtcsXmWithSampleVo> getJcXtcsXmNameAndCount(String defaultDate,String xmName,Date start,Date end, Integer pageNum, Integer pageSize);

	List<Object> getCountJcXtcsXmWithMonth(String start, String end);

	PageInfo<JcXtcsXmWithSampleVo> getJcXtcsXmNameAndCountWithMonth(String defaultDate, String xmName, Date start, Date end, Integer pageNum, Integer pageSize);

	List<Object> getCountJcXtcsXmAmount(Date startTime,Date endTime);
}
