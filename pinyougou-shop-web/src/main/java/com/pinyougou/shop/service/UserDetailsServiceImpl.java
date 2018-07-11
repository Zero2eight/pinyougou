package com.pinyougou.shop.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbSeller;
import com.pinyougou.sellergoods.service.SellerService;

/**
 * 认证类
 * 将用户输入的账号进行查找后并与密码进行匹配
 * @author boyideyt
 *
 */
public class UserDetailsServiceImpl implements UserDetailsService {

	private SellerService sellerService;
	//不使用注解的方式,需要先在配置文件构建,所以必须有set方法
	public SellerService getSellerService() {
		return sellerService;
	}
	public void setSellerService(SellerService sellerService) {
		this.sellerService = sellerService;
	}
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// 根据用户名(id)查询商家用户
		TbSeller tbSeller = sellerService.findOne(username);
		// 构建角色列表
		String sellerId = tbSeller.getSellerId();
		String password = tbSeller.getPassword();
		String status = tbSeller.getStatus();
		// 构建角色组
		List<GrantedAuthority> list = new ArrayList<GrantedAuthority>();
		list.add(new SimpleGrantedAuthority("ROLE_SELLER"));
		// User(String username, String password, Collection<? extends
		// GrantedAuthority> authorities)
		if ("1".equals(status)) {
			return new User(sellerId, password, list);
		} else {
			return null;
		}
	}
}
