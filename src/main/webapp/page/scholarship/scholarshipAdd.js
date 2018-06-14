layui.use(['form','layer','laydate','upload'],function(){
    var form = layui.form
        layer = parent.layer === undefined ? layui.layer : top.layer,
        laypage = layui.laypage,
        $ = layui.jquery;

    form.on("submit(addNews)",function(data){
        //弹出loading
        var index = top.layer.msg('数据提交中，请稍候',{icon: 16,time:false,shade:0.8});
        // 实际使用时的提交信息
        // $.post("上传路径",{
        //     newsName : $(".newsName").val(),  //文章标题
        //     abstract : $(".abstract").val(),  //文章摘要
        //     content : layedit.getContent(editIndex).split('<audio controls="controls" style="display: none;"></audio>')[0],  //文章内容
        //     newsImg : $(".thumbImg").attr("src"),  //缩略图
        //     classify : '1',    //文章分类
        //     newsStatus : $('.newsStatus select').val(),    //发布状态
        //     newsTime : submitTime,    //发布时间
        //     newsTop : data.filed.newsTop == "on" ? "checked" : "",    //是否置顶
        // },function(res){
        //
        // })
        setTimeout(function(){
            top.layer.close(index);
            top.layer.msg("文章添加成功！");
            layer.closeAll("iframe");
            //刷新父页面
            parent.location.reload();
        },500);
        return false;
    });

    form.on("select(change)",function(data){
        var grade = $(".grade").val();
        var academy = $(".academy").val();
        var major = $(".major").val();
        var num = $(".num").val();
        var text = grade+"-"+academy+"-"+major+"-"+num+"-"+"班";
        document.getElementById("name").value = text;        
    });

    $(".major").on('input propertychange',function(){
        var grade = $(".grade").val();
        var academy = $(".academy").val();
        var major = $(".major").val();
        var num = $(".num").val();
        var text = grade+"-"+academy+"-"+major+"-"+num+"-"+"班";
        document.getElementById("name").value = text; 
    });

})