//控制层 
app.controller('itemCatController', function($scope, $controller,
		itemCatService) {

	$controller('baseController', {
		$scope : $scope
	});// 继承

	// 读取列表数据绑定到表单中
	$scope.findAll = function() {
		itemCatService.findAll().success(function(response) {
			$scope.list = response;
		});
	}

	// 分页
	$scope.findPage = function(page, rows) {
		itemCatService.findPage(page, rows).success(function(response) {
			$scope.list = response.rows;
			$scope.paginationConf.totalItems = response.total;// 更新总记录数
		});
	}

	// 查询实体
	$scope.findOne = function(id) {
		itemCatService.findOne(id).success(function(response) {
			$scope.entity = response;
		});
	}
	
	//除父类id外,其他的内容清空
	$scope.getParentId = function() {
		$scope.entity={parentId:$scope.searchEntity.parentId};
	}
	// 保存
	$scope.save = function() {
		var serviceObject;// 服务层对象
		if ($scope.entity.id != null) {// 如果有ID
			serviceObject = itemCatService.update($scope.entity); // 修改
		} else {
			serviceObject = itemCatService.add($scope.entity);// 增加
		}
		serviceObject.success(function(response) {
			if (response.success) {
				// 重新查询
				$scope.reloadList();// 重新加载
			} else {
				alert(response.message);
			}
		});
	}

	// 批量删除
	$scope.dele = function() {
		// 获取选中的复选框
		itemCatService.dele($scope.selectIds).success(function(response) {
			if (response.success) {
				$scope.reloadList();// 刷新列表
			}
		});
	}

	
	// 搜索 其中searchEntity是传父类id属性的
	$scope.search = function(page, rows) {
		itemCatService.search(page, rows, $scope.searchEntity).success(
				function(response) {
					$scope.list = response.rows;
					$scope.paginationConf.totalItems = response.total;// 更新总记录数
				});
	}
	
	//定义面包屑字符数组
	$scope.breadcrumbs=[{id:0,parentId:0,name:'顶级分类列表'}];
	
	//截断面包屑
	$scope.updateBreadcrumbs=function(index){
		$scope.findByParentId($scope.breadcrumbs[index]);
		$scope.breadcrumbs.length=index+1;
	}
	
	// 按父类id查找item
	/*$scope.findByParentId = function(id,itemCatList) {
		//当前对象的id是搜索需要的parentId.
		itemCatService.findByParentId(id).success(function(response){
			itemCatList=response;
		});
	}*/
	/*$scope.findByParentId2 = function(id) {
		//当前对象的id是搜索需要的parentId.
		itemCatService.findByParentId(id).success(function(response){
			$scope.itemCat2List=response;
		});
	}
	$scope.findByParentId3 = function(id) {
		//当前对象的id是搜索需要的parentId.
		itemCatService.findByParentId(id).success(function(response){
			$scope.itemCat3List=response;
		});
	}*/

});
