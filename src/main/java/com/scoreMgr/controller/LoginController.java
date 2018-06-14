package com.scoreMgr.controller;

import java.util.HashMap;
import java.util.Map;

import com.jfinal.core.ActionKey;
import com.scoreMgr.model.Student;
import com.scoreMgr.model.Teacher;
import com.scoreMgr.utils.Md5Utils;

public class LoginController extends BaseController{
	
	public void index(){
        render("/page/login/login.html");
    }
	
	@ActionKey("/doLogin")
	public void doLogin(){
		boolean flag = false;
		String msg = "";
		Map<String, Object> responseM = new HashMap<String, Object>();
		String username = getPara("userName");
		String password = getPara("password");
		boolean result = validateCaptcha("code");
		if (!result) {
	        responseM.put("flag", false);
	        responseM.put("msg", "验证码输入错误");
	        renderJson(responseM);
	        return;
		}
	
		password = new Md5Utils().getMD5(password);
			
        Teacher teacher = Teacher.dao.findFirst(("select * from teacher where username = ? limit 1"),username);
        if(teacher != null){
        	if(teacher.getPassword().equals(password)){
        		setSessionAttr("userID", teacher.getUsername());
        		setSessionAttr("username", teacher.getName());
        		setSessionAttr("role",teacher.getRole());
        		flag = true;
        	} else {
        		msg = "密码输入错误！";
        	}
        } 
        else 
        {
        	Student student = Student.dao.findFirst(("select * from student where username = ? limit 1"),username);
        	if (student != null){
        		if(student.getPassword().equals(password)){
            		setSessionAttr("userID", student.getUsername());
            		setSessionAttr("username", student.getName());
            		setSessionAttr("role","student");
        			flag = true;
        		} else {
        			msg = "密码输入错误！";
        		}
        	} else {
        		msg = "账号不存在！";
        	}
        }
        responseM.put("flag", flag);
        responseM.put("msg", msg);
        renderJson(responseM);
	}

	@ActionKey("/createImage")
	public void createImage(){
		renderCaptcha();
	}
}
