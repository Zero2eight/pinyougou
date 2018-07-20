package com.pinyougou.search.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.HighlightOptions;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleHighlightQuery;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.result.HighlightEntry;
import org.springframework.data.solr.core.query.result.HighlightEntry.Highlight;
import org.springframework.data.solr.core.query.result.HighlightPage;
import org.springframework.data.solr.core.query.result.ScoredPage;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.search.service.ItemSearchService;

@Service(timeout = 3000)
public class ItemSearchServiceImpl implements ItemSearchService {

	@Autowired
	private SolrTemplate solrTemplate;

	/**
	 * 高亮显示
	 * 
	 * @param searchMap
	 * @return Map<String, Object>
	 */
	@Override
	public Map<String, Object> search(Map searchMap) {

		Map<String, Object> map = new HashMap<>();
		SimpleHighlightQuery highlightQuery = new SimpleHighlightQuery();
		HighlightOptions highlightOptions = new HighlightOptions().addField("item_title");

		// 设置高亮查询的highlightOptions(前后缀内容)
		highlightOptions.setSimplePrefix("<em style='color:red'>");// 高亮前缀
		highlightOptions.setSimplePostfix("</em>");// 高亮后缀
		highlightQuery.setHighlightOptions(highlightOptions);// 设置高亮选项
		String searchField = (String) searchMap.get("keywords");
		System.out.println(searchField);
		// 设置关键字查询,is就是模糊查询
		Criteria criteria = new Criteria("item_keywords").is(searchField);
		// Criteria criteria = new
		// Criteria("item_keywords").contains(searchField);
		highlightQuery.addCriteria(criteria);

		// 执行高亮搜索,获取结果页
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
		return map;
	}

	/**
	 * 普通显示
	 * 
	 * @param searchMap
	 * @return Map<String, Object>
	 */
	public Map<String, Object> search1(Map searchMap) {
		Map<String, Object> map = new HashMap<>();
		Query query = new SimpleQuery();
		// 添加查询条件

		String searchField = (String) searchMap.get("keywords");
		Criteria criteria = new Criteria("item_keywords").contains(searchField);
		query.addCriteria(criteria);
		query.setRows(20);
		// 分页查询
		ScoredPage<TbItem> page = solrTemplate.queryForPage(query, TbItem.class);
		map.put("rows", page.getContent());
		return map;
	}

}
