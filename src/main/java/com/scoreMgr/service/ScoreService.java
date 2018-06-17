package com.scoreMgr.service;

import java.util.Date;

import com.scoreMgr.model.Clazz;
import com.scoreMgr.model.Score;

import com.jfinal.kit.Ret;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

public class ScoreService {
	
	public static final ScoreService me = new ScoreService();
	public static final Score dao = new Score().dao();

	/**
	 * 成绩录入分页
	 * @param page
	 * @param limit
	 * @param keywords
	 * @param role
	 * @param id
	 * @return
	 */
	public Page<Record> paginate(int page,int limit, String keywords,String role,int id){
		StringBuilder sb = new StringBuilder();
		if(StrKit.notBlank(keywords)){
			sb.append(" and (student.username like '%"+keywords+"%' or student.name like '%"+keywords+"%' or clazz.name like '%"+keywords+"%')");
		}
		if(role == "teacher" || role.equals("teacher")){
			if(sb.length() > 0){
				sb.insert(0, "FROM "
						+ "("
						+ "SELECT student.username AS sId,student.`name` AS sName,"
						+ "course.id AS cId,course.`name` AS cName,"
						+ "clazz.`name` AS clazzName,"
						+ "teacher.`name` AS tName "
						+ "FROM "
						+ "course,student,clazz,teacher "
						+ "WHERE "
						+ "student.clazzid = course.clazzid AND student.clazzid = clazz.id AND course.tid = teacher.id AND teacher.id = "+id);
			} else {
				sb.append("FROM "
						+ "("
						+ "SELECT student.username AS sId,student.`name` AS sName,"
						+ "course.id AS cId,course.`name` AS cName,"
						+ "clazz.`name` AS clazzName,"
						+ "teacher.`name` AS tName "
						+ "FROM "
						+ "course,student,clazz,teacher "
						+ "WHERE "
						+ "student.clazzid = course.clazzid AND student.clazzid = clazz.id AND course.tid = teacher.id AND teacher.id = "+id);
			}
		}
		else if(role == "admin" || role.equals("admin")){
			if(sb.length() > 0){
				sb.insert(0, "FROM "
						+ "("
						+ "SELECT student.username AS sId,student.`name` AS sName,"
						+ "course.id AS cId,course.`name` AS cName,"
						+ "clazz.`name` AS clazzName,"
						+ "teacher.`name` AS tName "
						+ "FROM "
						+ "course,student,clazz,teacher "
						+ "WHERE "
						+ "student.clazzid = course.clazzid AND student.clazzid = clazz.id AND course.tid = teacher.id");
			} else {
				sb.append("FROM "
						+ "("
						+ "SELECT student.username AS sId,student.`name` AS sName,"
						+ "course.id AS cId,course.`name` AS cName,"
						+ "clazz.`name` AS clazzName,"
						+ "teacher.`name` AS tName "
						+ "FROM "
						+ "course,student,clazz,teacher "
						+ "WHERE "
						+ "student.clazzid = course.clazzid AND student.clazzid = clazz.id AND course.tid = teacher.id");
			}
		}
		else{
			System.err.println("无法获取roleSession...请用户重新登录！");
			return null;
		}

		//公有字段
		sb.append(") "
				+ "AS info "
				+ "LEFT JOIN score "
				+ "ON "
				+ "info.sId = score.sid AND info.cId = score.cid");
		
		return Db.paginate(page, limit, 
				"SELECT info.sId,info.sName,info.cId,info.cName,info.clazzName,info.tName,"
				+ "score.score,score.createtime as scoreTime",sb.toString());
	}

