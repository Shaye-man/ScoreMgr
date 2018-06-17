package com.scoreMgr.service;

import java.util.Date;

import com.jfinal.kit.Ret;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;
import com.scoreMgr.model.Scholarship;

public class ScholarshipService {

	public static final ScholarshipService me = new ScholarshipService();
	public static final Scholarship dao = new Scholarship().dao();
	
	/**
	 * table分页数据查询
	 * @param page
	 * @param limit
	 * @param keywords
	 * @param 
	 * @return
	 */
	public Page<Scholarship> paginate(int page,int limit, String keywords){
		StringBuilder sb = new StringBuilder();
		if(StrKit.notBlank(keywords)){
			sb.append(" and (rank like '%"+keywords+"%' or price like '%"+keywords+"%' or num like '%"+keywords+"%' "
					+ "or info like '%"+keywords+"%')");
		}
		if(sb.length() > 0){
			sb.insert(0, "from scholarship where 1=1 ");
			sb.append(" order by scholarship.id desc");
		} else {
			sb.append("from scholarship");
			sb.append(" order by scholarship.id desc");
		}
		return dao.paginate(page, limit, "select id,rank,price,num,info,createTime",sb.toString());
	}
	
	/**
	 * 创建奖学金信息
	 * @param scholarship
	 * @param rank
	 * @return ret
	 */
	private Ret create(Scholarship scholarship, String rank) {
		scholarship.setCreatetime(new Date());
		Scholarship sch =dao.findFirst("select id from scholarship where rank='"+rank+"'");
		if(sch != null){
			return Ret.fail();
		} else {
			scholarship.save();
			return Ret.ok();
		}
	}
	
	/**
	 * 
	 * 更新课程信息
	 * @param scholarship
	 * @return ret
	 */
	private Ret update(Scholarship scholarship) {
		boolean success = scholarship.update();
		if(success){
			return Ret.ok();
		} else {
			return Ret.fail();
		}
	}
	
	/**
	 * 提交表单
	 * @param clazz
	 * 
	 * @return
	 */
	public Ret submit(Scholarship scholarship, String rank){
		if(scholarship.getId() == null || scholarship.getId() <= 0){
			return create(scholarship,rank);
		} else {
			return update(scholarship);
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
