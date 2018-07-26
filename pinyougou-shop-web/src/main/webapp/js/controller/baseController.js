
app.controller("baseController", function($scope) {
	
	//重新加载列表 数据
	$scope.reloadList=function(){
    	//切换页码  
    	$scope.search( $scope.paginationConf.currentPage, $scope.paginationConf.itemsPerPage);	   	
    }
    
	//分页控件配置 
	$scope.paginationConf = {
         currentPage: 1,
         totalItems: 10,
         itemsPerPage: 10,
         perPageOptions: [10, 20, 30, 40, 50],
         onChange: function(){
        	 $scope.reloadList();//重新加载
     	 }
	}; 
	
	$scope.selectIds = [];
	// 勾选复选框
	$scope.updateCheck = function($event, id) {
		if ($event.target.checked) {
			$scope.selectIds.push(id);
		} else {
			var index = $scope.selectIds.indexOf(id);
			$scope.selectIds.splice(index, 1);
		}
	}

	// 从对象集合中找到对象名,如果含有该对象,返回集合索引.
	$scope.findIndexByKeyValue = function(list, key, keyValue) {
		for (var i = 0; i < list.length; i++) {
			// if(list[i].key==keyValue){
			if (list[i][key] == keyValue) {
				return (i);
			}
		}
		return null;
	}

	// 优化显示,输入效果:"[{\"id\":27,\"text\":\"网络\"},{\"id\":32,\"text\":\"机身内存\"}]"
	$scope.jsonToString = function(jsonString, key) {
		// json字符串转换为对象
		var json = JSON.parse(jsonString);
		var value = "";
		for (var i = 0; i < json.length; i++) {
			if (i > 0) {
				value += ",";
			}
			value += json[i][key];
		}
		// 显示效果:网络,机身内存
		return value;
	}
	
});

//checkbox 全选/取消全选
var isCheckAll = false;

function swapCheck() {
	if(isCheckAll) {
		$("input[type='checkbox']").each(function() {
			this.checked = false;
		});
		isCheckAll = false;
	} else {
		$("input[type='checkbox']").each(function() {
			this.checked = true;
		});
		isCheckAll = true;
	}
}