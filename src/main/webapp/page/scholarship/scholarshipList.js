layui.use(['form','layer','laydate','table','laytpl'],function(){
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        laytpl = layui.laytpl,
        table = layui.table;

    table.render({
        elem: '#scholarshipList',
        url : '/page/scholarship/list',
        cellMinWidth : 95,
        page : true,
        height : "full-125",
        limit : 20,
        limits : [10,15,20,25],
        id : "scholarshipListTable",
        cols : [[
            {type: "checkbox", fixed:"left", width:50},
            {field: 'id', title: 'ID', width:60, align:"center",sort:true},
            {field: 'rank', title: '奖项', width:180,align:'center'},
            {field: 'price', title: '金额', width:150, align:'center',sort:true},
            {field: 'num', title: '数目', width:150,align:'center',sort:true},
            {field: 'info', title: '备注', width:350,align:'center',templet:function(d){
                if (d.info === "") {
                    return "无";
                }else{
                    return d.info;
                }
            }},
            {field: 'createtime', title: '发布时间', align:'center', minWidth:110, templet:function(d){
                return d.createTime.substring(0,10);
            }},
            {title: '操作', width:170, templet:'#scholarshipListBar',fixed:"right",align:"center"}
        ]]
    });

    //搜索【此功能需要后台配合，所以暂时没有动态效果演示】
    $(".search_btn").on("click",function(){
        table.reload("scholarshipListTable",{
            page: {
                curr: 1 //重新从第 1 页开始
            },
            where: {
            	url: '/page/scholarship/list',
                keywords: $(".searchVal").val()  //搜索的关键字
            }
        })
    });

    //添加文章
    function addscholarship(edit){
        var index = layui.layer.open({
            title : "添加奖项",
            type : 2,
            area: ['450px', '400px'],
            content : "scholarshipAdd.html",
            success : function(layero, index){
                var body = layui.layer.getChildFrame('body', index);
                if(edit){
                    body.find("#id").val(edit.id);
                    body.find(".rank").val(edit.rank);
                    body.find(".price").val(edit.price);
                    body.find(".num").val(edit.num);
                    body.find(".info").val(edit.info);
                    form.render();
                }
                setTimeout(function(){
                    layui.layer.tips('点击此处返回奖学金列表', '.layui-layer-setwin .layui-layer-close', {
                        tips: 3
                    });
                },500)
            }
        })
    }
    $(".addScholarship_btn").click(function(){
        addscholarship();
    })

    //批量删除
    $(".delAll_btn").click(function(){
        var checkStatus = table.checkStatus('scholarshipListTable'),
            data = checkStatus.data;

        if(data.length > 0) {
            var ids = "";
        	for(var i=0; i<data.length; i++){
        		ids += data[i].id+",";
        	}
        	ids=ids.substring(0, ids.length-1);
            layer.confirm('确定删除选中的奖项？', {icon: 3, title: '提示信息'}, function (index) {
                 $.get("/page/scholarship/batchRemove",{
                     ids : ids  
                 },function(responseM){
                	if(responseM.code == 0){
                        layer.msg("奖项删除成功！");
                        tableIns.reload();
    	                layer.close(index);
                	}else{
                		 layer.msg("奖项删除失败！");
                	}
                 })
            })
        }else{
            layer.msg("请选择需要删除的奖项");
        }
    });
    
    //列表操作
    table.on('tool(scholarshipList)', function(obj){
        var layEvent = obj.event,
            data = obj.data;

        if(layEvent === 'edit'){ //编辑
            addscholarship(data);
        } 
        else if(layEvent === 'del'){ //删除
            layer.confirm('确定删除此奖项？',{icon:3, title:'提示信息'},function(index){
                $.get("/page/scholarship/delete",{
                    id : data.id
                },function(ret){
               	if(ret.state == "ok"){
                       layer.msg("奖项删除成功！");
                       tableIns.reload();
                       layer.close(index);
               	}else{
               		layer.msg("奖项删除失败！");
               	}
                })
           });
       }
    });

})