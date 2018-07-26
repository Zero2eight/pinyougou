app.controller('searchController', function($scope, $location, searchService) {

	// 定义搜索对象的结构 category:商品分类
	$scope.searchMap = {
		'keywords' : '',
		'category' : '',
		'brand' : '',
		'spec' : {},
		"sortField" : '',
		'direction' : '',
		'page' : '',
		'size' : ''
	};
	// $scope.resultMap={'rows':[],'categoryList':[],'brandList':[],'specList':[]};

	// 搜索
	$scope.search = function() {
		$scope.searchMap.page = $scope.paginationConf.currentPage;
		$scope.searchMap.size = $scope.paginationConf.itemsPerPage;
		searchService.search($scope.searchMap).success(function(response) {
			$scope.resultMap = response;
			$scope.list = response.rows// 当前页数
			$scope.paginationConf.totalItems = response.total // 总记录数
		});
	}
	// 接收主页跳转数据
	$scope.initSearch = function() {
		$scope.searchMap.keywords = $location.search()['searchField'];
		$scope.search();
	}

	// 添加搜索项 改变searchMap的值
	$scope.addSearchItem = function(key, value) {

		if (key == 'category' || key == 'brand') {// 如果用户点击的是分类或品牌
			$scope.searchMap[key] = value;

		} else {// 用户点击的是规格
			$scope.searchMap.spec[key] = value;
		}
		$scope.search();// 查询
	}

	// 撤销搜索项
	$scope.removeSearchItem = function(key) {
		if (key == 'category' || key == 'brand') {// 如果用户点击的是分类或品牌
			$scope.searchMap[key] = "";
		} else {// 用户点击的是规格
			delete $scope.searchMap.spec[key];
		}
		$scope.search();// 查询
	}

	// 搜索排序
	$scope.sortSearch = function(sortField, direction) {
		$scope.searchMap.direction = direction;
		$scope.searchMap.sortField = sortField;
		$scope.search();
	}

	// 分页控件配置
	$scope.paginationConf = {
		currentPage : 1,
		totalItems : 10,
		itemsPerPage : 10,
		perPageOptions : [ 10, 20, 30, 40, 50 ],
		onChange : function() {
			$scope.search();// 重新加载

		}
	};
});