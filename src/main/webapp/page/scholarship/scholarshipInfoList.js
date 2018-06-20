layui.use(['form','layer','laydate','table','laytpl'],function(){
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
    
    //新闻列表
    var tableIns = table.render({
        elem: '#scholarshipInfoList',
        url : path+'/page/scholarship/countList',
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

})