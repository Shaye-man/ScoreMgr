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
            {field: 'sName', title: '姓名', width:150,align:'center',sort:true},
            {field: 'cId', title: '课程号', align:'center',width:100},
            {field: 'cName', title: '课程名', width:150,align:'center'},
            {field: 'clazzName', title: '上课班级', width:280,align:'center',sort:true},
            {field: 'tName', title: '授课教师', width:100,align:'center',sort:true},
            {field: 'score', title: '成绩', width:100,align:'center',sort:true,edit:true,templet:function(d){
                if(d.score >= 60){
                    return '<span class="layui-blue">'+d.score+'</span>'
                }else{
                    return '<span class="layui-red">'+d.score+'</span>'
                }
            }},
            {field: 'createTime', title: '录入时间', align:'center', minWidth:110, templet:function(d){
                return d.createTime.substring(0,10);
            }},
            {title: '操作', width:150, templet:'#scoreListBar',fixed:"right",align:"center"}
        ]]
    });

    //搜索【此功能需要后台配合，所以暂时没有动态效果演示】
    $(".search_btn").on("click",function(){
        if($(".searchVal").val() != ''){
            table.reload("scoreListTable",{
                page: {
                    curr: 1 //重新从第 1 页开始
                },
                where: {
                	url: '/page/score/list'
                    key: $(".searchVal").val()  //搜索的关键字
                }
            })
        }else{
            layer.msg("请输入搜索的内容");
        }
    });
   
    function addscore(obj){
        var data = obj.data;
        layer.prompt({
            title: '修改['+ data.sName +'] - ['+ data.cName +']的成绩为',
            value: data.score
        }, 
        function(value, index,data){
            layer.close(index);
        
            //这里一般是发送修改的Ajax请求
            $.ajax({
        		  url: '/page/score/submit',
        		  data: {
        			  sid : data.sId,
        			  cid : data.cId,
        			  score : data.score
                  },
        		  dataType: 'json',
        		  type: 'post',
        		  success: function(ret){
        			  if(ret.state=="ok"){
        				  top.layer.msg("成绩录入成功！");
        		            //同步更新表格和缓存对应的值
        		            obj.update({
        		                score: value
        		            });
        			  } else {
        				  top.layer.msg("成绩录入失败！");
        			  }
        		  },
        	  	  error: function(){
        	  		top.layer.msg("成绩录入失败！");
        			  }
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