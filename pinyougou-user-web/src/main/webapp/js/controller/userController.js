//控制层 
app.controller('userController', function($scope, $controller, userService) {

	$scope.register = {
		'agree' : '0',
		'code' : '',
		'usernameMsg' : '',
		'passwordMsg' : '',
		'confPassword' : ''
	}
	$scope.entity = {
		'username' : '',
		'password' : '',
		'phone' : ''

	}
	// 保存
	$scope.add = function() {
		if ($scope.userFlag && $scope.phoneCodeFlag && $scope.passwordFlag) {
			userService.add($scope.entity, $scope.register.code).success(
					function(response) {
						if (response.success) {
							alert(response.message);
						} else {
							alert(response.message);
						}
					});
		} else {
			alert("请检查未填写的信息!");
		}

	}
	// 发验证码
	$scope.sendMsgCode = function() {
		if ($scope.entity.phone == null || $scope.entity.phone == "") {
			alert("手机号码为空");
			$scope.phoneCodeFlag=false;
		} else {
			userService.sendMsgCode($scope.entity.phone).success(
					function(response) {
						if (response.success) {
							alert(response.message);
							$scope.phoneCodeFlag=true;
						} else {
							alert(response.message);
							$scope.phoneCodeFlag=false;
						}
					});
		}
	}
	
	//确认用户名信息
	$scope.findUserName = function() {

		userService.findUserName($scope.entity.username).success(
				function(response) {
					// 存在返回true
					if (response.success) {
						$scope.register.usernameMsg = response.message;
						$scope.userFlag=false;
					} else {
						$scope.register.usernameMsg = ''
						$scope.userFlag=true;
					}

				})
	}
	
	//确认密码信息
	$scope.checkPassword = function() {
		if ($scope.entity.password == "" || $scope.entity.password == "") {
			$scope.register.passwordMsg = "密码不能为空";
			$scope.passwordFlag=false;
			return;
		}
		if ($scope.register.confpassword == "" || $scope.register.confpassword == null) {
			$scope.register.passwordMsg = "确认密码不能为空";
			$scope.passwordFlag=false;
			return;
		}
		if ($scope.entity.password == $scope.register.confPassword) {
			$scope.register.passwordMsg = "ok";
			$scope.passwordFlag=true;
		}else{
			//不想等则false
			$scope.passwordFlag=false;
			$scope.register.passwordMsg = "两次密码不相等";
		}
	}

});
