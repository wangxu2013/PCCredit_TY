/**
 * 工厂模式
 */
var crud = crud || {};
crud.dom = crud.dom || {};

//带auth的GET
crud.dom.GETAuth = function() {
    this.doGetAuth = function(url,auth,callback,errorMsg) {
        $.ajax({
            url : wsHost + url,
            type : "GET",
            timeout : 10000,
            dataType : 'text',
            beforeSend : function(req) {
                req.setRequestHeader('Authorization', auth);
            },
            success : function(json) {
                //回调
                callback(json);
            },
            error : function(e,xhr,opt) {
                // 请求出错处理
                //alert("请求出错(请检查网络连接.)");
                alert(errorMsg);
            }
        });
    };
};

// 子函数1：GET--获取
crud.dom.GET = function() {
    this.doGet = function(url, callback,errorMsg) {
        $.ajax({
            url : wsHost + url,
            type : "GET",
            timeout : 10000,
            dataType : 'text',
            success : function(json) {
                //回调
                callback(json);
            },
            error : function(e,xhr,opt) {
                // 请求出错处理
                //alert("请求出错(请检查网络连接.)");
                alert(errorMsg);
            }
        });
    };
};

// 子函数2：POST--添加
crud.dom.POST = function() {
    this.doPost = function(url,data,callback,errorMsg) {
        $.ajax({
            url : wsHost + url,
            type : "POST",
            timeout : 10000,
            data: data,
            dataType : 'text',
            success : function(json) {
                //回调
                callback(json);
            },
            error : function(xhr) {
                // 请求出错处理
                //alert("请求出错(请检查网络连接.)");
                alert(errorMsg);
            }
        });
    };
};

// 子函数3：PUT--更新
crud.dom.PUT = function() {
    this.doPut = function(url,data,callback,errorMsg) {
        $.ajax({
            url : wsHost + url,
            type : "PUT",
            timeout : 10000,
            data: data,
            dataType : 'text',
            success : function(json) {
                //回调
                callback(json);
            },
            error : function(xhr) {
                // 请求出错处理
                //alert("请求出错(请检查网络连接.)");
                alert(errorMsg);
            }
        });
    };
};

//子函数4：DELETE--删除
crud.dom.DELETE = function() {
    this.doDelete = function(url,data,callback,errorMsg) {
        $.ajax({
            url : wsHost + url,
            type : "DELETE",
            timeout : 10000,
            data: data,
            dataType : 'text',
            success : function(json) {
                //回调
                callback(json);
            },
            error : function(xhr) {
                // 请求出错处理
                //alert("请求出错(请检查网络连接.)");
                alert(errorMsg);
            }
        });
    };
};

//子函数5:MEDIA--上传文件
crud.dom.MEDIA = function() {
    this.doMedia = function upload(Url,type,UpLoadCallBack,errorMsg) {
        var options = new FileUploadOptions();
        options.fileKey="file";
        options.fileName=Url.substr(Url.lastIndexOf('/')+1);
        //options.mimeType="image/jpeg";
        //options.mimeType="multipart/form-data";
        
        //var params = new Object();
        //params.value1 = "test";
        //params.value2 = "param";

        //options.params = params;
        options.chunkedMode = false;

        var ft = new FileTransfer();
        if(type == "img"){
            ft.upload(Url, encodeURI(wsHost + wsImg + "?file_path=" + Url), UpLoadCallBack, fail, options);
        }
        else if(type == "file"){
            ft.upload(Url, encodeURI(wsHost + wsFile + "?file_path=" + Url), UpLoadCallBack, fail, options);
        }
        else if(type == "video"){
            ft.upload(Url, encodeURI(wsHost + wsVideo + "?file_path=" + Url), UpLoadCallBack, fail, options);
        }
        else if(type == "voice"){
            ft.upload(Url, encodeURI(wsHost + wsVoice + "?file_path=" + Url), UpLoadCallBack, fail, options);
        }
        
        function fail(error) {
            //alert("An error has occurred: Code = " + error.code);
            alert(errorMsg);
        }
    };
};

//子函数6:MEDIAFILE--上传文件
crud.dom.MEDIAFILE = function() {
    this.doMediaFile = function upload(mediaFile,type,UpLoadCallBack,errorMsg) {
        var ft = new FileTransfer(), 
        path = mediaFile.fullPath, 
        name = mediaFile.name;
        if(type == "img"){
            ft.upload(path, encodeURI(wsHost + wsImg + "?file_path=" + name), UpLoadCallBack, fail, { fileName: name });
        }
        else if(type == "file"){
            ft.upload(path, encodeURI(wsHost + wsFile + "?file_path=" + name), UpLoadCallBack, fail, { fileName: name });
        }
        else if(type == "video"){
            ft.upload(path, encodeURI(wsHost + wsVideo + "?file_path=" + name), UpLoadCallBack, fail, { fileName: name });
        }
        else if(type == "voice"){
            ft.upload(path, encodeURI(wsHost + wsVoice + "?file_path=" + name), UpLoadCallBack, fail, { fileName: name });
        } 
        
        function fail(error) {
            //alert("An error has occurred: Code = " + error.code);
            alert(errorMsg);
        }
    };
};
  
// 工厂方法接口
// [javascript]
crud.dom.factory = function(operation) {
    return new crud.dom[operation];
}