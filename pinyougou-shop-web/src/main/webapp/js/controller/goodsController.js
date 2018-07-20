//控制层 
app.controller('goodsController', function($scope, $controller,$location,goodsService,typeTemplateService,
		itemCatService, uploadService) {

	$controller('baseController', {
		$scope : $scope
	});// 继承

    //读取列表数据绑定到表单中  
	$scope.findAll=function(){
		goodsService.findAll().success(
			function(response){
				$scope.list=response;
			}			
		);
	}    
	
	//分页
	$scope.findPage=function(page,rows){			
		goodsService.findPage(page,rows).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
	
	//查询实体 
	$scope.findOne=function(id){				
		goodsService.findOne(id).success(
			function(response){
				$scope.entity= response;					
			}
		);				
	}
	
	//保存 
	$scope.save=function(){				
		var serviceObject;//服务层对象  				
		if($scope.entity.id!=null){//如果有ID
			serviceObject=goodsService.update( $scope.entity ); //修改  
		}else{
			serviceObject=goodsService.add( $scope.entity  );//增加 
		}				
		serviceObject.success(
			function(response){
				if(response.success){
					//重新查询 
		        	$scope.reloadList();//重新加载
				}else{
					alert(response.message);
				}
			}		
		);				
	}
	
	//增加商品 
	$scope.add=function(){			
		$scope.entity.goodsDesc.introduction=editor.html();
		
		goodsService.add( $scope.entity  ).success(
			function(response){
				if(response.success){
					alert("新增成功");
					$scope.entity={};
					editor.html("");//清空富文本编辑器
				}else{
					alert(response.message);
				}
			}		
		);				
	}
	
	 
	//批量删除 
	$scope.dele=function(){			
		//获取选中的复选框			
		goodsService.dele( $scope.selectIds ).success(
			function(response){
				if(response.success){
					$scope.reloadList();//刷新列表
					$scope.selectIds=[];
				}						
			}		
		);				
	}
	
	$scope.searchEntity={};//定义搜索对象 
	$scope.searchStatus=['未申请','申请中','审核通过','已驳回'];
	//搜索
	$scope.search=function(page,rows){			
		goodsService.search(page,rows,$scope.searchEntity).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
	//状态搜索栏
	$scope.$watch("searchEntity.auditStatus", function(newValue, oldValue) {
		if (newValue === oldValue)
			return;
		$scope.reloadList();//重新加载
		});
	
	
	//good.html页面的类型显示映射数组
	$scope.itemCatList={};
	$scope.findItemCatList=function(){
		itemCatService.findAll().success(function(response){
			for(var i=0;i<response.length;i++){
				$scope.itemCatList[response[i].id]=response[i].name;
			}
		});
	}

	$scope.entity = {
		tbGoods : {},
		tbGoodsDesc : {
			itemImages : []
		},
		itemList:[]
	}

	
	/**
	 * 上传图片
	 */
	$scope.uploadFile = function() {
		uploadService.uploadFile().success(function(response) {
			if (response.success) {// 如果上传成功，取出url
				$scope.imageEntity.url = response.message;// 设置文件地址
			} else {
				alert(response.message);
			}
		}).error(function() {
			alert("上传发生错误");
		});
	};
	
	/**
	 * 跳转修改界面后直接执行的查询方法
	 */
	$scope.findOneGoods = function() {
		var id=$location.search()['id'];
		if(id==null){
			return ;
		}		
		goodsService.findOneGoods(id).success(function(response){
			$scope.entity=response;
			//富文本编辑器内容读出来
			editor.html($scope.entity.tbGoodsDesc.introduction);
			//图片格式转换
			$scope.entity.tbGoodsDesc.itemImages=JSON.parse($scope.entity.tbGoodsDesc.itemImages);
			//扩展属性
			$scope.entity.tbGoodsDesc.customAttributeItems=JSON.parse($scope.entity.tbGoodsDesc.customAttributeItems);
			//规格
			$scope.entity.tbGoodsDesc.specificationItems=JSON.parse($scope.entity.tbGoodsDesc.specificationItems);
			//
			$scope.createSKU();
		})
	}
	
	/**
	 * 全部信息增加到数据库
	 */
	$scope.addGoods = function() {
		// 将富文本编辑器内容装进去
		$scope.entity.tbGoodsDesc.introduction = editor.html();
		// 将当前用户id加进去
		// $scope.entity.tbGoods.sellerId=$scope.loginName;
		
		if($scope.entity.tbGoods.id!=null){
			goodsService.saveGoods($scope.entity).success(function(response) {
				
			});
			return;
		}
		goodsService.addGoods($scope.entity).success(function(response) {
			if (response.success) {
				alert('保存成功');
				$scope.entity = {
						tbGoods : {},
						tbGoodsDesc : {
							itemImages : []
						},
						itemList:[]
					};
				$scope.typeTemplate ={};
				editor.html("");
			} else {
				alert(response.message);
			}
		})
	}
	/**
	 * 保存上传的图片到entity
	 */
	$scope.addImageEntity = function() {
		$scope.entity.tbGoodsDesc.itemImages.push($scope.imageEntity);
		$scope.imageEntity= {
			url : ""
		};
	}
	$scope.itemCat1List = [];
	$scope.itemCat2List = [];
	$scope.itemCat3List = [];
	/**
	 * 三级下拉菜单的改变响应方法
	 */
	$scope.findByParent0=function(){
		//$scope.findByParentId(0,$scope.itemCat1List);
		itemCatService.findByParentId(0).success(function(response){
			$scope.itemCat1List=response;
		});
	}
	
	$scope.$watch("entity.tbGoods.category1Id", function(newValue, oldValue) {
		if (newValue === oldValue||newValue == undefined||newValue == false)
			return;
		itemCatService.findByParentId(newValue).success(function(response){
			$scope.itemCat2List=response;
		});
		$scope.entity.tbGoods.category3Id = "";
	});
	$scope.$watch("entity.tbGoods.category2Id", function(newValue, oldValue) {
		if (newValue === oldValue||newValue == undefined||newValue == false){
			$scope.itemCat2List="";
			$scope.entity.tbGoods.category2Id = "";
			return;}
		itemCatService.findByParentId(newValue).success(function(response){
			$scope.itemCat3List=response;
		});
	});
	//	模板第三栏变更后更改后搜索相应的模板ID
	$scope.$watch("entity.tbGoods.category3Id", function(newValue, oldValue) {
		if (newValue === oldValue||newValue == undefined||newValue == false){
			$scope.itemCat3List="";
			$scope.entity.tbGoods.category3Id = "";
			return;}
		itemCatService.findOne(newValue).success(function(response) {
			$scope.entity.tbGoods.typeTemplateId = response.typeId; // 更新模板ID
		});
	});
	//  模板ID更改后更新品牌的下拉列表
	$scope.$watch("entity.tbGoods.typeTemplateId", function(newValue, oldValue) {
		if (newValue === oldValue||newValue == undefined||newValue==false)
			return;
//		typeTemplateService.findOne(newValue).success(function(response) {
			typeTemplateService.findSpecIdOptions(newValue).success(function(response) {
			$scope.typeTemplate = response; 
			//{"brandIds":"[]","customAttributeItems":"[]","id":35,"name":"手机","specIds":"[]"}
			$scope.typeTemplate.brandIds=JSON.parse( $scope.typeTemplate.brandIds);
			$scope.entity.tbGoodsDesc.customAttributeItems=JSON.parse( $scope.typeTemplate.customAttributeItems)
			$scope.typeTemplate.specIds=JSON.parse( $scope.typeTemplate.specIds)
		});
	});
	
	$scope.AttributeList=[];
	//updateSpecAttribute,将选项框赋值到tbGoodsDesc.specificationItems
	$scope.updateSpecAttribute=function($event,text,optionName){
		//直接赋值到局部变量obj
		var obj = {attributeName:text,attributeValue:[optionName]};
		//先判断是选中还是取消选中
		if($event.target.checked){
			//再判断是否第一次
			if($scope.AttributeList==null){
				$scope.AttributeList.push(obj);
			}else{
				var index = $scope.findIndexByKeyValue($scope.AttributeList,"attributeName",text);
				//判断是否含有这个attributeName
				if(index==null){
					//没含有就增加这个obj
					$scope.AttributeList.push(obj);
				}else{
					//否则就在含有的attributeValue上push一个值
					$scope.AttributeList[index].attributeValue.push(optionName);
				}
			}
			
		//如果是取消选中
		}else{
			//判断是哪个attributeName索引下的值被取消
			var index = $scope.findIndexByKeyValue($scope.AttributeList,"attributeName",text);
			//然后行从中取消一个值
			$scope.AttributeList[index].attributeValue.splice(
			$scope.AttributeList[index].attributeValue.indexOf(optionName),1);
			//如果attributeValue无,
			if($scope.AttributeList[index].attributeValue.length==0){
				$scope.AttributeList.splice(index,1);
			}
		}
		$scope.entity.tbGoodsDesc.specificationItems=$scope.AttributeList;
	}

						
	//[{"attributeName":"网络","attributeValue":["移动3G","移动4G"]},{"attributeName":"机身内存","attributeValue":["16G"]}]
	//动态显示item的录入表
	//	headList=["价格","库存","是否启用","是否默认"];
	$scope.createSKU=function(){
		$scope.AttributeList=$scope.entity.tbGoodsDesc.specificationItems;
		$scope.entity.itemList = [{num:"",price:"",spec:{},status:"0",isDefault:"0"}];
		$scope.itemList=$scope.entity.itemList;
		for(var x=0;x<$scope.AttributeList.length;x++){
			var itemList=[];
			for(var y=0;y<$scope.itemList.length;y++){
				for(var z=0;z<$scope.AttributeList[x].attributeValue.length;z++){
					var row=JSON.parse(JSON.stringify($scope.itemList[y]));
					row.spec[$scope.AttributeList[x].attributeName]=$scope.AttributeList[x].attributeValue[z];
					itemList.push(row);
				}
			}
			$scope.itemList=itemList;
		}
		
		$scope.entity.itemList=($scope.itemList);
	}
	
	//判断规格选项单个点选框 是否应该被勾选,输入:选项名,选项值
	$scope.checkAttributeValue=function(specName,optionName){
		//"specificationItems":"[{\"attributeValue\":[\"联通3G\",\"移动4G\"],\"attributeName\":\"网络\"},
		//{\"attributeValue\":[\"128G\",\"64G\"],\"attributeName\":\"机身内存\"}]"}
		var specificationItems= $scope.entity.goodsDesc.specificationItems;
		for(var x=0;x<specificationItems.length;x++){
			if(specificationItems[x].attributeName==specName){
				var attributeValue=JSON.parse(specificationItems[x].attributeValue)
				for(var y=0;y<attributeValue.length;y++){
					if(attributeValue[y]==optionName)
						return true;
				}
			}
		}
		return false;
	}
});
