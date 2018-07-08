package com.pinyougou.manager.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.sellergoods.service.BrandService;

import entity.PageResult;
import entity.Result;
@RestController
@RequestMapping("/brand")
public class BrandController {
	
	@Reference
	private BrandService brandService;
	
	@RequestMapping("/findAll")
	public List<TbBrand> fingAll(){
		return brandService.findName();
		
	}
	//品牌分页
	@RequestMapping("/findPage")
	public PageResult findPage(int page , int size){
		return brandService.findPage(page, size);
	}
	//增加
	@RequestMapping("/add")
	public Result add(@RequestBody TbBrand brand){
		try {
			brandService.add(brand);
			return new Result(true,"增加成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false,"增加失败");
		}
	}
	
	//查询一个
	@RequestMapping("/findOne")
	public TbBrand findOne(Long id){
		return brandService.findOne(id);
	}
	
	//修改
	@RequestMapping("/update")
	public Result update(@RequestBody TbBrand brand){
		try {
			brandService.update(brand);
			return new Result(true,"修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false,"修改失败");
		}
	}
	//删除
	@RequestMapping("/delete")
	public Result delete(Long [] ids){
		try {
			brandService.delete(ids);
			return new Result(true,"删除成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false,"删除失败");
		}
	}
	//条件查询
	@RequestMapping("/search")
	public PageResult findPage(@RequestBody TbBrand brand, int page , int size){
		return brandService.findPage(brand,page, size);
	}
	@RequestMapping("/findBrandList")
	public List<Map> findBrandList(){
		return brandService.findBrandList();	
	}
 }