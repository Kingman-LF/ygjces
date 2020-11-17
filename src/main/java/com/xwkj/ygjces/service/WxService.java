package com.xwkj.ygjces.service;

import com.xwkj.ygjces.utils.AesException;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.cp.api.WxCpService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface WxService extends WxCpService {
	/**
	 * 处理微信通讯录回调通知
	 * @param request
	 * @param response
	 */
	public void callbackNotification(HttpServletRequest request, HttpServletResponse response) throws AesException, IOException, WxErrorException;

}
