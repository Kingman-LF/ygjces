package com.xwkj.ygjces.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 紧急程度表
 */
public class ExigencyInfo {

	/**
	 * 主键
	 */
	@Setter
	@Getter
	private Long pid;

	/**
	 *程度名称
	 */
	@Setter
	@Getter
	private String exName;

	/**
	 *程度等级
	 */
	@Setter
	@Getter
	private Integer level;

	/**
	 *创建时间
	 */
	@Setter
	@Getter
	private Date createTime;

	/**
	 *创建人
	 */
	@Setter
	@Getter
	private Long createUserId;

	/**
	 *最后修改时间
	 */
	@Setter
	@Getter
	private Date lastModifiedTime;

	/**
	 *最后修改人
	 */
	@Setter
	@Getter
	private Long lastModifier;

	/**
	 *是否启用
	 */
	@Setter
	@Getter
	private Integer isEnable;

}
