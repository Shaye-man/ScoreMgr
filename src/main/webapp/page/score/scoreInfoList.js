layui.use(['form','layer','laydate','table','laytpl'],function(){
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        laytpl = layui.laytpl,
        table = layui.table;

    var tableIns = table.render({
        elem: '#scoreList',
        url : '/page/score/allScoreList',
        cellMinWidth : 95,
        page : true,
        height : "full-125",
        limit : 10,
        limits : [10,15,20,25],
        id : "scoreListTable",
        cols : [[
            {field: 'sId', title: '学号', width:180, align:"center",sort:true},
            {field: 'sName', title: '姓名', width:120,align:'center'},
            {field: 'cName', title: '课程名', width:150,align:'center'},
            {field: 'clazzName', title: '上课班级', width:280,align:'center',sort:true},
            {field: 'score', title: '成绩',align:'center',sort:true,templet:function(d){
                if(d.score != null){
                	if(d.score >= 60){
                        return '<span class="layui-blue">'+d.score+'</span>'
                    }else{
                        return '<span class="layui-red">'+d.score+'</span>'
                    }
                }else{
                	return '<span class="layui-badge-rim layui-bg-orange">未录入</span>'
                }
            }}
        ]]
    });

    table.render({
            elem: '#clazzList',
            url : '/page/score/clazzScoreList',
            cellMinWidth : 95,
            page : true,
            height : "full-125",
            limit : 10,
            limits : [10,15,20,25],
            id : "clazzListTable",
            cols : [[
                {field: 'clazzName', title: '班级全称',align:'center',width:200},
                {field: 'totalScore', title: '总分', align:'center',sort:true},
                {field: 'averageScore', title: '平均分', align:'center',sort:true,templet:function(d){
                	if(d.averageScore >= 90){
                        return '<span class="layui-blue">'+d.averageScore+'</span>'
                    }
                	else if(d.averageScore < 60){
                        return '<span class="layui-red">'+d.averageScore+'</span>'
                    }
                	else return d.averageScore;
                }},
                {field: 'highScore', title: '最高分', align:'center',sort:true,templet:function(d){
                	if(d.highScore >= 90){
                        return '<span class="layui-blue">'+d.highScore+'</span>'
                    }
                	else if(d.highScore < 60){
                        return '<span class="layui-red">'+d.highScore+'</span>'
                    }
                	else return d.highScore;
                }},
                {field: 'minScore', title: '最低分', align:'center',sort:true,templet:function(d){
                	if(d.minScore >= 90){
                        return '<span class="layui-blue">'+d.minScore+'</span>'
                    }
                	else if(d.minScore < 60){
                        return '<span class="layui-red">'+d.minScore+'</span>'
                    }
                	else return d.minScore;
                }}
            ]]
        });

    //搜索区域
    $(".searchAll_btn").on("click",function(){
        table.reload("scoreListTable",{
            page: {
                curr: 1 //重新从第 1 页开始
            },
            where: {
            	url: '/page/score/allScoreList',
                keywords: $(".searchAllVal").val()  //搜索的关键字
            }
        })
    });
    
    $(".searchClazz_btn").on("click",function(){
        table.reload("clazzListTable",{
            page: {
                curr: 1 //重新从第 1 页开始
            },
            where: {
            	url: '/page/score/clazzScoreList',
                keywords: $(".searchClazzVal").val()  //搜索的关键字
            }
        })
    });
  
 // 基于准备好的dom，初始化echarts实例
    var scoreChart = echarts.init(document.getElementById('scoreLine'));
    var clazzChart = echarts.init(document.getElementById('clazzLine'));

    // 指定图表的配置项和数据
    var scoreOption = {
        title: {
            text: 'ECharts 入门示例'
        },
        tooltip: {},
        legend: {
            data:['销量']
        },
        xAxis: {
            data: ["衬衫","羊毛衫","雪纺衫","裤子","高跟鞋","袜子"]
        },
        yAxis: {},
        series: [{
            name: '销量',
            type: 'bar',
            data: [5, 20, 36, 10, 10, 20]
        }]
    };


option = {
    tooltip: {
        trigger: 'axis',
        axisPointer: {
            type: 'shadow'
        }
    },
    legend: {
        data: ['16自动化电气工程（1）班', '16自动化物联网（3）班']
    },
    grid: {
        left: '3%',
        right: '4%',
        bottom: '3%',
        containLabel: true
    },
    xAxis: {
        type: 'value',
        boundaryGap: [0, 0.01]
    },
    yAxis: {
        type: 'category',
        data: ['总分','平均分','最高分','最低分']
    },
    series: [{
		name: '16自动化电气工程（1）班',
		type: 'bar',
		data: [248, 82.67, 92, 68]
    	}, 
		{
		name: '16自动化物联网（3）班',
		type: 'bar',
		data: [391, 78.20, 98, 46]
	}]
};
    
    $.get('/page/score/clazzScoreList').done(function (data) {
    	var	clazzNames = "";
    		clazzNames += "[";
    	var	datas = "";
    		datas += "[";
    	for(var i=0;i<data.data.length;i++){
    		clazzNames += "'"+data.data[i].clazzName+"',";
    		datas += "{name:'"+data.data[i].clazzName+"'";
    		datas += ",type:'bar'";
    		datas += ",data:["+data.data[i].minScore+","+data.data[i].highScore;
    		datas += ","+data.data[i].averageScore+","+data.data[i].totalScore+"]";
    		datas += "},"
    	}
    	clazzNames = clazzNames.substring(0, clazzNames.length-1);
    	clazzNames += "]";
    	datas = datas.substring(0, datas.length-1);
    	datas += "]";
    	
    	datas = eval('(' + datas + ')');
    	clazzNames = eval('(' + clazzNames + ')');
    	clazzChart.setOption({
		    tooltip: {
		        trigger: 'axis',
		        axisPointer: {
		            type: 'shadow'
		        }
		    },
		    legend: {
		        data: clazzNames
		    },
		    grid: {
		        left: '3%',
		        right: '4%',
		        bottom: '3%',
		        containLabel: true
		    },
		    xAxis: {
		        type: 'value',
		        boundaryGap: [0, 0.01]
		    },
		    yAxis: {
		        type: 'category',
		        data: ['最低分','最高分','平均分','总分']
		    },
		    series: datas
		});
    });
    
    // 使用刚指定的配置项和数据显示图表。
    scoreChart.setOption(scoreOption);
})