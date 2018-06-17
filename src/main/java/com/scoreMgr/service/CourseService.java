package com.scoreMgr.service;

import java.util.Date;

import com.scoreMgr.model.Course;
import com.scoreMgr.model.Student;

import com.jfinal.kit.Ret;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;

public class CourseService {
	
	public static final CourseService me = new CourseService();
	public static final Course dao = new Course().dao();

	public Page<Course> paginate(int page,int limit, String keywords,String role,int id){
		StringBuilder sb = new StringBuilder();
		if(StrKit.notBlank(keywords)){
			sb.append(" and (course.name like '%"+keywords+"%' or teacher.name like '%"+keywords+"%' or clazz.name like '%"+keywords+"%')");
		}
		if(role == "student" || role.equals("student")){
			Student stu = Student.dao.findFirst("select clazzid from student where id = ?",id);
			int clazzID = stu.getClazzid();
			if(sb.length() > 0){
				sb.insert(0, "from clazz,course,teacher "
						+ "where clazz.id = course.clazzid and teacher.username = course.tid and clazz.id = "+clazzID);
				sb.append(" order by course.id desc");
			} else {
				sb.append("from clazz,course,teacher");
				sb.append(" where clazz.id = course.clazzid and teacher.id = course.tid and clazz.id = "+clazzID);
				sb.append(" order by course.id desc");
			}
		}
		else if(role == "teacher" || role.equals("teacher")){
			if(sb.length() > 0){
				sb.insert(0, "from clazz,course,teacher where clazz.id = course.clazzid and teacher.id = course.tid and teacher.id = "+id);
				sb.append(" order by course.id desc");
			} else {
				sb.append("from clazz,course,teacher");
				sb.append(" where clazz.id = course.clazzid and teacher.id = course.tid and teacher.id = "+id);
				sb.append(" order by course.id desc");
			}
		}
		else if(role == "admin" || role.equals("admin")){
			if(sb.length() > 0){
				sb.insert(0, "from clazz,course,teacher where clazz.id = course.clazzid and teacher.id = course.tid");
				sb.append(" order by course.id desc");
			} else {
				sb.append("from clazz,course,teacher");
				sb.append(" where clazz.id = course.clazzid and teacher.id = course.tid");
				sb.append(" order by course.id desc");
			}
		}
		else{
			System.err.println("无法获取roleSession...");
			return null;
		}

		return dao.paginate(page, limit, 
				"select course.id,course.name,course.pname,clazz.name as clazzName,"
				+ "course.credit,course.clazzhour,course.address,course.info,course.createTime,"
				+ "teacher.name as tName",sb.toString());
	}

	/**
	 * 提交表单
	 * @param course
	 * @param stuId 
	 * @return
	 */
	public Ret submit(Course course){
		if(course.getId() == null || course.getId() <= 0){
			return create(course);
		} else {
			return update(course);
		}
	}
	
	/**
	 * 
	 * 更新用户信息
	 * @param course
	 * @return ret
	 */
	private Ret update(Course course) {
		boolean success = course.update();
		if(success){
			return Ret.ok();
		} else {
			return Ret.fail();
		}
	}

	/**
	 * 创建用户信息
	 * @param course
	 * @return ret
	 */
	private Ret create(Course course) {
		course.setCreatetime(new Date());
		course.save();
		return Ret.ok();
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

