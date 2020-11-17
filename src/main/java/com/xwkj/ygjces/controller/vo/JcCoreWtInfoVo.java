package com.xwkj.ygjces.controller.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 委托单合同关联Vo
 */
public class JcCoreWtInfoVo {

	/**
	 *委托主键
	 */
	@Setter
	@Getter
	private String wtId;
	/**
	 * 委托单号
	 */
	@Setter
	@Getter
	private String WtNum;
	/**
	 * 委托日期
	 */
	@Setter
	@Getter
	private Date wtDate;
	/**
	 * 委托单位
	 */
	@Setter
	@Getter
	private String wtUnit;
	/**
	 * 委托项目名称
	 */
	@Setter
	@Getter
	private String xmName;
	/**
	 * 关联合同号
	 */
	@Setter
	@Getter
	private String contractCode;
	/**
	 * 委托单实收金额
	 * */
	@Setter
	@Getter
	private String realMoney;

}
