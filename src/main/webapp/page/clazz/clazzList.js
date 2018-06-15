layui.use(['form','layer','laydate','table','laytpl'],function(){
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        laytpl = layui.laytpl,
        table = layui.table;

    //新闻列表
    var tableIns = table.render({
        elem: '#clazzList',
        url : '/page/clazz/list',
        cellMinWidth : 95,
        page : true,
        height : "full-125",
        limit : 20,
        limits : [10,15,20,25],
        id : "clazzListTable",
        cols : [[
            {type: "checkbox", fixed:"left", width:50},
            {field: 'id', title: 'ID', width:60, align:"center",sort:true},
            {field: 'name', title: '班级全称', width:300,align:'center'},
            {field: 'grade', title: '年级', align:'center',width:80,sort:true},
            {field: 'academy', title: '学院', width:200,align:'center',sort:true},
            {field: 'major', title: '专业', width:250,align:'center',sort:true},
            {field: 'num', title: '班别', width:80,align:'center'},
            {field: 'createTime', title: '发布时间', align:'center', minWidth:110, templet:function(d){
                return d.createTime.substring(0,10);
            }},
            {title: '操作', width:170, templet:'#clazzListBar',fixed:"right",align:"center"}
        ]]
    });

    //搜索【此功能需要后台配合，所以暂时没有动态效果演示】
    $(".search_btn").on("click",function(){
        table.reload("clazzListTable",{
            page: {
                curr: 1 //重新从第 1 页开始
            },
            where: {
            	url: '/page/clazz/list',
                keywords: $(".searchVal").val()  //搜索的关键字
            }
        });
    });

    //添加文章
    function addClazz(edit){
        var index = layui.layer.open({
            title : "添加班级",
            type : 2,
            content : "clazzAdd.html",
            success : function(layero, index){
                var body = layui.layer.getChildFrame('body', index);
                if(edit){
                	body.find("#id").val(edit.id);
                    body.find("#name").val(edit.name);
                    body.find(".grade").val(edit.grade);
                    body.find(".academy").val(edit.academy);
                    body.find(".major").val(edit.major);
                    body.find(".num").val(edit.num);
                    form.render();
                }
                setTimeout(function(){
                    layui.layer.tips('点击此处返回班级列表', '.layui-layer-setwin .layui-layer-close', {
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
    $(".addClazz_btn").click(function(){
        addClazz();
    });

  //批量删除
    $(".delAll_btn").click(function(){
        var checkStatus = table.checkStatus('clazzListTable'),
            data = checkStatus.data;

        if(data.length > 0) {
            var ids = "";
        	for(var i=0; i<data.length; i++){
        		ids += data[i].id+",";
        	}
        	ids=ids.substring(0, ids.length-1);
            layer.confirm('确定删除选中的班级？', {icon: 3, title: '提示信息'}, function (index) {
                 $.get("/page/clazz/batchRemove",{
                     ids : ids  
                 },function(responseM){
                	if(responseM.code == 0){
                        layer.msg("班级删除成功！");
    	                tableIns.reload();
    	                layer.close(index);
                	}else{
                		 layer.msg("班级删除失败！");
                	}
                 })
            })
        }else{
            layer.msg("请选择需要删除的班级");
        }
    })

    //列表操作
    table.on('tool(clazzList)', function(obj){
        var layEvent = obj.event,
            data = obj.data;

        if(layEvent === 'edit'){ //编辑
            addClazz(data);
        } 
        else if(layEvent === 'del'){ //删除
            layer.confirm('确定删除此班级？',{icon:3, title:'提示信息'},function(index){
                 $.get("/page/course/delete",{
                     id : data.id  //将需要删除的newsId作为参数传入
                 },function(ret){
                	if(ret.state == "ok"){
                        layer.msg("班级删除成功！");
                        layer.close(index);
                        tableIns.reload();
                	}else{
                		layer.msg("班级删除失败！");
                	}
                 })
            });
        }
    });

})