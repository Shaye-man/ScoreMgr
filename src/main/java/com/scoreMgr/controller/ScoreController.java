package com.scoreMgr.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.scoreMgr.model.Clazz;
import com.scoreMgr.model.Score;
import com.scoreMgr.model.Teacher;
import com.scoreMgr.service.ScoreService;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

public class ScoreController extends BaseController {

	static ScoreService service = ScoreService.me;
	Map<String, Object> responseM = new HashMap<String, Object>();
	
	public void list(){
		 Integer page = getParaToInt("page");
		 Integer limit = getParaToInt("limit");
		 String keywords = getPara("keywords");
		 String role = getSessionAttr("role","admin");
		 String sid = getSessionAttr("ID");
		 Page<Record> pageData = service.paginate(page, limit, keywords, role,Integer.parseInt(sid));
		 
		 setAttr("code",0);
		 setAttr("msg","成功读取课程数据！");
		 setAttr("count",pageData.getTotalRow());
		 setAttr("data",pageData.getList());
		 
		 renderJson();
	}
	
	public void allScoreList(){
		 Integer page = getParaToInt("page");
		 Integer limit = getParaToInt("limit");
		 String keywords = getPara("keywords");
		 String role = getSessionAttr("role","admin");
		 String sid = getSessionAttr("ID","1");
		 Page<Record> pageData = service.paginateAll(page, limit, keywords, role,Integer.parseInt(sid));
		 
		 setAttr("code",0);
		 setAttr("msg","成功读取课程数据！");
		 setAttr("count",pageData.getTotalRow());
		 setAttr("data",pageData.getList());
		 
		 renderJson();
	}
	
	public void clazzScoreList(){
		 Integer page = getParaToInt("page",1);
		 Integer limit = getParaToInt("limit",10);
		 String keywords = getPara("keywords","");
		 Page<Record> pageData = service.paginateClazz(page, limit, keywords);
		 
		 setAttr("code",0);
		 setAttr("msg","成功读取clazzScoreList数据！");
		 setAttr("count",pageData.getTotalRow());
		 setAttr("data",pageData.getList());
		 
		 renderJson();
	}
	
	public void courseScoreList(){
		 String role = getSessionAttr("role","admin");
		 String sid = getSessionAttr("username","3116001371");
		 String tid = getSessionAttr("ID","2");
		 List<Record> records = service.paginateCourse(sid, role,Integer.parseInt(tid));
		 setAttr("data", records);
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
		Score score = new Score();
		score.setSid(getPara("sId"));
		score.setCid(getParaToInt("cId"));
		score.setScore(getParaToInt("score"));
		
		Ret ret = service.submit(score);
		renderJson(ret);
	}
	
}
