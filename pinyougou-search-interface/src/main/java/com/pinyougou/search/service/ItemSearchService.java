package com.pinyougou.search.service;

import java.util.List;
import java.util.Map;

import com.pinyougou.pojo.TbItem;

public interface ItemSearchService {
	/**
	 * 搜索
	  * @param searchMap searchMap = {'keywords' : '','category' : '','brand' : '','spec' : {}}
	 * @return Map<String, Object> = {"rows":[],"categoryList":[],"brandList":[],"specList":[]}
	 */
	public Map<String,Object> search(Map<String,Object> searchMap);

	public void updateFields(List<TbItem> list);

	public void deleteByGoodsIds(List<Long> goodsIds);
	
}
