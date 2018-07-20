package com.pinyougou.sellergoods.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.mapper.TbBrandMapper;
import com.pinyougou.mapper.TbGoodsDescMapper;
import com.pinyougou.mapper.TbGoodsMapper;
import com.pinyougou.mapper.TbItemCatMapper;
import com.pinyougou.mapper.TbItemMapper;
import com.pinyougou.mapper.TbSellerMapper;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.pojo.TbGoods;
import com.pinyougou.pojo.TbGoodsDesc;
import com.pinyougou.pojo.TbGoodsExample;
import com.pinyougou.pojo.TbGoodsExample.Criteria;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.pojo.TbItemCat;
import com.pinyougou.pojo.TbItemExample;
import com.pinyougou.pojo.TbSeller;
import com.pinyougou.pojogroup.Goods;
import com.pinyougou.sellergoods.service.GoodsService;

import entity.PageResult;

/**
 * 服务实现层
 * 
 * @author Administrator
 *
 */
@Service
@Transactional
public class GoodsServiceImpl implements GoodsService {

	@Autowired
	private TbGoodsMapper goodsMapper;
	@Autowired
	private TbGoodsDescMapper goodsDescMapper;
	@Autowired
	private TbItemMapper itemMapper;
	@Autowired
	private TbSellerMapper sellerMapper;

	@Autowired
	private TbBrandMapper brandMapper;
	@Autowired
	private TbItemCatMapper itemCatMapper;

