package com.xwkj.ygjces.controller.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * 项目分类树
 */
public class JcXmCategoryVo {

	/**
	 * 主键
	 */
	@Setter
	@Getter
	private String categoryId;

	/**
	 * 节点名称
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

	/**
	 * 是否选中
	 */
	@Setter
	@Getter
	private Boolean checked;
}
