package com.xwkj.ygjces.controller.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * @author wanglei
 * @create 2019-10-15 14:05
 */
public class JcXmCategoryAmountVo {
	/**
	 * 检测项名称
	 */
	@Setter
	@Getter
	private String jcXmName;


	/**
	 应收金额
	 */
	@Setter
	@Getter
	private Long sfmon;

	/**
	 实收金额
	 */
	@Setter
	@Getter
	private Long realMoney;

	/**
	 已收金额
	 */
	@Setter
	@Getter
	private Long sfRealMoney;
}
