package com.scoreMgr.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jetty.util.ajax.JSON;

import com.jfinal.json.FastJson;
import com.jfinal.json.FastJsonFactory;
import com.jfinal.json.Json;
import com.jfinal.kit.Ret;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.scoreMgr.model.Student;
import com.scoreMgr.model.Teacher;
import com.scoreMgr.utils.Md5Utils;

public class MainIndexController extends BaseController {

	Map<String, Object> responseM = new HashMap<String, Object>();

	/**
	 * 密码修改
	 */
	public void changePwd(){
		boolean flag = false;
		String msg = "";
		
		int id = getParaToInt("id");
		String role = getSessionAttr("role");
		String oldPwd = getPara("oldPwd");
		String newPwd = getPara("newPwd");
		String confirmPwd = getPara("confirmPwd");
		
		if(StrKit.notBlank(oldPwd)){
			if(StrKit.notBlank(newPwd)){
				if(newPwd.equals(confirmPwd) || newPwd == confirmPwd){
					oldPwd = new Md5Utils().getMD5(oldPwd);
					if(role.equals("student") || role == "student"){
						Student student = Student.dao.findById(id);
						if(oldPwd.equals(student.getPassword())){
							int success = Db.update("UPDATE student SET password = ? WHERE id = "+id,new Md5Utils().getMD5(newPwd));
							if(success != 0){
								msg = "密码修改成功！";
								responseM.put("flag", flag);
								responseM.put("msg", msg);
								renderJson(responseM);
								return;
							}else{
								msg = "密码修改失败！";
								responseM.put("flag", flag);
								responseM.put("msg", msg);
								renderJson(responseM);
								return;
							}
						}else{
							msg = "旧密码输入错误！";
							responseM.put("flag", flag);
							responseM.put("msg", msg);
							renderJson(responseM);
							return;
						}
					}else{
						Teacher teacher = Teacher.dao.findById(id);
						if(oldPwd.equals(teacher.getPassword())){
							int success = Db.update("UPDATE teacher SET password = ? WHERE id = "+id,new Md5Utils().getMD5(newPwd));
							if(success != 0){
								msg = "密码修改成功！";
								responseM.put("flag", flag);
								responseM.put("msg", msg);
								renderJson(responseM);
								return;
							}else{
								msg = "密码修改失败！";
								responseM.put("flag", flag);
								responseM.put("msg", msg);
								renderJson(responseM);
								return;
							}
						}else{
							msg = "旧密码输入错误！";
							responseM.put("flag", flag);
							responseM.put("msg", msg);
							renderJson(responseM);
							return;
						}
					}
				}else{
					msg = "确认密码输入不一致";
					responseM.put("flag", flag);
					responseM.put("msg", msg);
					renderJson(responseM);
					return;
				}
			}else{
				msg = "新密码输入为空";
				responseM.put("flag", flag);
				responseM.put("msg", msg);
				renderJson(responseM);
				return;
			}
		}else{
			msg = "密码输入为空";
			responseM.put("flag", flag);
			responseM.put("msg", msg);
			renderJson(responseM);
			return;
		}
	}

	/**
	 * 超简易版菜单权限处理
	 * 有时间的话，打死也别这么做
	 */
	public void navCtrl(){
		String role = getSessionAttr("role", "admin");
		String path = "";
		if(role.equals("student")){
			path = MainIndexController.class.getClassLoader().getResource("navs-1.json").getPath();
		}
		else if(role.equals("teacher")){
			path = MainIndexController.class.getClassLoader().getResource("navs-2.json").getPath();
		}
		else if(role.equals("admin")){
			path = MainIndexController.class.getClassLoader().getResource("navs-3.json").getPath();
		}
		else{
			System.err.println("无法获取roleSession...菜单无法初始化");
			return;
		}

		String json = "";
		String line = "";
        try {
    		BufferedReader br = new BufferedReader(new FileReader(path));
    		while ((line = br.readLine()) != null) {
				json += line;
			}
    		br.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("无法加载nav文件...");
        }
        renderJson(json);
	}
}
