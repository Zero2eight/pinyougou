package com.pinyougou.pojogroup;

import java.io.Serializable;
import java.util.List;

import com.pinyougou.pojo.TbGoods;
import com.pinyougou.pojo.TbGoodsDesc;
import com.pinyougou.pojo.TbItem;


/**
 * Goods类用于封装/pinyougou-shop-web/goods_edit.html页面存储的信息
 * @author boyideyt
 *
 */
public class Goods implements Serializable{
	private TbGoods tbGoods;
	private TbGoodsDesc tbGoodsDesc;
	private List<TbItem> itemList;
	
	
	public TbGoods getTbGoods() {
		return tbGoods;
	}
	public void setTbGoods(TbGoods tbGoods) {
		this.tbGoods = tbGoods;
	}
	public TbGoodsDesc getTbGoodsDesc() {
		return tbGoodsDesc;
	}
	public void setTbGoodsDesc(TbGoodsDesc tbGoodsDesc) {
		this.tbGoodsDesc = tbGoodsDesc;
	}
	public List<TbItem> getItemList() {
		return itemList;
	}
	public void setItemList(List<TbItem> itemList) {
		this.itemList = itemList;
	}
	@Override
	public String toString() {
		return "Goods [tbGoods=" + tbGoods + ", tbGoodsDesc=" + tbGoodsDesc + ", itemList=" + itemList + "]";
	}
	
	
}
