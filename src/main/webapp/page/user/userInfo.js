layui.use(['form','layer','upload','laydate'],function(){
    form = layui.form;
    $ = layui.jquery;
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        upload = layui.upload,
        laydate = layui.laydate;

    function getRootPath(){  
        var curPath=window.document.location.href;  
        var pathName=window.document.location.pathname;  
        var pos=curPath.indexOf(pathName);  
        var localhostPaht=curPath.substring(0,pos);  
        var projectName=pathName.substring(0,pathName.substr(1).indexOf('/')+1);  
        return localhostPaht+projectName;
    }
        
    var path = getRootPath();

    //个人信息初始化
    $.get(path+'/page/mainIndex/getUserInfo').done(function(data){
        $("#userName").val(data.username);  
        $("#role").val(data.role);  
        $("#name").val(data.name); 
        $("#sex input[value="+data.sex+"]").prop("checked","checked");
        $("#phone").val(data.phone);
        $("#address").val(data.address);    
        $("#birthday").val(data.birthday.substring(0,10));  
        $("#createTime").val(data.createtime);   
        $("#mailbox").val(data.mailbox);  
        $("#introduce").val(data.introduce);   
        form.render();    
    });


    //上传头像
    upload.render({
        elem: '.userFaceBtn',
        url: path+'/json/userface.json',
        method : "get",  //此处是为了演示之用，实际使用中请将此删除，默认用post方式提交
        done: function(res, index, upload){
            var num = parseInt(4*Math.random());  //生成0-4的随机数，随机显示一个头像信息
            $('#userFace').attr('src',res.data[num].src);
            window.sessionStorage.setItem('userFace',res.data[num].src);
        }
    });

    //添加验证规则
    form.verify({
        userBirthday : function(value){
            if(!/^(\d{4})[\u4e00-\u9fa5]|[-\/](\d{1}|0\d{1}|1[0-2])([\u4e00-\u9fa5]|[-\/](\d{1}|0\d{1}|[1-2][0-9]|3[0-1]))*$/.test(value)){
                return "出生日期格式不正确！";
            }
        }
    })
    //选择出生日期
    laydate.render({
        elem: '#birthday',
        format: 'yyyy-MM-dd',
        trigger: 'click',
        max : 0,
        mark : {"0-2-11":"生日"},
        done: function(value, date){
            if(date.month === 10 && date.date === 1){ 
                layer.msg('今天是shaye的生日，大家赶紧给他送一波祝福或者生日礼物吧！');
            }
        }
    });

    //提交个人资料
    form.on("submit(changeUser)",function(data){
        var index = layer.msg('提交中，请稍候',{icon: 16,time:false,shade:0.8});
        // 实际使用时的提交信息
        $.ajax({
          url: path+'/page/mainIndex/updateUserInfo',
          data: {
              userName: $("#userName").val(),
              sex : data.field.sex,
              phone : $("#phone").val(),
              address : $("#address").val(),
              birthday : $("#birthday").val(),
              mailBox : $("#mailBox").val(),
              introduce : $("#introduce").val(),
          },
          dataType: 'json',
          type: 'post',
          success: function(success){
              if(success){
                 layer.msg("信息修改成功！");
                 form.render();
              } else {
                  layer.msg("信息修改失败！");
              }
          },
          error: function(){
            layer.msg("信息修改失败！");
            }
        });
        return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
    });
    

})