	/**
	 * 查询全部
	 */
	@Override
	public List<TbGoods> findAll() {
		return goodsMapper.selectByExample(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		Page<TbGoods> page = (Page<TbGoods>) goodsMapper.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * 增加
	 */
	@Override
	public void add(TbGoods goods) {
		goodsMapper.insert(goods);
	}

	/**
	 * 修改
	 */
	@Override
	public void update(TbGoods goods) {
		goodsMapper.updateByPrimaryKey(goods);
	}

	/**
	 * 根据ID获取实体
	 * 
	 * @param id
	 * @return
	 */
	@Override
	public TbGoods findOne(Long id) {
		return goodsMapper.selectByPrimaryKey(id);
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		// for (Long id : ids) {
		// goodsMapper.deleteByPrimaryKey(id);
		// }
		//map的属性集
				Map<String,Long[]> rooms = new HashMap<String, Long[]>();
				rooms.put("roomsId",ids);
				//mapper接口
				goodsMapper.deleteByMapId(rooms);
	}

	@Override
	public PageResult findPage(TbGoods goods, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);

		TbGoodsExample example = new TbGoodsExample();
		Criteria criteria = example.createCriteria();

		if (goods != null) {
			if (goods.getSellerId() != null && goods.getSellerId().length() > 0) {
//				criteria.andSellerIdLike("%" + goods.getSellerId() + "%");
				criteria.andSellerIdEqualTo(goods.getSellerId());
			}
			if (goods.getGoodsName() != null && goods.getGoodsName().length() > 0) {
				criteria.andGoodsNameLike("%" + goods.getGoodsName() + "%");
			}
			if (goods.getAuditStatus() != null && goods.getAuditStatus().length() > 0) {
				criteria.andAuditStatusLike("%" + goods.getAuditStatus() + "%");
			}
			if (goods.getIsMarketable() != null && goods.getIsMarketable().length() > 0) {
				criteria.andIsMarketableLike("%" + goods.getIsMarketable() + "%");
			}
			if (goods.getCaption() != null && goods.getCaption().length() > 0) {
				criteria.andCaptionLike("%" + goods.getCaption() + "%");
			}
			if (goods.getSmallPic() != null && goods.getSmallPic().length() > 0) {
				criteria.andSmallPicLike("%" + goods.getSmallPic() + "%");
			}
			if (goods.getIsEnableSpec() != null && goods.getIsEnableSpec().length() > 0) {
				criteria.andIsEnableSpecLike("%" + goods.getIsEnableSpec() + "%");
			}
//			if (goods.getIsDelete() != null && goods.getIsDelete().length() > 0) {
//				criteria.andIsDeleteLike("%" + goods.getIsDelete() + "%");
//			}

		}
		criteria.andIsDeleteEqualTo("0");
		Page<TbGoods> page = (Page<TbGoods>) goodsMapper.selectByExample(example);
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * goods_edit.html页面的商品信息添加
	 */
	@Override
	public void add(Goods goods) {
		// 添加商品信息(spu表)
		TbGoods tbGoods = goods.getTbGoods();
		// 商品初始状态
		tbGoods.setAuditStatus("0");
		// 商家id
		String sellerId = tbGoods.getSellerId();
		// 商品录入
		goodsMapper.insert(tbGoods);
		// 用以item的商品id
		Long goodId = tbGoods.getId();

		// 添加商品描述信息
		TbGoodsDesc tbGoodsDesc = goods.getTbGoodsDesc();
		tbGoodsDesc.setGoodsId(goodId);
		goodsDescMapper.insert(tbGoodsDesc);

		
		// 用以item的店铺名称
		TbSeller seller = sellerMapper.selectByPrimaryKey(sellerId);
		String nickName = seller.getNickName();
		// 用以item的品牌名称
		TbBrand brand = brandMapper.selectByPrimaryKey(tbGoods.getBrandId());
		String brandName = brand.getName();
		// 用以item的种类名称,种类id
		Long categoryId = tbGoods.getCategory3Id();
		TbItemCat itemCat = itemCatMapper.selectByPrimaryKey(categoryId);
		String itemCatName = itemCat.getName();
		List<TbItem> itemList;
		// 判断是否有规格列表
		if ("1".equals(tbGoods.getIsEnableSpec())) {
			// 添加sku表信息
			itemList = goods.getItemList();
		} else {
			itemList = new ArrayList<TbItem>();
			TbItem tbItem = new TbItem();
			tbItem.setTitle(tbGoods.getGoodsName());
			tbItem.setPrice(tbGoods.getPrice());
			tbItem.setStatus("1");
			tbItem.setNum(1);
			tbItem.setIsDefault("1");
			itemList.add(tbItem);
		}
		for (TbItem tbItem : itemList) {
			// {stockCount: "2", price: "32", spec: {网络: "移动3G", 机身内存:
			// "32G"}, status: true, isDefault: true}
			Map<String,Object> map = JSON.parseObject(tbItem.getSpec());
			String title = tbGoods.getGoodsName();
			if(map!=null){
				for (String key : map.keySet()) {
					title+=map.get(key);
				}
			}
			
			tbItem.setTitle(title);// 创建标题
			tbItem.setCreateTime(new Date());// 创建日期
			tbItem.setUpdateTime(new Date());// 更新日期
			// 商家id
			tbItem.setSellerId(sellerId);
			// 店铺名称
			tbItem.setSeller(nickName);
			// 品牌名称
			tbItem.setBrand(brandName);
			// item种类名称
			tbItem.setCategory(itemCatName);
			// item种类id
			tbItem.setCategoryid(categoryId);
			// 图片
			List<Map> imageList = JSON.parseArray(tbGoodsDesc.getItemImages(), Map.class);
			if (imageList.size() > 0) {
				tbItem.setImage((String) imageList.get(0).get("url"));
			}
			itemMapper.insert(tbItem);
		}
	}

	@Override
	public Goods findOneGoods(Long id) {
		Goods goods = new Goods();
		TbGoods tbGoods = goodsMapper.selectByPrimaryKey(id);
		TbGoodsDesc tbGoodsDesc = goodsDescMapper.selectByPrimaryKey(id);
		
		
		TbItemExample example = new TbItemExample();
		com.pinyougou.pojo.TbItemExample.Criteria criteria = example.createCriteria();
		criteria.andGoodsIdEqualTo(id);
		List<TbItem> list = itemMapper.selectByExample(example);
		
		goods.setItemList(list);
		goods.setTbGoods(tbGoods);
		goods.setTbGoodsDesc(tbGoodsDesc);
		
		return goods;
	}
}
