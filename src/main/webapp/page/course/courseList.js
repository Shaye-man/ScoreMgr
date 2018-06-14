layui.use(['form','layer','laydate','table','laytpl'],function(){
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        laytpl = layui.laytpl,
        table = layui.table;

    //新闻列表
    var tableIns = table.render({
        elem: '#courseList',
        url : '../../json/courseList.json',
        cellMinWidth : 95,
        page : true,
        height : "full-125",
        limit : 20,
        limits : [10,15,20,25],
        id : "courseListTable",
        cols : [[
            {type: "checkbox", fixed:"left", width:50},
            {field: 'id', title: 'ID', width:60, align:"center",sort:true},
            {field: 'name', title: '课程名', width:150,align:'center',sort:true},
            {field: 'pName', title: '先修课', align:'center',width:100},
            {field: 'credit', title: '学分', width:80,align:'center'},
            {field: 'clazzhour', title: '课时', width:80,align:'center'},
            {field: 'address', title: '上课地点', width:150,align:'center'},
            {field: 'info', title: '课程简介', width:250,align:'center'},
            {field: 'tName', title: '授课教师', width:100,align:'center',sort:true},
            {field: 'clazzName', title: '上课班级', width:200,align:'center',sort:true},
            {field: 'createTime', title: '创建时间', align:'center', minWidth:110, templet:function(d){
                return d.createTime.substring(0,10);
            }},
            {title: '操作', width:150, templet:'#courseListBar',fixed:"right",align:"center"}
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

    //添加文章
    function addCourse(edit){
        var index = layui.layer.open({
            title : "添加课程",
            type : 2,
            content : "courseAdd.html",
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
        layui.layer.full(index);
        //改变窗口大小时，重置弹窗的宽高，防止超出可视区域（如F12调出debug的操作）
        $(window).on("resize",function(){
            layui.layer.full(index);
        })
    }
    $(".addCourse_btn").click(function(){
        addCourse();
    })

    //批量删除
    $(".delAll_btn").click(function(){
        var checkStatus = table.checkStatus('newsListTable'),
            data = checkStatus.data,
            newsId = [];
        if(data.length > 0) {
            for (var i in data) {
                newsId.push(data[i].newsId);
            }
            layer.confirm('确定删除选中的班级？', {icon: 3, title: '提示信息'}, function (index) {
                // $.get("删除文章接口",{
                //     newsId : newsId  //将需要删除的newsId作为参数传入
                // },function(data){
                tableIns.reload();
                layer.close(index);
                // })
            })
        }else{
            layer.msg("请选择需要删除的班级");
        }
    })

    //列表操作
    table.on('tool(courseList)', function(obj){
        var layEvent = obj.event,
            data = obj.data;

        if(layEvent === 'edit'){ //编辑
            addCourse(data);
        } 
        else if(layEvent === 'del'){ //删除
            layer.confirm('确定删除此班级？',{icon:3, title:'提示信息'},function(index){
                // $.get("删除文章接口",{
                //     newsId : data.newsId  //将需要删除的newsId作为参数传入
                // },function(data){
                    tableIns.reload();
                    layer.close(index);
                // })
            });
        }
    });

})