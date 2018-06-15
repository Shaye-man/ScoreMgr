package com.scoreMgr.controller;

import java.util.HashMap;
import java.util.Map;

import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.scoreMgr.model.News;
import com.scoreMgr.service.NewsService;

public class NewsController extends BaseController{

	static NewsService service = NewsService.me;
	Map<String,Object> responseM = new HashMap<String,Object>();
	
	public void list(){
		 Integer page = getParaToInt("page");
		 Integer limit = getParaToInt("limit");
		 String keywords = getPara("keywords");
		 Page<News> nePageData = service.paginate(page, limit, keywords);
		 
		 setAttr("code",0);
		 setAttr("msg","成功读取学生数据！");
		 setAttr("count",nePageData.getTotalRow());
		 setAttr("data",nePageData.getList());
		 
		 renderJson();
	}
	
	/**
	 * 提交表单信息
	 */
	public void submit(){
		News news = new News();
		news.setId(getParaToInt("id"));
		news.setTitle(getPara("title"));
		news.setAbstract(getPara("abstract"));
		news.setContent(getPara("content"));
		String userName = getSessionAttr("username", "管理员");
		news.setAuthor(userName);

		String title = news.getTitle();
		Ret ret = service.submit(news,title);
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
			Db.update("delete from news where id in ("+str+")",arr);
			responseM.put("code", 0);
			responseM.put("msg", "批量数据删除成功！");
		}
		renderJson(responseM);
	}
}
