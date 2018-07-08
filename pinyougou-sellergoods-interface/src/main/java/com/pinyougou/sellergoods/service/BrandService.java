package com.pinyougou.sellergoods.service;

import java.util.List;
import java.util.Map;

import com.pinyougou.pojo.TbBrand;

import entity.PageResult;

public interface BrandService {
	public List<TbBrand> findName();
	/**
	 * 品牌分页
	 * @param pageNum 当前页数
	 * @param pageSize 当前页的品牌数
	 * @return
	 */
	public PageResult findPage(int pageNum,int pageSize);
	
	//增加
	public void add(TbBrand brand );
	
	//查询一个
	public TbBrand findOne(Long id);
	
	//修改
	public void update(TbBrand brand);
	
	//删除
	public void delete(Long[] ids);
	
	//条件查询
	public PageResult findPage(TbBrand brand, int pageNum,int pageSize);
	
	
	//查询brand
	public List<Map> findBrandList();
}
