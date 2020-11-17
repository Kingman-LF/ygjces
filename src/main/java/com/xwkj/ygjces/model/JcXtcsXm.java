package com.xwkj.ygjces.model;


import lombok.Getter;
import lombok.Setter;

/**
 * 检测项目设置表实体
 */
public class JcXtcsXm {

	/**
	 * 主键
	 */
	@Setter
	@Getter
	private String xmId;

	/**
	 * 项目名称
	 */
	@Setter
	@Getter
	private String xmName;

	/**
	 * 关联项目分类树外键
	 */
	@Setter
	@Getter
	private String categoryId;
}
