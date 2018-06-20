layui.use(['form','layer','laydate','table','laytpl'],function(){
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        laydate = layui.laydate,
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
    
    //添加验证规则
    form.verify({
        newPwd : function(value, item){
            if(value.length < 6){
                return "密码长度不能小于6位";
            }
        },
        confirmPwd : function(value, item){
            if(!new RegExp($("#oldPwd").val()).test(value)){
                return "两次输入密码不一致，请重新输入！";
            }
        }
    })

    //用户等级
    table.render({
        elem: '#userGrade',
        url : path+'/json/userGrade.json',
        cellMinWidth : 95,
        cols : [[
            {field:"id", title: 'ID', width: 60, fixed:"left",sort:"true", align:'center', edit: 'text'},
            {field: 'gradeIcon', title: '图标展示', templet:'#gradeIcon', align:'center'},
            {field: 'gradeName', title: '等级名称', edit: 'text', align:'center'},
            {field: 'gradeValue', title: '等级值', edit: 'text',sort:"true", align:'center'},
            {field: 'gradeGold', title: '默认金币', edit: 'text',sort:"true", align:'center'},
            {field: 'gradePoint', title: '默认积分', edit: 'text',sort:"true", align:'center'},
            {title: '当前状态',minWidth:100, templet:'#gradeBar',fixed:"right",align:"center"}
        ]]
    });

    form.on('switch(gradeStatus)', function(data){
        var tipText = '确定禁用当前会员等级？';
        if(data.elem.checked){
            tipText = '确定启用当前会员等级？'
        }
        layer.confirm(tipText,{
            icon: 3,
            title:'系统提示',
            cancel : function(index){
                data.elem.checked = !data.elem.checked;
                form.render();
                layer.close(index);
            }
        },function(index){
            layer.close(index);
        },function(index){
            data.elem.checked = !data.elem.checked;
            form.render();
            layer.close(index);
        });
    });

    //初始修改密码表单
	if(window.sessionStorage.getItem("NAME") != ""){
		$("#id").val(window.sessionStorage.getItem("USER_ID"));
		$(".userName").val(window.sessionStorage.getItem("NAME"));
	}
	
    //修改密码
	form.on("submit(changePwd)",function(data){
        //弹出loading
        var index = top.layer.msg('数据提交中，请稍候',{icon: 16,time:false,shade:0.8});
        
        // 实际使用时的提交信息
        $.ajax({
  		  url: path+'/page/mainIndex/changePwd',
  		  data: {
              id: $("#id").val(),
         	  oldPwd : $(".oldPwd").val(),
              newPwd : $(".newPwd").val(),
              confirmPwd : $(".confirmPwd").val()
          },
  		  dataType: 'json',
  		  type: 'post',
  		  success: function(responseM){
  			  if(responseM.flag){
         		 top.layer.msg(responseM.msg);
         		 form.render();
  			  } else {
  				  top.layer.msg(responseM.msg);
  			  }
  		  },
  	  	  error: function(){
  	  		top.layer.msg("密码修改失败！");
  			  }
  	  });
        return false;
    });
    
    //新增等级
    $(".addGrade").click(function(){
        var $tr = $(".layui-table-body.layui-table-main tbody tr:last");
        if($tr.data("index") < 9) {
            var newHtml = '<tr data-index="' + ($tr.data("index") + 1) + '">' +
                '<td data-field="id" data-edit="text" align="center"><div class="layui-table-cell laytable-cell-1-id">' + ($tr.data("index") + 2) + '</div></td>' +
                '<td data-field="gradeIcon" align="center" data-content="icon-vip' + ($tr.data("index") + 2) + '"><div class="layui-table-cell laytable-cell-1-gradeIcon"><span class="seraph vip' + ($tr.data("index") + 2) + ' icon-vip' + ($tr.data("index") + 2) + '"></span></div></td>' +
                '<td data-field="gradeName" data-edit="text" align="center"><div class="layui-table-cell laytable-cell-1-gradeName">请输入等级名称</div></td>' +
                '<td data-field="gradeValue" data-edit="text" align="center"><div class="layui-table-cell laytable-cell-1-gradeValue">0</div></td>' +
                '<td data-field="gradeGold" data-edit="text" align="center"><div class="layui-table-cell laytable-cell-1-gradeGold">0</div></td>' +
                '<td data-field="gradePoint" data-edit="text" align="center"><div class="layui-table-cell laytable-cell-1-gradePoint">0</div></td>' +
                '<td data-field="' + ($tr.data("index") + 1) + '" align="center" data-content="" data-minwidth="100"><div class="layui-table-cell laytable-cell-1-' + ($tr.data("index") + 1) + '"> <input type="checkbox" name="gradeStatus" lay-filter="gradeStatus" lay-skin="switch" lay-text="启用|禁用" checked=""><div class="layui-unselect layui-form-switch layui-form-onswitch" lay-skin="_switch"><em>启用</em><i></i></div></div></td>' +
                '</tr>';
            $(".layui-table-body.layui-table-main tbody").append(newHtml);
            $(".layui-table-fixed.layui-table-fixed-l tbody").append('<tr data-index="' + ($tr.data("index") + 1) + '"><td data-field="id" data-edit="text" align="center"><div class="layui-table-cell laytable-cell-1-id">' + ($tr.data("index") + 2) +'</div></td></tr>');
            $(".layui-table-fixed.layui-table-fixed-r tbody").append('<tr data-index="' + ($tr.data("index") + 1) + '"><td data-field="' + ($tr.data("index") + 1) + '" align="center" data-content="" data-minwidth="100"><div class="layui-table-cell laytable-cell-1-' + ($tr.data("index") + 1) + '"> <input type="checkbox" name="gradeStatus" lay-filter="gradeStatus" lay-skin="switch" lay-text="启用|禁用" checked=""><div class="layui-unselect layui-form-switch layui-form-onswitch" lay-skin="_switch"><em>启用</em><i></i></div></div></td></tr>');
            form.render();
        }else{
            layer.alert("模版中由于图标数量的原因，只支持到vip10，实际开发中可根据实际情况修改。当然也不要忘记增加对应等级的颜色。",{maxWidth:300});
        }
    });

    //控制表格编辑时文本的位置【跟随渲染时的位置】
    $("body").on("click",".layui-table-body.layui-table-main tbody tr td",function(){
        $(this).find(".layui-table-edit").addClass("layui-"+$(this).attr("align"));
    });

})