layui.use(['form','layer','table','laytpl'],function(){
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        laytpl = layui.laytpl,
        table = layui.table;

    function getRootPath(){  
        var curPath=window.document.location.href;  
        var pathName=window.document.location.pathname;  
        var pos=curPath.indexOf(pathName);  
        var localhostPaht=curPath.substring(0,pos);  
        var projectName=pathName.substring(0,pathName.substr(1).indexOf('/')+1);  
        return localhostPaht+projectName;
    }
        
    var path = getRootPath();
    
    //用户列表
    var tableIns = table.render({
        elem: '#teacherList',
        url : path+'/page/user/teacher/list',
        cellMinWidth : 95,
        page : true,
        height : "full-125",
        limits : [10,15,20,25],
        limit : 20,
        id : "teacherListTable",
        cols : [[
            {type: "checkbox", fixed:"left", width:50},
            {field: 'id', title: 'ID', width:60, align:"center"},
            {field: 'username', title: '用户名', width:120, align:"center"},
            {field: 'name', title: '姓名', width:110,align:'center'},

            {field: 'sex', title: '性别', align:'center',width:60,templet:function(d){
            	if(d.sex == "M") return "男";
            	else if(d.sex == "F") return "女";
            	else return "保密";
            }},
            {field: 'role', title: '用户组', width:80,align:'center',templet:function(d){
                if(d.role == "teacher"){
                    return '<span class="layui-blue">教师</span>'
                }else{
                    return '<span class="layui-red">管理员</span>'
                }
            }},
            {field: 'phone', title: '手机',  align:'center',maxWidth:110},
            {field: 'introduce', title: '简介', align:'center',maxWidth:150},
            {field: 'mailbox', title: '用户邮箱', minWidth:200, align:'center',templet:function(d){
                return '<a class="layui-blue" href="mailto:'+d.mailbox+'">'+d.mailbox+'</a>';
            }},
            {field: 'createtime', title: '注册时间', align:'center',minWidth:150},
            {title: '操作', minWidth:175, templet:'#teacherListBar',fixed:"right",align:"center"}
        ]]
    });

    //搜索【此功能需要后台配合，所以暂时没有动态效果演示】
    $(".search_btn").on("click",function(){
        table.reload("teacherListTable",{
            page: {
                curr: 1 //重新从第 1 页开始
            },
            where: {
            	url: path+'/page/user/teacher/list',
                keywords: $(".searchVal").val()  //搜索的关键字
            }
        })
    });

    //添加用户
    function addUser(edit){
        var index = layui.layer.open({
            title : "添加用户",
            type : 2,
            content : "teacherAdd.html",
            success : function(layero, index){
                var body = layui.layer.getChildFrame('body', index);
                if(edit){
                	body.find(".id").val(edit.id);
                    body.find(".username").val(edit.username);  
                    body.find(".name").val(edit.name);  
                    body.find(".sex input[value="+edit.sex+"]").prop("checked","checked"); 
                    body.find(".mailbox").val(edit.mailbox);  
                    body.find(".introduce").val(edit.introduce);    
                    body.find(".role").val(edit.role);    
                    form.render();
                }
                setTimeout(function(){
                    layui.layer.tips('点击此处返回用户列表', '.layui-layer-setwin .layui-layer-close', {
                        tips: 3
                    });
                },500)
            }
        })
        layui.layer.full(index);
        window.sessionStorage.setItem("index",index);
        //改变窗口大小时，重置弹窗的宽高，防止超出可视区域（如F12调出debug的操作）
        $(window).on("resize",function(){
            layui.layer.full(window.sessionStorage.getItem("index"));
        })
    }
    $(".addNews_btn").click(function(){
        addUser();
    })

       //批量删除
    $(".delAll_btn").click(function(){
        var checkStatus = table.checkStatus('teacherListTable'),
            data = checkStatus.data;

        if(data.length > 0) {
            var ids = "";
        	for(var i=0; i<data.length; i++){
        		ids += data[i].id+",";
        	}
        	ids=ids.substring(0, ids.length-1);
            layer.confirm('确定删除选中的用户？', {icon: 3, title: '提示信息'}, function (index) {
                 $.get(path+"/page/user/teacher/batchRemove",{
                     ids : ids  
                 },function(responseM){
                	if(responseM.code == 0){
                        layer.msg("用户删除成功！");
    	                tableIns.reload();
    	                layer.close(index);
                	}else{
                		 layer.msg("用户删除失败！");
                	}
                 })
            })
        }else{
            layer.msg("请选择需要删除的用户");
        }
    })

    //列表操作
    table.on('tool(teacherList)', function(obj){
        var layEvent = obj.event,
            data = obj.data;

        if(layEvent === 'edit'){ //编辑
            addUser(data);
        }else if(layEvent === 'usable'){ //启用禁用
            var _this = $(this),
                usableText = "是否确定禁用此用户？",
                btnText = "已禁用";
            if(_this.text()=="已禁用"){
                usableText = "是否确定启用此用户？",
                btnText = "已启用";
            }
            layer.confirm(usableText,{
                icon: 3,
                title:'系统提示',
                cancel : function(index){
                    layer.close(index);
                }
            },function(index){
                _this.text(btnText);
                layer.close(index);
            },function(index){
                layer.close(index);
            });
        }else if(layEvent === 'del'){ //删除
            layer.confirm('确定删除此用户？',{icon:3, title:'提示信息'},function(index){
                $.get(path+"/page/user/teacher/delete",{
                    id : data.id  //将需要删除的newsId作为参数传入
                },function(ret){
               	if(ret.state == "ok"){
                       tableIns.reload();
                       layer.msg("用户删除成功！");
                       layer.close(index);
               	}else{
               		layer.msg("用户删除失败！");
               	}
                })
           });
       }
    });

})
