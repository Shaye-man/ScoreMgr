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
		int USER_ID = 0;
		String NAME = "";
		String ROLE = "";
		
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
        		setSessionAttr("ID", teacher.getId().toString());
        		setSessionAttr("username", teacher.getUsername());
        		setSessionAttr("name", teacher.getName());
        		setSessionAttr("role",teacher.getRole());
        		USER_ID = teacher.getId();
        		NAME = teacher.getName();
        		ROLE = teacher.getRole();
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
        			setSessionAttr("ID", student.getId().toString());
            		setSessionAttr("username", student.getUsername());
            		setSessionAttr("name", student.getName());
            		setSessionAttr("role","student");
            		USER_ID = student.getId();
            		NAME = student.getName();
            		ROLE = "student";
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
        responseM.put("USER_ID", USER_ID);
        responseM.put("NAME", NAME);
        responseM.put("ROLE", ROLE);
        renderJson(responseM);
	}

	@ActionKey("/createImage")
	public void createImage(){
		renderCaptcha();
	}
}
