app.controller("baseController",function($scope){
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
	//刷新页数
	$scope.reloadList=function(){
		$scope.search($scope.paginationConf.currentPage,$scope.paginationConf.itemsPerPage);
	}
	
	$scope.selectIds=[];
	//勾选复选框
	$scope.updateCheck=function($event,id){
		if($event.target.checked){
			$scope.selectIds.push(id);
		}else{
			var index= $scope.selectIds.indexOf(id);
			$scope.selectIds.splice(index,1);
		}
	}
	
	//用于商品种类的展示
	$scope.jsonToString=function(jsonString,key){
		var json= JSON.parse(jsonString);
		var value="";
		for(var i=0;i<json.length;i++){
			if(i>0){
				value+=",";
			}
			value+=json[i][key];
		}
		return value;
	}
	
	//用于面包屑转换为字符串
	$scope.jsonToString2=function(json,key){
//		var json= JSON.parse(jsonString);
		var value="";
		for(var i=0;i<json.length;i++){
			if(i>0){
				value+=">";
			}
			value+=json[i][key];
		}
		return value;
	}
});