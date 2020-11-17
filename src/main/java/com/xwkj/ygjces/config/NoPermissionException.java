package com.xwkj.ygjces.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.xwkj.ygjces.common.Common;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class NoPermissionException {
	/**
	 * 没有权限 异常
	 * 后续根据不同的需求定制即可
	 */
	@ExceptionHandler({UnauthorizedException.class})
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public ModelAndView processUnauthenticatedException(HttpServletRequest request, HttpServletResponse response, UnauthorizedException e) {
		if(Common.isAjaxRequest(request)){
			// 输出JSON
			Map<String, String> map = new HashMap<>();
			map.put("code", "401");
			map.put("msg", "您没有权限.请联系管理员!");
			writeJson(map, response);
			return null;
		}
		ModelAndView mv = new ModelAndView();
		mv.addObject("exception", e);
		mv.setViewName("403");

		return mv;
	}
	@ExceptionHandler({Exception.class})
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ModelAndView processAuthorizationExceptionException(HttpServletRequest request, UnauthorizedException e) {
		ModelAndView mv = new ModelAndView();
		mv.addObject("exception", e);
		mv.setViewName("/403");
		return mv;
	}
	/**
	 * 输出json
	 * @param map
	 * @param response
	 */
	private void writeJson(Map<String, String> map, HttpServletResponse response) {
		PrintWriter out = null;
		try {
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/json; charset=utf-8");
			out = response.getWriter();
			ObjectMapper json = new ObjectMapper();
			out.write(json.writeValueAsString(map));
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}
}
