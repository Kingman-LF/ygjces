package com.xwkj.ygjces.service;

import com.github.pagehelper.PageInfo;
import com.xwkj.ygjces.model.ExigencyInfo;

/**
 * 紧急程度管理业务接口
 */
public interface ExigencyInfoService {

	/**
	 *紧急程度表分页显示
	 * @param exName
	 * @param isEnable
	 * @param page
	 * @param limit
	 * @return
	 * @author wanglei
	 */
	public PageInfo<ExigencyInfo> getExigencyInfoPage(String exName,Integer isEnable,Integer page, Integer limit);

	/**
	 * 修改启用状态
	 * @param pid
	 * @param isEnable
	 * @author wanglei
	 */
	public void updateExigency(Long pid , Integer isEnable);

	/**
	 *添加紧急程度信息功能
	 * @param exName
	 * @param level
	 * @author wanglei
	 */
	public void addExigencyInfo(String exName,Integer level);

	/**
	 *添加紧急程度信息功能
	 * @param exName
	 * @param level
	 * @param sameCode
	 * @author wanglei
	 */
	public void addExigencyInfoWithSameLevel(String exName,Integer level,Integer sameCode);


	/**
	 * 根据等级查询紧急程度信息
	 * @param level
	 * @return
	 * @author wanglei
	 */
	public Integer countExigencyInfoByLevel(Integer level);
}
