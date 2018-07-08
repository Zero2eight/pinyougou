//品牌controller
	app.controller("brandController", function($scope,$controller,brandService) {
		$controller("baseController",{$scope:$scope});//继承
		$scope.findAll = function() {
			brandService.findAll().success(function(response) {
				$scope.list = response
			});
		}
		
		
		$scope.findPage=function(page,size){
			brandService.findPage(page,size).success(function(response){
				$scope.list =response.rows//当前页数
				$scope.paginationConf.totalItems=response.total //总记录数
			})	
		}	
		
		//增加
		$scope.save=function(){
			var object = null;
			if($scope.entity.id !=null){
				object =brandService.update($scope.entity)
			}else{
				object =brandService.add($scope.entity)
			}
			object.success(
			function(response){
				if(response.success){
					$scope.reloadList();//成功就刷新
				}else{
				alert(response.message);//失败就打印失败信息	
				}
			}		
		);	
		}	
		
		//查询一个
		 $scope.findOne=function(id){
			 brandService.findOne(id).success(
			function(response){
				$scope.entity=response;
			}		
			);
		}  
		
	
		//删除
		 $scope.dele=function(){
			brandService.dele($scope.selectIds).success(
					function(response){
						if(response.success){
							//刷新
							$scope.reloadList();
						}else{
							alert(response.message)
						}
					}
			);
		} 
		
		//条件查询
		$scope.searchEntity={}; //定义搜索对象
		$scope.search=function(page,size){	
			brandService.search(page,size,$scope.searchEntity).success(
						function(response){
						$scope.list =response.rows//当前页数
						$scope.paginationConf.totalItems=response.total //总记录数
					});
			
		}
		
	});