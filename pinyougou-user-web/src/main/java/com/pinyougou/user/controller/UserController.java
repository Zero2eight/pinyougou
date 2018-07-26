package com.pinyougou.user.controller;
import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbUser;
import com.pinyougou.user.service.UserService;

import entity.PageResult;
import entity.Result;
/**
 * controller
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/user")
public class UserController {

	@Reference
	private UserService userService;
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findAll")
	public List<TbUser> findAll(){			
		return userService.findAll();
	}
	
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findPage")
	public PageResult  findPage(int page,int rows){			
		return userService.findPage(page, rows);
	}
	
	/**
	 * 增加
	 * @param user
	 * @return
	 */
	@RequestMapping("/add")
	public Result add(@RequestBody TbUser user,String code){
		try {
			if(userService.add(user,code)){
				return new Result(true, "增加成功");
			}else{
				return new Result(false, "违法的手机号码");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "增加失败");
		}
	}
	
	@RequestMapping("/sendMsgCode")
	public Result sendMsgCode(String phone){
		try {
			if(phone==null||"".equals(phone)){
				return new Result(false, "手机号为空");
			}
			if(userService.sendMsgCode(phone)){
				return new Result(true, "发送成功");
			}else{
				return new Result(false, "违法的手机号码");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "发送失败,请稍候重试");
		}
	}
	
	@RequestMapping("/findUserName")
	public Result findUserName(String username){
		try {
			if(username==null||"".equals(username)){
				return new Result(false, "用户名为空");
			}
			if(userService.findUserName(username)){
				return new Result(true, "用户名存在");
			}else{
				return new Result(false, "用户名不存在");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "未知错误");
		}
	}
	
	
	
	
	/**
	 * 修改
	 * @param user
	 * @return
	 */
	@RequestMapping("/update")
	public Result update(@RequestBody TbUser user){
		try {
			userService.update(user);
			return new Result(true, "修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "修改失败");
		}
	}	
	
	/**
	 * 获取实体
	 * @param id
	 * @return
	 */
	@RequestMapping("/findOne")
	public TbUser findOne(Long id){
		return userService.findOne(id);		
	}
	
	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@RequestMapping("/delete")
	public Result delete(Long [] ids){
		try {
			userService.delete(ids);
			return new Result(true, "删除成功"); 
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "删除失败");
		}
	}
	
		/**
	 * 查询+分页
	 * @param brand
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("/search")
	public PageResult search(@RequestBody TbUser user, int page, int rows  ){
		
		return userService.findPage(user, page, rows);		
	}
}
