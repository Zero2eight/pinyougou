package com.pinyougou.search.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.pojo.TbItemExample;
import com.pinyougou.pojo.TbItemExample.Criteria;
import com.pinyougou.search.service.ItemSearchService;



/**
 * 监听器,在服务启动时,接收activemq的消息,并将商品页存到solr
 * @author boyideyt
 *
 */
@Component
public class ItemDeleteListener implements MessageListener {
	
	@Autowired
	private ItemSearchService itemSearchService;
	
	@Override
	public void onMessage(Message message) {
	
		ObjectMessage objectMessage = (ObjectMessage) message;
		try {
			Long[] goodsIds= (Long[]) objectMessage.getObject();
			System.out.println("监听获取到消息："+goodsIds);
			itemSearchService.deleteByGoodsIds(Arrays.asList(goodsIds));
			System.out.println("执行索引库删除");
		} catch (JMSException e) {
			e.printStackTrace();
		}

	}
	

}
