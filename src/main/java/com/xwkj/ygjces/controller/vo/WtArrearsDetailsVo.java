package com.xwkj.ygjces.controller.vo;

import lombok.Getter;
import lombok.Setter;

public class WtArrearsDetailsVo {
	/**
	 * 主键
	 */
	@Setter
	@Getter
	private String syNum;

	/**
	 * 委托单位
	 */
	@Setter
	@Getter
	private String wtUnit;

	/**
	 * 欠款金额
	 */
	@Setter
	@Getter
	private Integer arrearsAmount;
}
