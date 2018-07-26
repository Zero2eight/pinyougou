package com.pinyougou.shop.controller;

import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbGoods;
import com.pinyougou.pojo.TbGoodsDesc;
import com.pinyougou.pojogroup.Goods;
import com.pinyougou.sellergoods.service.GoodsService;

import entity.PageResult;
import entity.Result;

/**
 * controller
 * 
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {

	@Reference
	private GoodsService goodsService;

	/**
	 * 返回全部列表(不用)
	 * 
	 * @return
	 */
	@RequestMapping("/findAll")
	public List<TbGoods> findAll() {
		return goodsService.findAll();
	}

	/**
	 * 返回全部列表(不用)
	 * 
	 * @return
	 */
	@RequestMapping("/findPage")
	public PageResult findPage(int page, int rows) {
		return goodsService.findPage(page, rows);
	}

	/**
	 * 增加 这个不用
	 * 
	 * @param goods
	 * @return
	 */
	// @RequestMapping("/add")
	// public Result add(@RequestBody TbGoods goods){
	// try {
	// goodsService.add(goods);
	// return new Result(true, "增加成功");
	// } catch (Exception e) {
	// e.printStackTrace();
	// return new Result(false, "增加失败");
	// }
	// }

	/**
	 * 修改 这个不用
	 * 
	 * @param goods
	 * @return
	 */
	@RequestMapping("/update")
	public Result update(@RequestBody TbGoods goods) {
		try {
			goodsService.update(goods);
			return new Result(true, "修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "修改失败");
		}
	}

	/**
	 * 获取实体
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/findOne")
	public TbGoods findOne(Long id) {
		return goodsService.findOne(id);
	}

	/**
	 * 批量删除
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping("/delete")
	public Result delete(Long[] ids) {
		try {
			goodsService.delete(ids);
			return new Result(true, "删除成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "删除失败");
		}
	}

	/**
	 * 查询+分页
	 * 
	 * @param brand
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("/search")
	public PageResult search(@RequestBody TbGoods goods, int page, int rows) {
		// 返回当前登录名
		String sellerId = SecurityContextHolder.getContext().getAuthentication().getName();
		// System.out.println(sellerId);
		goods.setSellerId(sellerId);
		return goodsService.findPage(goods, page, rows);
	}

	/**
	 * goods_edit.html页面的商品信息添加
	 * 
	 * @param goods
	 * @return
	 */
	@RequestMapping("/addGoods")
	public Result addGoods(@RequestBody Goods goods) {
		try {
			// 返回当前登录名
			String sellerId = SecurityContextHolder.getContext().getAuthentication().getName();
			goods.getTbGoods().setSellerId(sellerId);
			goodsService.add(goods);
			return new Result(true, "添加成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "添加失败");
		}
	}

	/**
	 * 修改
	 * 
	 * @param goods
	 * @return
	 */
	@RequestMapping("/saveGoods")
	public Result saveGoods(@RequestBody Goods goods) {
		try {
			// 返回当前登录名
			String sellerId = SecurityContextHolder.getContext().getAuthentication().getName();
			goods.getTbGoods().setSellerId(sellerId);
			goodsService.update(goods);
			return new Result(true, "修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "修改失败");
		}
	}

	/**
	 * 修改页展示
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/findOneGoods")
	public Goods findOneGoods(Long id) {
		Goods findOneGoods = goodsService.findOneGoods(id);
		System.out.println(findOneGoods);
		return findOneGoods;
	}

	/**
	 * 修改审核状态
	 * 
	 * @param ids
	 * @param status
	 * @return
	 */
	@RequestMapping("/updateStatus")
	public Result updateStatus(Long[] ids, String status) {
		try {
			goodsService.updateStatus(ids, status);
			return new Result(true, "修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "修改失败");
		}
	}

	/**
	 * 更改上下架状态
	 * 
	 * @param ids
	 * @param status
	 * @return
	 */
	@RequestMapping("/updateMarket")
	public Result updateMarket(Long[] ids, String status) {
		try {
			goodsService.updateMarket(ids, status);
			if ("1".equals(status)) {
				return new Result(true, "上架成功");
			} else {
				return new Result(true, "下架成功");
			}
		} catch (Exception e) {
			e.printStackTrace();
			if ("1".equals(status)) {
				return new Result(true, "上架失败");
			} else {
				return new Result(true, "下架失败");
			}
		}
	}
}
