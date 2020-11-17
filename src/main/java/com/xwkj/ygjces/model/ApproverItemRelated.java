package com.xwkj.ygjces.model;

import lombok.Getter;
import lombok.Setter;

/**
 * 批准人与项目中间表实体类
 */
public class ApproverItemRelated {

	/**
	 * 主键
	 */
	@Setter
	@Getter
	private Long id;

	/**
	 * 用户ID
	 */
	@Setter
	@Getter
	private String userId;

	/**
	 * 项目id
	 */
	@Setter
	@Getter
	private String itemId;

	/**
	 * 项目名称
	 * */
	@Setter
	@Getter
	private String itemName;

}
