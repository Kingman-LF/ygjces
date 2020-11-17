package com.xwkj.ygjces.service;

import com.github.pagehelper.PageInfo;
import com.xwkj.ygjces.controller.vo.WtArrearsDetailsVo;

import java.util.Date;
import java.util.List;

public interface JcCoreSampleService {

	/**
	 * 委托单位欠款情况
	 * @param wtUnit
	 * @param startTime
	 * @param endTime
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public PageInfo<WtArrearsDetailsVo> getWtArrearsDetails(String wtUnit, Date startTime, Date endTime, Integer pageNum, Integer pageSize);

	/**
	 *按照月份统计当年报告
	 * @return
	 * @author wanglei
	 */
	public List<Object> getReportCountByMonth();

	/**
	 *查询当天打印报告量
	 * @return
	 * @author wanglei
	 */
	public Integer getprintReportCountWithOneDay();

	/**
	 *查询当月打印报告量
	 * @return
	 * @author wanglei
	 */
	public Integer getprintReportCountWithOneMonth();

	/**
	 * 获取委托单位的应收金额、实收金额、已收金额
	 * @return
	 */
	List<Object> getWtUnitAmount(String wtUnit, String sfStatus, Date startTime, Date endTime);

}
