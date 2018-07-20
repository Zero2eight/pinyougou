//文件上传服务层 
app.service("uploadService",function($http){ 
 	this.uploadFile=function(){ 
 	 	var formData=new FormData(); 
 	 	//file.files[0]
 	    formData.append("file",file.files[0]);    
 	 	return $http({ 
            method:'POST', 
            url:"../upload.do", 
            data: formData, 
            headers: {'Content-Type':undefined}, //证明是文件上传
            transformRequest: angular.identity 	//对该表单二进制序列化
        });  	 
 	}  
}); 
