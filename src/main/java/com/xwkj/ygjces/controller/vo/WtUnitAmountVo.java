package com.xwkj.ygjces.controller.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * @author wanglei
 * @create 2019-10-14 11:35
 */
public class WtUnitAmountVo {

	/**
	 * 委托单位
	 */
	@Setter
	@Getter
	private String wtUnit;


	/**
	应收金额
	 */
	@Setter
	@Getter
	private Integer sfmon;

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
