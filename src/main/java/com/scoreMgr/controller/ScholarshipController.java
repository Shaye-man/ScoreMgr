package com.scoreMgr.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.scoreMgr.model.Scholarship;
import com.scoreMgr.service.ScholarshipService;

public class ScholarshipController extends BaseController {

	static ScholarshipService service = ScholarshipService.me;
	Map<String, Object> responseM = new HashMap<String, Object>();

	public void list() {
		Integer page = getParaToInt("page");
		Integer limit = getParaToInt("limit");
		String keywords = getPara("keywords");
		Page<Scholarship> schPageData = service.paginate(page, limit, keywords);

		setAttr("code", 0);
		setAttr("msg", "成功读取学生数据！");
		setAttr("count", schPageData.getTotalRow());
		setAttr("data", schPageData.getList());

		renderJson();
	}

	public void countList(){
		 String countScore = Db.getSql("countScore");
		 String totalScore = Db.getSql("totalScore");
		 String rank = Db.getSql("rank");
		 
		 List<Record> countScoreList = Db.find(countScore);
		 List<Record> rankList = Db.find(rank);
		 
		 float[] num = new float[3];
		 String[] rankStr = new String[3];
		 int[] price = new int[3];
		 for (int i = 0; i < rankList.size(); i++) {
			 num[i] = rankList.get(i).get("num");
			 rankStr[i] = rankList.get(i).get("rank");
			 price[i] = rankList.get(i).get("price");
		 }
		 
		 for(int i=0;i<countScoreList.size();i++){
			 
			 int clazzId = countScoreList.get(i).get("clazzId");
			 float personAve = Float.parseFloat(countScoreList.get(i).getStr("averageScore"));
			 Record record = Db.findFirst(totalScore,clazzId);
			 float clazzAve = Float.parseFloat(record.getStr("averageScore"));
			 
			 if(personAve > clazzAve * (1 + num[2])){
				 countScoreList.get(i).set("rank", rankStr[0]);
				 countScoreList.get(i).set("price", price[0]);
			 }
			 else if(personAve > clazzAve * (1 + num[1])){
				 countScoreList.get(i).set("rank", rankStr[1]);
				 countScoreList.get(i).set("price", price[1]);
			 }
			 else if(personAve > clazzAve * (1 + num[0])){
				 countScoreList.get(i).set("rank", rankStr[2]);
				 countScoreList.get(i).set("price", price[2]);
			 }
			 else{
				 countScoreList.get(i).set("rank", "无");
				 countScoreList.get(i).set("price", "无");
			 }
		 }
				 
		 setAttr("code",0);
		 setAttr("msg","成功读取数据！");
		 setAttr("count",countScoreList.size());
		 setAttr("data",countScoreList);
		 renderJson();
	}

	/**
	 * 提交表单信息
	 */
	public void submit() {
		Scholarship scholarship = new Scholarship();
		scholarship.setId(getParaToInt("id"));
		scholarship.setRank(getPara("rank"));
		scholarship.setPrice(getParaToInt("price"));
		scholarship.setNum(Float.parseFloat(getPara("num")));
		scholarship.setInfo(getPara("info"));

		String rank = scholarship.getRank();
		Ret ret = service.submit(scholarship, rank);
		renderJson(ret);
	}

	/**
	 * 单条删除用户
	 */
	public void delete() {
		Integer id = getParaToInt("id");
		Ret ret = service.delById(id);
		renderJson(ret);
	}

	/**
	 * 批量删除用户
	 */
	public void batchRemove() {
		String ids = getPara("ids");
		String[] arr = ids.split(",");
		if (arr.length == 0 || arr == null) {
			responseM.put("code", 1);
			responseM.put("msg", "至少需要选择一条需要删除的数据！");
		} else {
			String str = new String();
			for (int i = 0; i < arr.length; i++) {
				str += "?";
				if (i < arr.length - 1) {
					str += ",";
				}
			}
			Db.update("delete from scholarship where id in (" + str + ")", arr);
			responseM.put("code", 0);
			responseM.put("msg", "批量数据删除成功！");
		}
		renderJson(responseM);
	}
}
