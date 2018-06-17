layui.use(['form','layer','laydate','table','laytpl'],function(){
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        laytpl = layui.laytpl,
        table = layui.table;

    permissionCtrl();
    
    function permissionCtrl(){
    	var role = window.sessionStorage.getItem("ROLE");
    	if(role == "student"){
    		$(".addNews_btn").addClass("layui-hide");
    		$(".delAll_btn").addClass("layui-hide");
    	}
    }
    
    //新闻列表
    var tableIns = table.render({
        elem: '#newsList',
        url : '/page/news/list',
        cellMinWidth : 95,
        page : true,
        height : "full-125",
        limit : 10,
        limits : [10,15,20,25],
        id : "newsListTable",
        cols : [[
            {type: "checkbox", fixed:"left", width:50},
            {field: 'id', title: 'ID', width:60, align:"center"},
            {field: 'title', title: '标题', width:300,align:'center'},
            {field: 'author', title: '发布者', align:'center',width:120},
            {field: 'abstract', title: '概要', align:'center',maxWidth:200},
            {field: 'content', title: '内容', width:350,align:'center'},
            {field: 'createTime', title: '发布时间', align:'center', minWidth:110, templet:function(d){
                return d.createTime.substring(0,10);
            }},
            {title: '操作', width:170, templet:'#newsListBar',fixed:"right",align:"center"}
        ]]
    });

    //搜索【此功能需要后台配合，所以暂时没有动态效果演示】
    $(".search_btn").on("click",function(){
        table.reload("newsListTable",{
            page: {
                curr: 1 //重新从第 1 页开始
            },
            where: {
            	url: '/page/news/list',
                keywords: $(".searchVal").val()  //搜索的关键字
            }
        })
    });

    //添加文章
    function addNews(edit){
        var index = layui.layer.open({
            title : "添加公告",
            type : 2,
            content : "newsAdd.html",
            success : function(layero, index){
                var body = layui.layer.getChildFrame('body', index);
                if(edit){
                	body.find("#id").val(edit.id);
                    body.find(".title").val(edit.title);
                    body.find(".author").val(edit.author);
                    body.find(".abstract").val(edit.abstract);
                    body.find(".content").val(edit.content);
                    form.render();
                }
                setTimeout(function(){
                    layui.layer.tips('点击此处返回公告列表', '.layui-layer-setwin .layui-layer-close', {
                        tips: 3
                    });
                },500)
            }
        })
        layui.layer.full(index);
        //改变窗口大小时，重置弹窗的宽高，防止超出可视区域（如F12调出debug的操作）
        $(window).on("resize",function(){
            layui.layer.full(index);
        })
    }
    $(".addNews_btn").click(function(){
        addNews();
    })

    //批量删除
    $(".delAll_btn").click(function(){
        var checkStatus = table.checkStatus('newsListTable'),
            data = checkStatus.data;

        if(data.length > 0) {
            var ids = "";
        	for(var i=0; i<data.length; i++){
        		ids += data[i].id+",";
        	}
        	ids=ids.substring(0, ids.length-1);
            layer.confirm('确定删除选中的公告？', {icon: 3, title: '提示信息'}, function (index) {
                 $.get("/page/news/batchRemove",{
                     ids : ids  //将需要删除的newsId作为参数传入
                 },function(responseM){
                	if(responseM.code == 0){
                        layer.msg("公告删除成功！");
                        tableIns.reload();
    	                layer.close(index);
                	}else{
                		 layer.msg("公告删除失败！");
                	}
                 })
            })
        }else{
            layer.msg("请选择需要删除的用户");
        }
    })

    //列表操作
    table.on('tool(newsList)', function(obj){
        var layEvent = obj.event,
            data = obj.data;
        
    	var role = window.sessionStorage.getItem("ROLE");
    	var permit = true;
    	if(role == "student"){
    		permit = false;	
    	}

        if(layEvent === 'edit'){ //编辑
        	if(permit){
        		addNews(data);
        	}else{
        		layer.msg("你无权进行此操作！");
        	}
        } 
        else if(layEvent === 'del'){ //删除
        	if(permit){
                layer.confirm('确定删除此公告？',{icon:3, title:'提示信息'},function(index){
                    $.get("/page/news/delete",{
                        id : data.id
                    },function(ret){
                   	if(ret.state == "ok"){
                           layer.msg("公告删除成功！");
                           tableIns.reload();
                           layer.close(index);
                   	}else{
                   		layer.msg("公告删除失败！");
                   	}
                    })
               });
        	}else{
        		layer.msg("你无权进行此操作！");
        	}
       }
    });

})