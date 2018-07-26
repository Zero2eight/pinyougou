package com.pinyougou.search.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.GroupOptions;
import org.springframework.data.solr.core.query.HighlightOptions;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleHighlightQuery;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.result.GroupEntry;
import org.springframework.data.solr.core.query.result.GroupPage;
import org.springframework.data.solr.core.query.result.GroupResult;
import org.springframework.data.solr.core.query.result.HighlightEntry;
import org.springframework.data.solr.core.query.result.HighlightEntry.Highlight;
import org.springframework.data.solr.core.query.result.HighlightPage;
import org.springframework.data.solr.core.query.result.ScoredPage;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.search.service.ItemSearchService;

@Service(timeout = 3000)
public class ItemSearchServiceImpl implements ItemSearchService {

	@Autowired
	private SolrTemplate solrTemplate;
	@Autowired
	private RedisTemplate redisTemplate;

	/**
	 * 高亮显示
	 * 
	 * @param searchMap
	 *            searchMap = {'keywords' : '','category' : '','brand' :
	 *            '','spec' : {},"sortField":'','direction':'','page' : '',
	 *            'size' : ''}
	 * @return Map<String, Object> =
	 *         {"rows":[],"categoryList":[],"brandList":[],"specList":[]}
	 */
	@Override
	public Map<String, Object> search(Map<String, Object> searchMap) {
		// 关键字空值排除
		String keywords = (String) searchMap.get("keywords");
		if (keywords == null || keywords.trim().length() == 0) {
			// 执行普通的全部查询
			return normalSearch(searchMap);
		}
		// 空格处理
		String newKeywords = keywords.replace(" ", "");
		searchMap.put("keywords", newKeywords);

		Map<String, Object> map = new HashMap<>();
		SimpleHighlightQuery highlightQuery = new SimpleHighlightQuery();
		HighlightOptions highlightOptions = new HighlightOptions().addField("item_title");

		// 设置高亮查询的highlightOptions(前后缀内容)
		highlightOptions.setSimplePrefix("<em style='color:red'>");// 高亮前缀
		highlightOptions.setSimplePostfix("</em>");// 高亮后缀
		highlightQuery.setHighlightOptions(highlightOptions);// 设置高亮选项

		// 1.提取搜索栏关键字
		String searchField = (String) searchMap.get("keywords");
		// 设置关键字查询,is就是模糊查询
		Criteria criteria = new Criteria("item_keywords").is(searchField);
		highlightQuery.addCriteria(criteria);

		// 2.搜索商品分类列表
		List<String> categoryList = searchCategoryList(searchField);

		// 3.搜索匹配品牌列表
		if (categoryList.size() > 0) {
			List<String> brandList = new ArrayList();
			// [手机, 平板电视, 鼠标]
			for (String category : categoryList) {
				// 3.查询品牌和规格列表
				map.putAll(searchBrandAndSpecList(category));
			}
		}
		// redisTemplate.boundHashOps("itemCatList").put(tbItemCat.getName(),
		// tbItemCat.getTypeId());

		// 4.根据指定字段(成员变量) 排序(ASC/DESC)
		String direction = (String) searchMap.get("direction");
		String sortField = (String) searchMap.get("sortField");
		if (searchField != null && !"".equals(sortField)) {
			if ("ASC".equals(direction)) {
				Sort sort = new Sort(Sort.Direction.ASC, "item_" + sortField);
				highlightQuery.addSort(sort);
			} else if ("DESC".equals(direction)) {
				Sort sort = new Sort(Sort.Direction.DESC, "item_" + sortField);
				highlightQuery.addSort(sort);
			}
		}
		
		// 5.分页配置
		Integer page = (Integer) searchMap.get("page");
		if(page==null){
			page=1;
		}
		Integer size = (Integer) searchMap.get("size");
		if(size==null){
			size=20;
		}
		//设置起始索引和每页的搜索个数
		highlightQuery.setOffset((page-1)*size);
		highlightQuery.setRows(size);
		
		// ************执行高亮搜索,获取结果页************
		HighlightPage<TbItem> highlightPage = solrTemplate.queryForHighlightPage(highlightQuery, TbItem.class);

		// 用结果页获取高亮入口集合
		List<HighlightEntry<TbItem>> highlighted = highlightPage.getHighlighted();
		for (HighlightEntry<TbItem> highlightEntry : highlighted) {
			// 每个高亮入口可以获取高亮列表
			List<Highlight> highlights = highlightEntry.getHighlights();

			if (highlights.size() > 0 && highlights.get(0).getSnipplets().size() > 0) {
				TbItem entity = highlightEntry.getEntity();
				entity.setTitle(highlights.get(0).getSnipplets().get(0));
			}
		}
		// 一样可以返回List<TbItem>
		map.put("rows", highlightPage.getContent());
		map.put("categoryList", categoryList);
		map.put("totalPages", highlightPage.getTotalPages());//总页数
		map.put("total", highlightPage.getTotalElements());//总记录数
		return map;
	}

