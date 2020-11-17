package com.xwkj.ygjces.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xwkj.ygjces.Mapper.ApproverItemRelatedMapper;
import com.xwkj.ygjces.Mapper_oracle.JcCoreSampleMapperManual;
import com.xwkj.ygjces.Mapper_oracle.JcXtcsXmCategoryMapperManual;
import com.xwkj.ygjces.Mapper_oracle.JcXtcsXmMapperManual;
import com.xwkj.ygjces.controller.vo.JcXmCategoryAmountVo;
import com.xwkj.ygjces.controller.vo.JcXmCategoryVo;
import com.xwkj.ygjces.controller.vo.JcXtcsXmWithSampleVo;
import com.xwkj.ygjces.model.JcXtcsXmCategory;
import com.xwkj.ygjces.service.JcXtcsXmService;
import com.xwkj.ygjces.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class JcXtcsXmServiceImpl  implements JcXtcsXmService{

	@Autowired
	JcXtcsXmCategoryMapperManual jcXtcsXmCategoryMapperManual;

	@Autowired
	JcCoreSampleMapperManual jcCoreSampleMapperManual;

	@Autowired
	JcXtcsXmMapperManual jcXtcsXmMapperManual;

	@Autowired
	ApproverItemRelatedMapper approverItemRelatedMapper;

	/**
	 * 返回用户绑定的检测项树
	 * @param id
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public List<JcXmCategoryVo> getJcXmCategoryByUserId(String id){
		//根据用户查询到categoryId
		List<String> categoryIds = approverItemRelatedMapper.selectCategoryIdByUserId(id);
		//查询项目分类
		List<JcXmCategoryVo> jcXtcsXmCategoryList = jcXtcsXmCategoryMapperManual.getJcXtcsXmCategoryList();
		//查询项目分类下项目
		List<JcXmCategoryVo> jcXmList = jcXtcsXmMapperManual.getjcXmList();
		for (int i = 0; i < categoryIds.size(); i++) {
			for (int j = 0; j < jcXmList.size(); j++) {
				if (jcXmList.get(j).getCategoryId().equals(categoryIds.get(i))){
					jcXmList.get(j).setChecked(true);
					break;
				}
			}
		}
		jcXtcsXmCategoryList.addAll(jcXmList);
		return jcXtcsXmCategoryList;
	}

	/**
	 * 返回项目树功能
	 * @return
	 * @author wanglei
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public List<JcXmCategoryVo> getJcXmCategory(){
		//查询项目分类
		List<JcXmCategoryVo> jcXtcsXmCategoryList = jcXtcsXmCategoryMapperManual.getJcXtcsXmCategoryList();
		//查询项目分类下项目
		List<JcXmCategoryVo> jcXmList = jcXtcsXmMapperManual.getjcXmList();
		jcXtcsXmCategoryList.addAll(jcXmList);
		return jcXtcsXmCategoryList;
	}

	/**
	 * 检测项两年的数量统计功能层实现
	 * @param startYear
	 * @param endYear
	 * @return
	 * @author wanglei
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public List<Object> getCountJcXtcsXm(Integer startYear,Integer endYear){
		//返回需要的数据
		List<Object> requireList = new ArrayList<>();
		// 年份返回返回筛选时间的最近两年
		List<Object> recentDate = new ArrayList<>();
		// 返回检测类九大项
		List<String> jcXtcsXmList = new ArrayList<>();
		// 返回统计检测量
		List<Integer> jcXtcsXmCount = new ArrayList<>();
		if(startYear == null && endYear == null){
			//默认返回系统当前年份和去年
			Calendar calendar = Calendar.getInstance();
			recentDate.add(calendar.get(Calendar.YEAR));
			calendar.add(Calendar.YEAR,-1);
			recentDate.add(calendar.get(Calendar.YEAR));
		}else{
			recentDate.add(endYear);
			recentDate.add(startYear);
		}
		// 查询检测类(九大项)
		List<JcXtcsXmCategory> list =  jcXtcsXmCategoryMapperManual.getJcXtcsXm();
		for (JcXtcsXmCategory jcXtcsXmCategory:list) {
			jcXtcsXmList.add(jcXtcsXmCategory.getCategoryName());
		}
		for(Object object : recentDate){
			//按照时间统计
			for (JcXtcsXmCategory jcXtcsXmCategory:list) {
				Integer xmCount = jcCoreSampleMapperManual.getJcXtcsXmCount(jcXtcsXmCategory.getCategoryId(),object);
				jcXtcsXmCount.add(xmCount);
			}
		}
		requireList.add(recentDate);
		requireList.add(jcXtcsXmList);
		requireList.add(jcXtcsXmCount);
		return requireList;
	}

	/**
	 *检测项统计所有子项数量功能层实现
	 * @param defaultDate
	 * @param xmName
	 * @param start
	 * @param end
	 * @param pageNum
	 * @param pageSize
	 * @return
	 * @author wanglei
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public PageInfo<JcXtcsXmWithSampleVo> getJcXtcsXmNameAndCount(String defaultDate,String xmName,Date start,Date end, Integer pageNum, Integer pageSize){
		PageHelper.startPage(pageNum , pageSize);
		List<JcXtcsXmWithSampleVo> jcXtcsXmWithSampleVo = jcCoreSampleMapperManual.getJcXtcsXmNameAndCount(defaultDate,xmName,start,end);
		return new PageInfo<JcXtcsXmWithSampleVo>(jcXtcsXmWithSampleVo);
	}

	@Override
	public List<Object> getCountJcXtcsXmWithMonth(String start, String end) {
		//返回需要的数据
		List<Object> requireList = new ArrayList<>();
		// 月份返回返回筛选时间的最近两个月
		List<Object> recentDate = new ArrayList<>();
		// 返回检测类九大项
		List<String> jcXtcsXmList = new ArrayList<>();
		// 返回统计检测量
		List<Integer> jcXtcsXmCount = new ArrayList<>();
		if(start == null && end == null){
			//默认返回系统当前月份和上个月
			Calendar calendar = Calendar.getInstance();
			int first = calendar.get(Calendar.MONTH) + 1;
			if (first<10){
				recentDate.add(calendar.get(Calendar.YEAR)+"-0"+first);
			}else{
				recentDate.add(calendar.get(Calendar.YEAR)+"-"+first);
			}
			calendar.add(Calendar.MONTH,-1);
			first = calendar.get(Calendar.MONTH) + 1;
			if (first<10){
				recentDate.add(calendar.get(Calendar.YEAR)+"-0"+first);
			}else{
				recentDate.add(calendar.get(Calendar.YEAR)+"-"+first);
			}
		}else{
			recentDate.add(end.trim());
			recentDate.add(start.trim());
		}
		// 查询检测类(九大项)
		List<JcXtcsXmCategory> list =  jcXtcsXmCategoryMapperManual.getJcXtcsXm();
		for (JcXtcsXmCategory jcXtcsXmCategory:list) {
			jcXtcsXmList.add(jcXtcsXmCategory.getCategoryName());
		}
		for(Object object : recentDate){
			//按照时间统计
			for (JcXtcsXmCategory jcXtcsXmCategory:list) {
				Integer xmCount = jcCoreSampleMapperManual.getJcXtcsXmCountWithMonth(jcXtcsXmCategory.getCategoryId(),object);
				jcXtcsXmCount.add(xmCount);
			}
		}
		requireList.add(recentDate);
		requireList.add(jcXtcsXmList);
		requireList.add(jcXtcsXmCount);
		return requireList;
	}

	@Override
	public PageInfo<JcXtcsXmWithSampleVo> getJcXtcsXmNameAndCountWithMonth(String defaultDate, String xmName, Date start, Date end, Integer pageNum, Integer pageSize) {
		PageHelper.startPage(pageNum , pageSize);
		List<JcXtcsXmWithSampleVo> jcXtcsXmWithSampleVo = jcCoreSampleMapperManual.getJcXtcsXmNameAndCountWithMonth(defaultDate,xmName,start,end);
		return new PageInfo<JcXtcsXmWithSampleVo>(jcXtcsXmWithSampleVo);
	}

	/**
	 * 检测项金额统计
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	@Override
	public List<Object> getCountJcXtcsXmAmount(Date startTime, Date endTime) {
		List<Object> list = new ArrayList<>();
		List<JcXmCategoryAmountVo> jcXmCategoryAmountVos = new ArrayList<>();
		//检测项金额统计对象
		JcXmCategoryAmountVo jcXmCategoryAmountVo = new JcXmCategoryAmountVo();
		//应收总额
		Long countJcXtcsXmSfMon = 0L;
		//实收总额
		Long countJcXtcsXmRealMon = 0L;
		//已收总额
		Long countJcXtcsXmSfReal = 0L;
		//默认条件查询一个月
		Calendar calendar = Calendar.getInstance();
		//返回当前时间年月
		String currYearMonth = DateUtil.dateToString(calendar.getTime(), DateUtil.YYYY_MM);
		// 查询检测类(九大项)
		List<JcXtcsXmCategory> jcXtcsXmCategories =  jcXtcsXmCategoryMapperManual.getJcXtcsXm();
		for (JcXtcsXmCategory jcXtcsXmCategory : jcXtcsXmCategories) {
			if (jcXtcsXmCategory != null) {
				jcXmCategoryAmountVo = jcCoreSampleMapperManual.getCountJcXtcsXmAmount(jcXtcsXmCategory.getCategoryId(),currYearMonth,startTime,endTime);
				if (jcXmCategoryAmountVo != null) {
					if (jcXmCategoryAmountVo.getSfRealMoney() == null){
						jcXmCategoryAmountVo.setSfRealMoney(0L);
					}
					if (jcXmCategoryAmountVo.getRealMoney() == null){
						jcXmCategoryAmountVo.setSfRealMoney(0L);
					}
					if (jcXmCategoryAmountVo.getSfmon() == null){
						jcXmCategoryAmountVo.setSfmon(0L);
					}
					if (jcXmCategoryAmountVo.getSfmon()!=null){
						countJcXtcsXmSfMon += jcXmCategoryAmountVo.getSfmon();
					}
					if (jcXmCategoryAmountVo.getRealMoney()!=null){
						countJcXtcsXmRealMon += jcXmCategoryAmountVo.getRealMoney();
					}

					if (jcXmCategoryAmountVo.getSfRealMoney()!=null){
						countJcXtcsXmSfReal += jcXmCategoryAmountVo.getSfRealMoney();
					}
					jcXmCategoryAmountVo.setJcXmName(jcXtcsXmCategory.getCategoryName());
					jcXmCategoryAmountVos.add(jcXmCategoryAmountVo);
				}
			}
		}
		list.add(jcXmCategoryAmountVos);
		list.add(countJcXtcsXmSfMon);
		list.add(countJcXtcsXmRealMon);
		list.add(countJcXtcsXmSfReal);
		return list;
	}

}
