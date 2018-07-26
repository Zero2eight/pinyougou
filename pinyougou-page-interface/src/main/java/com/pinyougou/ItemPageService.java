package com.pinyougou;

import java.util.List;


/**
 * 商品详细页接口
 * 
 * @author boyideyt
 *
 */
public interface ItemPageService {
	/**
	 * 生成商品详细页
	 * 
	 * @param goodsId
	 */
	public boolean genItemHtml(Long goodsId);

	public boolean deleItemHtml(List<Long> goodsIds);



}
