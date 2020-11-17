package com.xwkj.ygjces.model;

import lombok.Getter;
import lombok.Setter;

/**
 * 项目分类树实体
 */
public class JcXtcsXmCategory {

	/**
	 * 主键
	 */
	@Setter
	@Getter
	private String categoryId;

	/**
	 * 项目分类名称
	 */
	@Setter
	@Getter
	private String categoryName;

	/**
	 * 父ID
	 */
	@Setter
	@Getter
	private String pid;


}
