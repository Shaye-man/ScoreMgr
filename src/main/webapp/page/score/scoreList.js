layui.use(['form','layer','laydate','table','laytpl'],function(){
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        laytpl = layui.laytpl,
        table = layui.table;

    //新闻列表
    var tableIns = table.render({
        elem: '#scoreList',
        url : '../../json/scoreList.json',
        cellMinWidth : 95,
        page : true,
        height : "full-125",
        limit : 20,
        limits : [10,15,20,25],
        id : "scoreListTable",
        cols : [[
            {field: 'sId', title: '学号', width:180, align:"center",sort:true},
            {field: 'sName', title: '课程名', width:150,align:'center',sort:true},
            {field: 'cId', title: '课程号', align:'center',width:100},
            {field: 'cName', title: '课程名', width:150,align:'center'},
            {field: 'clazzName', title: '上课班级', width:280,align:'center',sort:true},
            {field: 'tName', title: '授课教师', width:100,align:'center',sort:true},
            {field: 'score', title: '成绩', width:100,align:'center',sort:true,edit:true},
            {field: 'createTime', title: '录入时间', align:'center', minWidth:110, templet:function(d){
                return d.createTime.substring(0,10);
            }},
            {title: '操作', width:150, templet:'#scoreListBar',fixed:"right",align:"center"}
        ]]
    });

    //搜索【此功能需要后台配合，所以暂时没有动态效果演示】
    $(".search_btn").on("click",function(){
        if($(".searchVal").val() != ''){
            table.reload("newsListTable",{
                page: {
                    curr: 1 //重新从第 1 页开始
                },
                where: {
                    key: $(".searchVal").val()  //搜索的关键字
                }
            })
        }else{
            layer.msg("请输入搜索的内容");
        }
    });

/*
    function addscore(edit){
        var index = layui.layer.open({
            title : "成绩录入",
            type : 2,
            area: ['500px', '300px'],
            content : "scoreAdd.html",
            success : function(layero, index){
                var body = layui.layer.getChildFrame('body', index);
                if(edit){
                    body.find("id").val(edit.id);
                    body.find(".name").val(edit.name);
                    body.find(".pName").val(edit.pName);
                    body.find(".credit").val(edit.credit);
                    body.find(".clazzhour").val(edit.clazzhour);
                    body.find(".address").val(edit.address);
                    body.find(".info").val(edit.info);
                    body.find(".tName").val(edit.tName);
                    body.find(".clazzName").val(edit.clazzName);                    
                    form.render();
                }
                setTimeout(function(){
                    layui.layer.tips('点击此处返回课程列表', '.layui-layer-setwin .layui-layer-close', {
                        tips: 3
                    });
                },500)
            }
        })
        layui.layer.open(index);
    }

    */
   
    function addscore(obj){
        var data = obj.data;
        layer.prompt({
            title: '修改['+ data.sName +'] - ['+ data.cName +']的成绩为',
            value: data.score
        }, 
        function(value, index){
            layer.close(index);
        
            //这里一般是发送修改的Ajax请求
        
            //同步更新表格和缓存对应的值
            obj.update({
                score: value
            });
        });
    }

    //列表操作
    table.on('tool(scoreList)', function(obj){
        var layEvent = obj.event,
            data = obj.data;

        if(layEvent === 'edit'){ //编辑
            addscore(obj);
        }
    });

})