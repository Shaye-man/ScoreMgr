package com.scoreMgr.service;

import java.util.Date;

import com.jfinal.kit.Ret;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;
import com.scoreMgr.model.Clazz;

public class ClazzService {

	public static final ClazzService me = new ClazzService();
	public static final Clazz dao = new Clazz().dao();
	
	/**
	 * table分页数据查询
	 * @param page
	 * @param limit
	 * @param keywords
	 * @param 
	 * @return
	 */
	public Page<Clazz> paginate(int page,int limit, String keywords){
		StringBuilder sb = new StringBuilder();
		if(StrKit.notBlank(keywords)){
			sb.append(" and (clazz.name like '%"+keywords+"%' or clazz.grade like '%"+keywords+"%' or clazz.major like '%"+keywords+"%' "
					+ "or clazz.num like '%"+keywords+"%' or clazz.academy like '%"+keywords+"%')");
		}
		if(sb.length() > 0){
			sb.insert(0, "from clazz where 1=1 ");
			sb.append(" order by clazz.id desc");
		} else {
			sb.append("from clazz");
			sb.append(" order by clazz.id desc");
		}
		return dao.paginate(page, limit, "select id,name,grade,major,num,academy,createTime",sb.toString());
	}
	
	/**
	 * 创建课程
	 * @param clazz
	 * @param name
	 * @return ret
	 */
	private Ret create(Clazz clazz, String name) {
		clazz.setCreateTime(new Date());
		Clazz cla =dao.findFirst("select id from clazz where name='"+name+"'");
		System.out.println("hah ---  1");
		if(cla != null){
			System.out.println("hah ---  2");
			return Ret.fail();
		} else {
			clazz.save();
			return Ret.ok();
		}
	}
	
	/**
	 * 
	 * 更新课程信息
	 * @param clazz
	 * @return ret
	 */
	private Ret update(Clazz clazz) {
		boolean success = clazz.update();
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
	public Ret submit(Clazz clazz, String name){
		if(clazz.getId() == null || clazz.getId() <= 0){
			return create(clazz,name);
		} else {
			return update(clazz);
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
