package com.xwkj.ygjces.controller.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * 检测项关联样品表Vo
 */
public class JcXtcsXmWithSampleVo {

	/**
	 * 主键
	 */
	@Setter
	@Getter
	private String syNum;

	/**
	 * 项目名称
	 */
	@Setter
	@Getter
	private String xmName;

	/**
	 * 检测项目数量
	 */
	@Setter
	@Getter
	private Integer xmCount;

}
