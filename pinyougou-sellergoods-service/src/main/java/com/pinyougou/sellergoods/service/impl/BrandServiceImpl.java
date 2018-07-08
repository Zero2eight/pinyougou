package com.pinyougou.sellergoods.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.mapper.TbBrandMapper;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.pojo.TbBrandExample;
import com.pinyougou.pojo.TbBrandExample.Criteria;
import com.pinyougou.sellergoods.service.BrandService;

import entity.PageResult;
@Service
public class BrandServiceImpl implements BrandService {
	@Autowired
	private TbBrandMapper tbBrandMapping;
	
	@Override
	public List<TbBrand> findName() {
		return tbBrandMapping.selectByExample(null);
	}
//品牌分页
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		Page<TbBrand> page = (Page<TbBrand>) tbBrandMapping.selectByExample(null);
		
		return new PageResult(page.getTotal(), page.getResult());
	}
	//增加
	@Override
	public void add(TbBrand brand) {
		tbBrandMapping.insert(brand);
	}
	//根据id查询一个
	@Override
	public TbBrand findOne(Long id) {
		return tbBrandMapping.selectByPrimaryKey(id) ;
	}
	//修改
	@Override
	public void update(TbBrand brand) {
		tbBrandMapping.updateByPrimaryKey(brand);
	}
	//删除
	@Override
	public void delete(Long[] ids) {
//			for (Long id : ids) {
//				tbBrandMapping.deleteByPrimaryKey(id);
//			}
		//map的属性集
		Map<String,Long[]> rooms = new HashMap<String, Long[]>();
		rooms.put("roomsId",ids);
		//mapper接口
		tbBrandMapping.deleteByMapId(rooms);
	}
	//条件查询
	@Override
	public PageResult findPage(TbBrand brand, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		
		TbBrandExample example = new TbBrandExample();
		Criteria criteria = example.createCriteria();
		if(brand !=null){
			if(brand.getName() !=null && brand.getName().length()>0){
				criteria.andNameLike("%"+brand.getName()+"%");				
			}
			if(brand.getFirstChar() != null && brand.getFirstChar().length()>0){			
				criteria.andFirstCharLike(brand.getFirstChar());
			}
		}
		Page<TbBrand> page = (Page<TbBrand>) tbBrandMapping.selectByExample(example);
		
		return new PageResult(page.getTotal(), page.getResult());
	
	}
	@Override
	public List<Map> findBrandList() {
		
		return tbBrandMapping.findBrandList();
	}

}
