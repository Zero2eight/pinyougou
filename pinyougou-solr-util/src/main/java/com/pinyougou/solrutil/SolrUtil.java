package com.pinyougou.solrutil;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.pinyougou.mapper.TbItemMapper;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.pojo.TbItemExample;
import com.pinyougou.pojo.TbItemExample.Criteria;

// @Component（把普通pojo实例化到spring容器中，相当于配置文件中的<bean id="" class=""/>）
@Component
public class SolrUtil {

	@Autowired
	private TbItemMapper itemMapper;

	@Autowired
	private SolrTemplate solrTemplate;
	
	//初始化索引库
	public static void main(String[] args) {
		// 1.扫描solr-util的配置文件,才可以扫描到自己 2.扫描dao层的配置文件?
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath*:spring/applicationContext*.xml");
		SolrUtil solrUtil = (SolrUtil) context.getBean("solrUtil");
		solrUtil.importItemData();

	}

	private void importItemData() {
		TbItemExample example = new TbItemExample();
		Criteria criteria = example.createCriteria();
		criteria.andStatusEqualTo("1");// 已审核
		List<TbItem> itemList = itemMapper.selectByExample(example);
		
		// 导入的内容展示
		System.out.println("===商品列表===");
		for (TbItem item : itemList) {
			Map specMap = JSON.parseObject(item.getSpec());
			item.setSpecMap(specMap);
			System.out.println(item);
		}
		System.out.println("===结束===");
		
		// 调用模板类导入 solr
		solrTemplate.saveBeans(itemList);
		solrTemplate.commit();

	}

}