	/**
	 * 所有成绩分页查询
	 * @param page
	 * @param limit
	 * @param keywords
	 * @param role
	 * @param id
	 * @return
	 */
	public Page<Record> paginateAll(int page,int limit, String keywords,String role,int id){
		StringBuilder sb = new StringBuilder();
		if(StrKit.notBlank(keywords)){
			sb.append(" and (student.username like '%"+keywords+"%' or student.name like '%"+keywords+"%' or clazz.name like '%"+keywords+"%')");
		}
		if(role == "student" || role.equals("student")){
			if(sb.length() > 0){
				sb.insert(0, "FROM "
						+ "course,student,clazz,score "
						+ "WHERE "
						+ "student.clazzid = course.clazzid "
						+ "AND student.clazzid = clazz.id "
						+ "AND score.sid = student.username "
						+ "AND score.cid = course.id "
						+ "AND student.id = "+id);
			} else {
				sb.append("FROM "
						+ "course,student,clazz,score "
						+ "WHERE "
						+ "student.clazzid = course.clazzid "
						+ "AND student.clazzid = clazz.id "
						+ "AND score.sid = student.username "
						+ "AND score.cid = course.id "
						+ "AND student.id = "+id);
			}
		}
		else if(role == "teacher" || role.equals("teacher")){
			if(sb.length() > 0){
				sb.insert(0, "FROM "
						+ "course,student,clazz,score "
						+ "WHERE "
						+ "student.clazzid = course.clazzid "
						+ "AND student.clazzid = clazz.id "
						+ "AND score.sid = student.username "
						+ "AND score.cid = course.id "
						+ "AND course.tid = "+id);
			} else {
				sb.append("FROM "
						+ "course,student,clazz,score "
						+ "WHERE "
						+ "student.clazzid = course.clazzid "
						+ "AND student.clazzid = clazz.id "
						+ "AND score.sid = student.username "
						+ "AND score.cid = course.id "
						+ "AND course.tid = "+id);
			}
		}
		else if(role == "admin" || role.equals("admin")){
			if(sb.length() > 0){
				sb.insert(0, "FROM "
						+ "course,student,clazz,score "
						+ "WHERE "
						+ "student.clazzid = course.clazzid "
						+ "AND student.clazzid = clazz.id "
						+ "AND score.sid = student.username "
						+ "AND score.cid = course.id");
			} else {
				sb.append("FROM "
						+ "course,student,clazz,score "
						+ "WHERE "
						+ "student.clazzid = course.clazzid "
						+ "AND student.clazzid = clazz.id "
						+ "AND score.sid = student.username "
						+ "AND score.cid = course.id");
			}
		}
		else{
			System.err.println("无法获取roleSession...请用户重新登录！");
			return null;
		}
		
		return Db.paginate(page, limit, 
				"SELECT student.username AS sId,"
				+ "student.`name` AS sName,course.`name` AS cName,clazz.`name` AS clazzName,"
				+ "score.score",sb.toString());
	}
	
	/**
	 * 班级总成绩查询分页
	 * @param page
	 * @param limit
	 * @param keywords
	 * @return
	 */
	public Page<Record> paginateClazz(int page,int limit, String keywords){
		StringBuilder sb = new StringBuilder();
		if(StrKit.notBlank(keywords)){
			sb.append(" AND (clazz.name like '%"+keywords+"%')");
		}
		if(sb.length() > 0){
			sb.insert(0, "FROM course,score,clazz "
					+ "WHERE score.cid = course.id AND course.clazzid = clazz.id");
			sb.append(" GROUP BY clazz.`name`");
			sb.append(" ORDER BY averageScore DESC");
		} else {
			sb.append("FROM course,score,clazz "
					+ "WHERE score.cid = course.id AND course.clazzid = clazz.id");
			sb.append(" GROUP BY clazz.`name`");
			sb.append(" ORDER BY averageScore DESC");
		}
		return Db.paginate(page, limit, "SELECT "
				+ "clazz.`name` AS clazzName,SUM(score) AS totalScore,"
				+ "FORMAT(AVG(score), 2) AS averageScore,MIN(score) AS minScore,"
				+ "MAX(score) AS highScore",sb.toString());
	}
	
	/**
	 * 成绩录入提交
	 * @param score
	 * @return
	 */
	public Ret submit(Score score){
		Score success = dao.findFirst("select * from score where sid = ? and cid = ?", score.getSid(),score.getCid());
		if(success == null){
			return create(score);
		} else {
			return update(score);
		}
	}
	
	/**
	 * 成绩录入更新
	 * @param score
	 * @return
	 */
	private Ret update(Score score) {
		boolean success = score.update();
		score.setCreatetime(new Date());
		if(success){
			return Ret.ok();
		} else {
			return Ret.fail();
		}
	}

	/**
	 * 成绩信息插入
	 * @param score
	 * @return
	 */
	private Ret create(Score score) {
		score.setCreatetime(new Date());
		score.save();
		return Ret.ok();
	}
	
}

