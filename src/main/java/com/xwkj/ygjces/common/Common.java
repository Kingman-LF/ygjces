package com.xwkj.ygjces.common;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

public class Common {

	/**
	 * 判断是否为AJAX请求
	 * @param request
	 * @return 返回true为ajax请求，fasle为非ajax请求
	 * @date 2018年9月25日 上午10:04:31
	 */
	public static boolean isAjaxRequest(HttpServletRequest request) {
		boolean isAsynReq = false;
		String xRequestedWith = request.getHeader("X-Requested-With");

		// 判断是否为ajax请求
		isAsynReq = StringUtils.isNotEmpty(xRequestedWith)
				&& StringUtils.indexOf(xRequestedWith, "XMLHttpRequest") > -1;

		// json 请求头的也是ajax请求
		if (!isAsynReq) {
			isAsynReq = StringUtils.indexOf(request.getHeader("accept"), "application/json") > -1;
		}

		return isAsynReq;
	}
}
