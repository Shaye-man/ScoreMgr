package com.scoreMgr.service;

import java.util.Date;

import com.jfinal.kit.Ret;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;
import com.scoreMgr.model.Teacher;
import com.scoreMgr.utils.Md5Utils;

public class TeacherService {

	public static final TeacherService me =new TeacherService();
	public static final Teacher dao = new Teacher().dao();
	
	/**
	 * table分页数据查询
	 * @param page
	 * @param limit
	 * @param keywords
	 * @return
	 */
	public Page<Teacher> paginate(int page,int limit,String keywords){
		StringBuilder sb = new StringBuilder();
		if(StrKit.notBlank(keywords)){
			sb.append("and(teacher.username like '%"+keywords+"%' or teacher.name like '%"+keywords+"%')");
		}
		if(sb.length() > 0){
			sb.insert(0, "from teacher where 1=1 ");
			sb.append(" order by teacher.id desc");
		} else {
			sb.append("from teacher");
			sb.append(" order by teacher.id desc");
		}
		return dao.paginate(page, limit, "select id,username,name,role,sex,phone,adress,mailbox,introduce,createtime",sb.toString());
	}
	
	/**
	 * 创建用户信息
	 * @param teacher
	 * @param username
	 * @return ret
	 */
	private Ret create(Teacher teacher, String username) {
		teacher.setPassword(new Md5Utils().getMD5("123456"));
		teacher.setCreatetime(new Date());
		Teacher tea =dao.findFirst("select id from teacher where username='"+username+"'");
		System.out.println("hah ---  1");
		if(tea != null){
			System.out.println("hah ---  2");
			return Ret.fail();
		} else {
			teacher.save();
			return Ret.ok();
		}
	}
	
	/**
	 * 
	 * 更新用户信息
	 * @param teacher
	 * @return ret
	 */
	private Ret update(Teacher teacher) {
		boolean success = teacher.update();
		if(success){
			return Ret.ok();
		} else {
			return Ret.fail();
		}
	}
	
	/**
	 * 提交表单
	 * @param teacher
	 * 
	 * @return
	 */
	public Ret submit(Teacher teacher, String username){
		if(teacher.getId() == null || teacher.getId() <= 0){
			return create(teacher,username);
		} else {
			return update(teacher);
		}
	}
	
	/**
	 * 根据id删除单条用户信息
	 * @param id
	 * @return  ret
	 */
	public Ret delById(Integer id){
		boolean success = dao.deleteById(id);
		if(success){
			return Ret.ok();
		} else {
			return Ret.fail();
		}
	}
	
}

