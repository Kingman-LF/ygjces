package com.xwkj.ygjces.Mapper_oracle;

import com.xwkj.ygjces.controller.vo.JcXmCategoryVo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JcXtcsXmMapperManual {

	/**
	 * 查询返回分类下检测项
	 * @return
	 */
	public List<JcXmCategoryVo> getjcXmList();
}
