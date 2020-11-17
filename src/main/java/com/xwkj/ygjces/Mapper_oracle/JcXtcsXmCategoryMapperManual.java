package com.xwkj.ygjces.Mapper_oracle;

import com.xwkj.ygjces.controller.vo.JcXmCategoryVo;
import com.xwkj.ygjces.model.JcXtcsXmCategory;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface JcXtcsXmCategoryMapperManual {

	/**
	 * 统计所有项目分类
	 * @return
	 */
	public List<JcXmCategoryVo> getJcXtcsXmCategoryList();


	/**
	 * 统计所有项目(下直属分类名称)Dao接口
	 * @return
	 * @author wanglei
	 */
	public List<JcXtcsXmCategory> getJcXtcsXm();

	/**
	 * 主键查询是否存在
	 * @param categoryId
	 * @return
	 */
	public Integer selectExistById(String categoryId);
}