	/**
	 * 使用分组查询 搜索商品分类列表
	 * 
	 * @param searchField
	 * @return
	 */
	private List<String> searchCategoryList(String searchField) {
		SimpleQuery query = new SimpleQuery("*:*");
		Criteria criteria = new Criteria("item_keywords").is(searchField);
		// 设置搜索条件
		query.addCriteria(criteria);

		GroupOptions groupOptions = new GroupOptions().addGroupByField("item_category");
		// 设置分组条件
		query.setGroupOptions(groupOptions);
		// 执行搜索
		GroupPage<TbItem> groupPage = solrTemplate.queryForGroupPage(query, TbItem.class);

		GroupResult<TbItem> groupResult = groupPage.getGroupResult("item_category");

		Page<GroupEntry<TbItem>> groupEntries = groupResult.getGroupEntries();

		List<GroupEntry<TbItem>> groupEntryList = groupEntries.getContent();

		ArrayList<String> list = new ArrayList<>();
		for (GroupEntry<TbItem> groupEntry : groupEntryList) {
			list.add(groupEntry.getGroupValue());
		}
		return list;
	}

	/**
	 * 从缓存中 搜索品牌列表及规格列表
	 * 
	 * @param category
	 * @return
	 */
	private Map searchBrandAndSpecList(String category) {
		Map map = new HashMap();
		Long typeId = (Long) redisTemplate.boundHashOps("itemCat").get(category);// 获取模板ID
		if (typeId != null) {
			// 根据模板ID查询品牌列表
			List brandList = (List) redisTemplate.boundHashOps("brandList").get(typeId);
			map.put("brandList", brandList);// 返回值添加品牌列表
			// 根据模板ID查询规格列表
			List specList = (List) redisTemplate.boundHashOps("specList").get(typeId);
			map.put("specList", specList);
			System.out.println("从缓存中获取品牌规格");
		}

		return map;
	}

	/**
	 * 普通显示
	 * 
	 * @param searchMap
	 * @return Map<String, Object>
	 */
	public Map<String, Object> normalSearch(Map searchMap) {
		Map<String, Object> map = new HashMap<>();
		Query query = new SimpleQuery("*:*");
		
		//分页配置
		Integer page = (Integer) searchMap.get("page");
		if(page==null){
			page=1;
		}
		Integer size = (Integer) searchMap.get("size");
		if(size==null){
			size=20;
		}
		//设置起始索引和每页的搜索个数
		query.setOffset((page-1)*size);
		query.setRows(size);
		
		
		// 分页查询
		ScoredPage<TbItem> scoredPage = solrTemplate.queryForPage(query, TbItem.class);
		map.put("rows", scoredPage.getContent());
		map.put("totalPages", scoredPage.getTotalPages());//总页数
		map.put("total", scoredPage.getTotalElements());//总记录数
		
		return map;
	}

	@Override
	public void updateFields(List<TbItem> list) {
		solrTemplate.saveBeans(list);
		solrTemplate.commit();
	}

	@Override
	public void deleteByGoodsIds(List<Long> goodsIds) {
		Query query = new SimpleQuery("*:*");
		Criteria criteria = new Criteria("item_goodsid").in(goodsIds);
		query.addCriteria(criteria);
		solrTemplate.delete(query);
		solrTemplate.commit();
	}

}
