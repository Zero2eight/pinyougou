package com.pinyougou.page.service.impl;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.ItemPageService;
import com.pinyougou.mapper.TbGoodsDescMapper;
import com.pinyougou.mapper.TbGoodsMapper;
import com.pinyougou.pojo.TbGoods;
import com.pinyougou.pojo.TbGoodsDesc;

import freemarker.template.Configuration;
import freemarker.template.Template;

@Service
public class ItemPageServiceImpl implements ItemPageService {
	// pinyougou-page-web下
	@Value("${pagedir}")
	private String pagedir;

	@Autowired
	private FreeMarkerConfig freeMarkerConfig;

	@Autowired
	private TbGoodsMapper goodsMapper;

	@Autowired
	private TbGoodsDescMapper goodsDescMapper;

	/**
	 * goodsid来自监听器接收 静态化网页的构建
	 */
	@Override
	public boolean genItemHtml(Long goodsId) {
		// 创建配置类
		Configuration configuration = freeMarkerConfig.getConfiguration();
		try {
			// 加载模板
			Template template = configuration.getTemplate("item.ftl");
			// 创建对象
			Map<String, Object> dataModel = new HashMap<>();
			// 1.加载商品表数据
			TbGoods goods = goodsMapper.selectByPrimaryKey(goodsId);
			dataModel.put("goods", goods);
			// 2.加载商品扩展表数据
			TbGoodsDesc goodsDesc = goodsDescMapper.selectByPrimaryKey(goodsId);
			// 对象赋值
			dataModel.put("goodsDesc", goodsDesc);
			// 创建输出流
			Writer out = new FileWriter(pagedir + goodsId + ".html");
			// 执行页面静态化
			template.process(dataModel, out);
			// 关闭输出流
			out.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * goodsid来自监听器接收 静态页面的删除
	 */
	@Override
	public boolean deleItemHtml(List<Long> goodsIds) {
		try {
			for (Long goodsId : goodsIds) {
				new File(pagedir + goodsId + ".html").delete();
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
