package com.xwkj.ygjces.Mapper;

import com.xwkj.ygjces.model.ExigencyInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 紧急程度Mapper
 */
@Repository
public interface ExigencyInfoMapper {

	/**
	 *查询所有紧急程度表
	 * @param exName
	 * @param isEnable
	 * @return
	 * @author wanglei
	 */
	public List<ExigencyInfo> getExigencyInfoList(@Param("exName") String exName,@Param("isEnable") Integer isEnable);

	/**
	 *修改启用状态
	 * @param exigencyInfo
	 * @author wanglei
	 */
	public void updateExigency(ExigencyInfo exigencyInfo);

	/**
	 * 根据等级统计
	 * @param level
	 * @return
	 * @author wanglei
	 */
	public Integer selectCountExigencyInfoByLevel(Integer level);

	/**
	 * 添加紧急程度信息
	 * @param exigencyInfo
	 * @author wanglei
	 */
	public void insertExigencyInfo(ExigencyInfo exigencyInfo);

	/**
	 * 将大于等于级别加1
	 * @param updatexigencyInfoByLevel
	 */
	public void updateAddLevelByCurrLevel(ExigencyInfo updatexigencyInfoByLevel);

	/**
	 * 根据等级获取紧急程度信息
	 * @param level
	 * @return
	 * @author zyh
	 */
	ExigencyInfo getExigencyInfoByLevel(Integer level);
}
