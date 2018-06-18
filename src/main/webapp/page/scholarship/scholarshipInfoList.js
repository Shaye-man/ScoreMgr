layui.use(['form','layer','laydate','table','laytpl'],function(){
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        laytpl = layui.laytpl,
        table = layui.table;

    //新闻列表
    var tableIns = table.render({
        elem: '#scholarshipInfoList',
        url : '/page/scholarship/countList',
        cellMinWidth : 95,
        page : true,
        height : "full-125",
        limit : 20,
        limits : [10,15,20,25],
        id : "scholarshipListTable",
        cols : [[
            {type: "checkbox", fixed:"left", width:50},
            {field: 'sId', title: '学号', align:"center"},
            {field: 'sName', title: '姓名', align:"center"},
            {field: 'clazzName', title: '班级',align:'center',sort:true},
            {field: 'totalScore', title: '总分', align:'center',sort:true},
            {field: 'rank', title: '奖项',align:'center',templet:function(d){
            	if(d.rank != "无"){
            		return '<span class="layui-red">'+d.rank+'</span>'
            	} else {
            		return '无';
            	}
            }},
            {field: 'price', title: '金额', align:'center'},
            {title: '操作', minWidth:120, templet:'#scholarshipInfoListBar',fixed:"right",align:"center"}
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

    //列表操作
    table.on('tool(scholarshipList)', function(obj){
        var layEvent = obj.event,
            data = obj.data;

        if(layEvent === 'edit'){ //编辑
            addscholarship(data);
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