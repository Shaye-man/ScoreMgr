package com.scoreMgr.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.scoreMgr.model.Clazz;
import com.scoreMgr.model.Course;
import com.scoreMgr.model.Teacher;
import com.scoreMgr.service.CourseService;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;

public class CourseController extends BaseController {

	static CourseService service = CourseService.me;
	Map<String, Object> responseM = new HashMap<String, Object>();
	
	public void list(){
		 Integer page = getParaToInt("page");
		 Integer limit = getParaToInt("limit");
		 String keywords = getPara("keywords");
		 String role = getSessionAttr("role","none");
		 String username = getSessionAttr("userID");
		 Page<Course> pageData = service.paginate(page, limit, keywords, role, username);
		 
		 setAttr("code",0);
		 setAttr("msg","成功读取课程数据！");
		 setAttr("count",pageData.getTotalRow());
		 setAttr("data",pageData.getList());
		 
		 renderJson();
	}
	
	/**
	 * 跳转至form界面
	 */
	public void form(){
		List<Clazz> clazz = Clazz.dao.find("select id,name from clazz");
		List<Teacher> teacher = Teacher.dao.find("select id,name from teacher");
		responseM.put("clazz", clazz);
		responseM.put("teacher", teacher);
		renderJson(responseM);
	}
	
	/**
	 * 提交表单信息
	 */
	public void submit(){
		Course course = new Course();
		course.setId(getParaToInt("id"));
		course.setName(getPara("name"));
		course.setPname(getPara("pname"));
		course.setCredit(getParaToInt("credit"));
		course.setClazzhour(getParaToInt("clazzhour"));
		course.setAddress(getPara("address"));
		course.setInfo(getPara("info"));
		course.setTid(getParaToInt("tName"));
		course.setClazzid(getParaToInt("clazzName"));
		
		Ret ret = service.submit(course);
		renderJson(ret);
	}
	
	/**
	 * 单条删除用户
	 */
	public void delete(){
		Integer id = getParaToInt("id");
		Ret ret = service.delById(id);
		renderJson(ret);
	}
	
	
	/**
	 * 批量删除用户
	 */
	public void batchRemove(){
		String ids = getPara("ids");
		String[] arr = ids.split(",");
		if(arr.length==0||arr == null){
			responseM.put("code", 1);
			responseM.put("msg", "至少需要选择一条需要删除的数据！");
		}else{
			String str=new String();
			for(int i=0;i<arr.length;i++){
				str+="?";
				if(i<arr.length-1){
					str+=",";
				}
			}
			Db.update("delete from course where id in ("+str+")",arr);
			responseM.put("code", 0);
			responseM.put("msg", "批量数据删除成功！");
		}
		renderJson(responseM);
	}
	
}
