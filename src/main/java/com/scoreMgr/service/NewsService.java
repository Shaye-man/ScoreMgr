package com.scoreMgr.service;

import java.util.Date;

import com.jfinal.kit.Ret;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;
import com.scoreMgr.model.News;

public class NewsService {

	public static final NewsService me = new NewsService();
	public static final News dao = new News().dao();
	
	/**
	 * table分页数据查询
	 * @param page
	 * @param limit
	 * @param keywords
	 * @param 
	 * @return
	 */
	public Page<News> paginate(int page,int limit, String keywords){
		StringBuilder sb = new StringBuilder();
		if(StrKit.notBlank(keywords)){
			sb.append(" and (title like '%"+keywords+"%' or author like '%"+keywords+"%' or abstract like '%"+keywords+"%' "
					+ "or content like '%"+keywords+"%')");
		}
		if(sb.length() > 0){
			sb.insert(0, "from news where 1=1 ");
			sb.append(" order by news.id desc");
		} else {
			sb.append("from news");
			sb.append(" order by news.id desc");
		}
		return dao.paginate(page, limit, "select id,title,author,abstract,content,createTime",sb.toString());
	}
	
	/**
	 * 创建公告
	 * @param news
	 * @param name
	 * @return ret
	 */
	private Ret create(News news, String title) {
		news.setCreatetime(new Date());
		News ne =dao.findFirst("select id from news where title='"+title+"'");
		System.out.println("hah ---  1");
		if(ne != null){
			System.out.println("hah ---  2");
			return Ret.fail();
		} else {
			news.save();
			return Ret.ok();
		}
	}
	
	/**
	 * 
	 * 更新课程信息
	 * @param news
	 * @return ret
	 */
	private Ret update(News news) {
		boolean success = news.update();
		if(success){
			return Ret.ok();
		} else {
			return Ret.fail();
		}
	}
	
	/**
	 * 提交表单
	 * @param news
	 * 
	 * @return
	 */
	public Ret submit(News news, String title){
		if(news.getId() == null || news.getId() <= 0){
			return create(news,title);
		} else {
			return update(news);
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
