package com.pinyougou.user.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import com.alibaba.fastjson.JSON;
import com.pinyougou.pojo.TbItemCat;
import com.pinyougou.pojo.TbTypeTemplate;
/**
 * 存储缓存的线程类
 * @author boyideyt
 *
 */
public class RedisSaveRunner implements Runnable {

	private List<TbItemCat> itemCatList;
	private List<TbTypeTemplate> typeTemplateList;
	private RedisTemplate redisTemplate;

	public RedisSaveRunner(List<TbItemCat> itemCatList, List<TbTypeTemplate> typeTemplateList,
			RedisTemplate redisTemplate) {
		super();
		this.itemCatList = itemCatList;
		this.typeTemplateList = typeTemplateList;
		this.redisTemplate = redisTemplate;
	}

	@Override
	public void run() {
		if (itemCatList != null) {
			saveItemCatList();
		} else if (typeTemplateList != null) {
			saveSpecAndBrandList();
		} else {
			System.out.println("没有更新");
		}

	}
	/**
	 * 更新缓存:商品分类表
	 */
	public void saveItemCatList() {

		for (TbItemCat tbItemCat : itemCatList) {
			redisTemplate.boundHashOps("itemCatList").put(tbItemCat.getName(),tbItemCat.getTypeId());
		}
		System.out.println("更新缓存:商品分类表");
	}
	/**
	 * 更新缓存:品牌表,规格表
	 */
	public void saveSpecAndBrandList() {

		// 循环模板
		for (TbTypeTemplate typeTemplate : typeTemplateList) {
			// 存储品牌列表
			List<Map> brandList = JSON.parseArray(typeTemplate.getBrandIds(), Map.class);
			redisTemplate.boundHashOps("brandList").put(typeTemplate.getId(), brandList);
			// 存储规格列表
			List<Map> specList = JSON.parseArray(typeTemplate.getSpecIds(), Map.class);// 根据模板ID查询规格列表
			redisTemplate.boundHashOps("specList").put(typeTemplate.getId(), specList);
		}
		System.out.println("更新缓存:品牌表,规格表");
	}

}
