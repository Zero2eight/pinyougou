package com.pinyougou.shop.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 当前用户名显示
 * @author boyideyt
 *
 */
@RestController
@RequestMapping("/login")
public class loginController {
	
	@RequestMapping("/name")
	public Map name(){
		//返回当前登录名
		String sellerId = SecurityContextHolder.getContext().getAuthentication().getName();
		
		Map map = new HashMap();
		map.put("loginName", sellerId);
		return map;	
	}
}
