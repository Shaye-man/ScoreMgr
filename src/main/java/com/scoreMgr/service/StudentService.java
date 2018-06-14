package com.scoreMgr.service;

import java.util.Date;

import com.scoreMgr.model.Student;
import com.scoreMgr.utils.Md5Utils;
import com.jfinal.kit.Ret;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;

public class StudentService {
	
	public static final StudentService me = new StudentService();
	public static final Student dao = new Student().dao();
	
	/**
	 * table分页数据查询
	 * @param page
	 * @param limit
	 * @param keywords
	 * @param role
	 * @return
	 */
	public Page<Student> paginate(int page,int limit, String keywords){
		StringBuilder sb = new StringBuilder();
		if(StrKit.notBlank(keywords)){
			sb.append(" and (username like '%"+keywords+"%' or name like '%"+keywords+"%' or clazzName like '%"+keywords+"%')");
		}
		if(sb.length() > 0){
			sb.insert(0, "from clazz,student");
			sb.append(" where clazz.id = student.clazzid");
			sb.append(" order by student.id desc");
		} else {
			sb.append("from clazz,student");
			sb.append(" where clazz.id = student.clazzid");
			sb.append(" order by student.id desc");
		}
		return dao.paginate(page, limit, "select student.id,student.username,student.name,clazz.name as clazzName,student.sex,student.phone,student.mailbox,student.introduce,student.createTime",sb.toString());
	}

	/**
	 * 提交表单
	 * @param student
	 * @param stuId 
	 * @return
	 */
	public Ret submit(Student student, String username){
		if(student.getId() == null || student.getId() <= 0){
			return create(student,username);
		} else {
			return update(student);
		}
	}
	
	/**
	 * 
	 * 更新用户信息
	 * @param student
	 * @return ret
	 */
	private Ret update(Student student) {
		boolean success = student.update();
		if(success){
			return Ret.ok();
		} else {
			return Ret.fail();
		}
	}

	/**
	 * 创建用户信息
	 * @param student
	 * @return ret
	 */
	private Ret create(Student student, String username) {
		student.setPassword(new Md5Utils().getMD5("123456"));
		student.setCreatetime(new Date());
		Student stu =dao.findFirst("select id from student where username='"+username+"'");
		System.out.println("hah ---  1");
		if(stu != null){
			System.out.println("hah ---  2");
			return Ret.fail();
		} else {
			student.save();
			return Ret.ok();
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

