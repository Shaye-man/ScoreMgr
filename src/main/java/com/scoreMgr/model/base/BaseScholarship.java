package com.scoreMgr.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseScholarship<M extends BaseScholarship<M>> extends Model<M> implements IBean {

	public M setId(java.lang.Integer id) {
		set("id", id);
		return (M)this;
	}
	
	public java.lang.Integer getId() {
		return getInt("id");
	}

	public M setRank(java.lang.String rank) {
		set("rank", rank);
		return (M)this;
	}
	
	public java.lang.String getRank() {
		return getStr("rank");
	}

	public M setPrice(java.lang.Integer price) {
		set("price", price);
		return (M)this;
	}
	
	public java.lang.Integer getPrice() {
		return getInt("price");
	}

	public M setNum(java.lang.Integer num) {
		set("num", num);
		return (M)this;
	}
	
	public java.lang.Integer getNum() {
		return getInt("num");
	}

	public M setInfo(java.lang.String info) {
		set("info", info);
		return (M)this;
	}
	
	public java.lang.String getInfo() {
		return getStr("info");
	}

	public M setCreatetime(java.util.Date createtime) {
		set("createtime", createtime);
		return (M)this;
	}
	
	public java.util.Date getCreatetime() {
		return get("createtime");
	}

}
