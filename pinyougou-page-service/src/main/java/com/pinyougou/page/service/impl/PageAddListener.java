package com.pinyougou.page.service.impl;


import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.pinyougou.ItemPageService;

/**
 * 监听器,在服务启动时,接收activemq的消息,并将商品页存到solr
 * 
 * @author boyideyt
 *
 */
@Component
public class PageAddListener implements MessageListener {

	@Autowired
	private ItemPageService ItemPageService;

	@Override
	public void onMessage(Message message) {
		ObjectMessage objectMessage = (ObjectMessage) message;
		try {
			Long[] goodsIds= (Long[]) objectMessage.getObject();
			System.out.println("监听到消息:"+goodsIds);
			for (Long goodsId : goodsIds) {
				//创建商品sku页面
				ItemPageService.genItemHtml(goodsId);
			}
			
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
