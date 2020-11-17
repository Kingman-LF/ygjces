package com.xwkj.ygjces.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xwkj.ygjces.Mapper_oracle.JcCoreSampleMapperManual;
import com.xwkj.ygjces.controller.vo.WtArrearsDetailsVo;
import com.xwkj.ygjces.controller.vo.WtUnitAmountVo;
import com.xwkj.ygjces.service.JcCoreSampleService;
import com.xwkj.ygjces.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class JcCoreSampleServiceImpl implements JcCoreSampleService {

	@Autowired
	JcCoreSampleMapperManual jcCoreSampleMapperManual;

	/**
	 *
	 * @param wtUnit
	 * @param startTime
	 * @param endTime
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public PageInfo<WtArrearsDetailsVo> getWtArrearsDetails(String wtUnit , Date startTime, Date endTime, Integer pageNum, Integer pageSize){
		PageHelper.startPage(pageNum,pageSize);
		List<WtArrearsDetailsVo> list =  jcCoreSampleMapperManual.selectWtArrearsDetails(wtUnit,startTime,endTime);
		return new PageInfo<WtArrearsDetailsVo>(list);
	}

	/**
	 *按照月份统计当年报告
	 * @return
	 * @author wanglei
	 */
	@Override
	public List<Object> getReportCountByMonth(){
		List<Object> requireTotal = new ArrayList<>();
		//当月报告数量
		List<Integer> requireReportCountByMonth = new ArrayList<>();
		//当月打印报告数量
		List<Integer> requirePrintReportCountByMonth = new ArrayList<>();
		//日期类
		Calendar calendar = Calendar.getInstance();
		//查询当前年第一天时间
		Date firstYearTime = DateUtil.getCurrYearFirst();
		//没有筛选条件,默认系统时间年12月份
		for(int i=0;i<12;i++){
			//返回年份和月份
			String currYearMonth = DateUtil.dateToString(firstYearTime, DateUtil.YYYY_MM);
			//根据年份月份查询报告数量
			Integer reportCountByMonth = jcCoreSampleMapperManual.selectReportCountByMonth(currYearMonth);
			Integer printReportCountByMonth = jcCoreSampleMapperManual.selectPrintReportCountByMonth(currYearMonth);
			requireReportCountByMonth.add(reportCountByMonth);
			requirePrintReportCountByMonth.add(printReportCountByMonth);
			//循环月份加1
			calendar.setTime(firstYearTime);
			calendar.add(Calendar.MONTH,1);
			firstYearTime = calendar.getTime();
		}
		requireTotal.add(requireReportCountByMonth);
		requireTotal.add(requirePrintReportCountByMonth);
		return requireTotal;

	}

	/**
	 *查询当天打印报告量
	 * @return
	 * @author wanglei
	 */
	@Override
	public Integer getprintReportCountWithOneDay(){
		return jcCoreSampleMapperManual.selectPrintReportCountWithOneDay();
	}

	/**
	 *查询当月打印报告量
	 * @return
	 * @author wanglei
	 */
	@Override
	public Integer getprintReportCountWithOneMonth(){
		return jcCoreSampleMapperManual.selectPrintReportCountWithOneMonth();
	}

	/**
	 * 获取委托单位的应收金额、实收金额、已收金额
	 * @return
	 */
	@Override
	public List<Object> getWtUnitAmount(String wtUnit,String sfStatus,Date startTime,Date endTime) {
//			String wtUnit1 = "廊坊市市政设施管理处";
//			List<JcCoreSample> jcCoreSample = jcCoreSampleMapperManual.getAll(wtUnit1,startTime,endTime);
//			System.out.println(jcCoreSample);
		List<Object> list = new ArrayList<>();
		//委托公司金额对象
		List<WtUnitAmountVo> wtUnitAmountVos = new ArrayList<>();
		List<WtUnitAmountVo> requireWtUnitAmountVos = new ArrayList<>();
		//应收总额
		Long countWtSfMon = 0L;
		//已收总额
		Long countWtSfReal = 0L;

		if (startTime == null && endTime == null){
			//默认条件查询一个月
			Calendar calendar = Calendar.getInstance();
			//返回当前时间年月
			String currYearMonth = DateUtil.dateToString(calendar.getTime(), DateUtil.YYYY_MM);
//			wtUnitAmountVos = jcCoreSampleMapperManual.getWtUnitWithOneMonth(currYearMonth,wtUnit,sfStatus);
			for (WtUnitAmountVo wtUnitAmountVo : wtUnitAmountVos) {
				if ( wtUnitAmountVo != null) {
					String wtUnitName = wtUnitAmountVo.getWtUnit();
					wtUnitAmountVo = jcCoreSampleMapperManual.getWtUnitAmount(null,wtUnitAmountVo.getWtUnit(),null,null,null);
					if ( wtUnitAmountVo != null) {
						if (wtUnitAmountVo.getSfmon() == null){
							wtUnitAmountVo.setSfmon(0);
						}
						if (wtUnitAmountVo.getRealMoney() == null){
							wtUnitAmountVo.setRealMoney(0L);
						}
						if (wtUnitAmountVo.getSfRealMoney() == null){
							wtUnitAmountVo.setSfRealMoney(0L);
						}
						wtUnitAmountVo.setWtUnit(wtUnitName);
						if (wtUnitAmountVo.getSfmon()!=null){
							countWtSfMon += wtUnitAmountVo.getSfmon();
						}

						if (wtUnitAmountVo.getSfRealMoney()!=null){
							countWtSfReal += wtUnitAmountVo.getSfRealMoney();
						}
						requireWtUnitAmountVos.add(wtUnitAmountVo);
					}
				}
			}
		}else{
			wtUnitAmountVos = jcCoreSampleMapperManual.getWtUnitWithOneMonth(null,wtUnit,null,startTime,endTime);
			for (WtUnitAmountVo wtUnitAmountVo : wtUnitAmountVos) {
				if (wtUnitAmountVo != null) {
					String wtUnitName = wtUnitAmountVo.getWtUnit();
					wtUnitAmountVo= jcCoreSampleMapperManual.getWtUnitAmount(null,wtUnitAmountVo.getWtUnit(),sfStatus,startTime,endTime);
					if (wtUnitAmountVo != null){
						if (wtUnitAmountVo.getSfmon() == null){
							wtUnitAmountVo.setSfmon(0);
						}
						if (wtUnitAmountVo.getRealMoney() == null){
							wtUnitAmountVo.setRealMoney(0L);
						}
						if (wtUnitAmountVo.getSfRealMoney() == null){
							wtUnitAmountVo.setSfRealMoney(0L);
						}
						if (wtUnitAmountVo.getSfmon()!=null){
							countWtSfMon += wtUnitAmountVo.getSfmon();
						}

						if (wtUnitAmountVo.getSfRealMoney()!=null){
							countWtSfReal += wtUnitAmountVo.getSfRealMoney();
						}
						wtUnitAmountVo.setWtUnit(wtUnitName);
						requireWtUnitAmountVos.add(wtUnitAmountVo);
					}
				}
			}
		}
		Collections.sort(requireWtUnitAmountVos,new Comparator<WtUnitAmountVo>() {

			@Override
			public int compare(WtUnitAmountVo o1, WtUnitAmountVo o2) {
				int i = o1.getSfmon() - o2.getSfmon();
				return i;
			}
		});
		list.add(requireWtUnitAmountVos);
		list.add(countWtSfMon);
		list.add(countWtSfReal);
		return list;
	}

}
