//服务层
app.service('goodsService',function($http){
	    	
	//读取列表数据绑定到表单中
	this.findAll=function(){
		return $http.get('../goods/findAll.do');		
	}
	//分页 
	this.findPage=function(page,rows){
		return $http.get('../goods/findPage.do?page='+page+'&rows='+rows);
	}
	//查询实体
	this.findOne=function(id){
		return $http.get('../goods/findOne.do?id='+id);
	}
	//增加 
	this.add=function(entity){
		return  $http.post('../goods/add.do',entity );
	}
	//修改 
	this.update=function(entity){
		return  $http.post('../goods/update.do',entity );
	}
	//删除
	this.dele=function(ids){
		return $http.get('../goods/delete.do?ids='+ids);
	}
	//搜索
	this.search=function(page,rows,searchEntity){
		return $http.post('../goods/search.do?page='+page+"&rows="+rows, searchEntity);
	}    	
	//增加
	this.addGoods=function(entity){
		return  $http.post('../goods/addGoods.do',entity );
	}
	//修改
	this.saveGoods=function(entity){
		return  $http.post('../goods/saveGoods.do',entity );
	}
	this.findOneGoods=function(id){
		return  $http.get('../goods/findOneGoods.do?id='+id);
	}
	//修改提交状态
	this.updateStatus = function(ids,status){
		return $http.get('../goods/updateStatus.do?ids='+ids+"&status="+status);
	}
	//更改上下架状态
	this.updateMarket = function(ids,status){
		return $http.get('../goods/updateMarket.do?ids='+ids+"&status="+status);
	}
});
