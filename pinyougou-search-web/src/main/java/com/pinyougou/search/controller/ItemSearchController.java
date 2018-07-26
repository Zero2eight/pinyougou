package com.pinyougou.search.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.search.service.ItemSearchService;

@RestController
@RequestMapping("/itemsearch")
public class ItemSearchController {
	
	@Reference(timeout=5000)
	private ItemSearchService itemSearchService;

	
	/**
	 * 搜索商品
	 * @param searchMap searchMap = {'keywords' : '','category' : '','brand' : '','spec' : {}}
	 * @return Map<String, Object> = {"rows":[],"categoryList":[],"brandList":[],"specList":[]}
	 */
	@RequestMapping("/search")
	public Map<String,Object> search(@RequestBody Map<String,Object> searchMap){
		return itemSearchService.search(searchMap);
	}
}
