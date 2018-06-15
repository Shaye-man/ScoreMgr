package com.scoreMgr.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.scoreMgr.model.Clazz;
import com.scoreMgr.model.Student;
import com.scoreMgr.service.StudentService;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;

public class StudentController extends BaseController {

	static StudentService service = StudentService.me;
	Map<String, Object> responseM = new HashMap<String, Object>();
	
	public void list(){
		 Integer page = getParaToInt("page");
		 Integer limit = getParaToInt("limit");
		 String keywords = getPara("keywords");
		 Page<Student> stuPageData = service.paginate(page, limit, keywords);
		 
		 setAttr("code",0);
		 setAttr("msg","成功读取学生数据！");
		 setAttr("count",stuPageData.getTotalRow());
		 setAttr("data",stuPageData.getList());
		 
		 renderJson();
	}
	
	/**
	 * 跳转至form界面
	 */
	public void form(){
		List<Clazz> data = Clazz.dao.find("select * from clazz");
		renderJson(data);
	}
	
	/**
	 * 提交表单信息
	 */
	public void submit(){
		Student student = new Student();
		student.setId(getParaToInt("id"));
		student.setUsername(getPara("username"));
		student.setName(getPara("name"));
		student.setMailbox(getPara("mailbox"));
		student.setSex(getPara("sex"));
		student.setClazzid(getParaToInt("userClazz"));
		student.setIntroduce(getPara("introduce"));
		
		String username = student.getUsername();
		Ret ret = service.submit(student,username);
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
//		Integer ids[]=getParaValuesToInt("ids");
//		System.out.println(ids);
		if(arr.length==0||arr == null){
			responseM.put("code", 1);
			responseM.put("msg", "至少需要选择一条需要删除的数据！");
		}else{
			//第一种
			/*Object[][] paras=new Object[ids.length][1];
			for(int i=0;i<ids.length;i++){
				paras[i][0]=ids[i];
			}
			Db.batch("delete from user where id=?",paras,ids.length);*/
			//第二种
//			for(int i=0;i<ids.length;i++){
//				User.dao.deleteById(ids[i]);
//				//Db.update("delete from user where id=?", ids[i]);
//			}
/*			//第三种
			String str=new String();
			for(int i=0;i<ids.length;i++){
				str+=ids[i];
				if(i<ids.length-1){
					str+=",";
				}
			}
			Db.update("delete from user where id in ("+str+")");
*/			//第四种
			String str=new String();
			for(int i=0;i<arr.length;i++){
				str+="?";
				if(i<arr.length-1){
					str+=",";
				}
			}
			Db.update("delete from student where id in ("+str+")",arr);
			responseM.put("code", 0);
			responseM.put("msg", "批量数据删除成功！");
		}
		renderJson(responseM);
	}
	
}
