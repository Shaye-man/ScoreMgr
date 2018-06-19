package com.scoreMgr.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseStudent<M extends BaseStudent<M>> extends Model<M> implements IBean {

	public M setId(java.lang.Integer id) {
		set("id", id);
		return (M)this;
	}
	
	public java.lang.Integer getId() {
		return getInt("id");
	}

	public M setUsername(java.lang.String username) {
		set("username", username);
		return (M)this;
	}
	
	public java.lang.String getUsername() {
		return getStr("username");
	}

	public M setPassword(java.lang.String password) {
		set("password", password);
		return (M)this;
	}
	
	public java.lang.String getPassword() {
		return getStr("password");
	}

	public M setName(java.lang.String name) {
		set("name", name);
		return (M)this;
	}
	
	public java.lang.String getName() {
		return getStr("name");
	}

	public M setClazzid(java.lang.Integer clazzid) {
		set("clazzid", clazzid);
		return (M)this;
	}
	
	public java.lang.Integer getClazzid() {
		return getInt("clazzid");
	}

	public M setSex(java.lang.String sex) {
		set("sex", sex);
		return (M)this;
	}
	
	public java.lang.String getSex() {
		return getStr("sex");
	}

	public M setBirthday(java.util.Date birthday) {
		set("birthday", birthday);
		return (M)this;
	}
	
	public java.util.Date getBirthday() {
		return get("birthday");
	}

	public M setPhone(java.lang.String phone) {
		set("phone", phone);
		return (M)this;
	}
	
	public java.lang.String getPhone() {
		return getStr("phone");
	}

	public M setAddress(java.lang.String address) {
		set("address", address);
		return (M)this;
	}
	
	public java.lang.String getAddress() {
		return getStr("address");
	}

	public M setIcon(java.lang.String icon) {
		set("icon", icon);
		return (M)this;
	}
	
	public java.lang.String getIcon() {
		return getStr("icon");
	}

	public M setMailbox(java.lang.String mailbox) {
		set("mailbox", mailbox);
		return (M)this;
	}
	
	public java.lang.String getMailbox() {
		return getStr("mailbox");
	}

	public M setIntroduce(java.lang.String introduce) {
		set("introduce", introduce);
		return (M)this;
	}
	
	public java.lang.String getIntroduce() {
		return getStr("introduce");
	}

	public M setCreatetime(java.util.Date createtime) {
		set("createtime", createtime);
		return (M)this;
	}
	
	public java.util.Date getCreatetime() {
		return get("createtime");
	}

}
