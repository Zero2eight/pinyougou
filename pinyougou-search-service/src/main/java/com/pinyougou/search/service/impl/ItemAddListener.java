package com.pinyougou.search.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.pojo.TbItemExample;
import com.pinyougou.pojo.TbItemExample.Criteria;
import com.pinyougou.search.service.ItemSearchService;

/**
 * 监听器,在服务启动时,接收activemq的消息,并将商品页存到solr
 * 
 * @author boyideyt
 *
 */
@Component
public class ItemAddListener implements MessageListener {

	@Autowired
	private ItemSearchService itemSearchService;

	@Override
	public void onMessage(Message message) {
		TextMessage textMessage = (TextMessage) message;
		try {
			List<TbItem> list = JSON.parseArray(textMessage.getText(),TbItem.class);
			System.out.println("监听到消息:"+list);
			
			itemSearchService.updateFields(list);
			
		} catch (Exception e) {
			System.out.println("存入失败");
		}

	}

	public static void main(String[] args) {
		String a = "[149187842867960]";
		JSONArray json = JSON.parseArray(a);
		if (json.size() > 0) {
			for (int i = 0; i < json.size(); i++) {
				// 遍历 jsonarray 数组，把每一个对象转成 json 对象
				Long id = (Long) json.get(i);
				// 得到 每个对象中的属性值
				System.out.println(id);
			}
		}
	}
}
