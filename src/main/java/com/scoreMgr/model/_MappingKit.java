package com.scoreMgr.model;

import com.jfinal.plugin.activerecord.ActiveRecordPlugin;

/**
 * Generated by JFinal, do not modify this file.
 * <pre>
 * Example:
 * public void configPlugin(Plugins me) {
 *     ActiveRecordPlugin arp = new ActiveRecordPlugin(...);
 *     _MappingKit.mapping(arp);
 *     me.add(arp);
 * }
 * </pre>
 */
public class _MappingKit {
	
	public static void mapping(ActiveRecordPlugin arp) {
		arp.addMapping("clazz", "id", Clazz.class);
		arp.addMapping("course", "id", Course.class);
		arp.addMapping("news", "id", News.class);
		arp.addMapping("scholarship", "id", Scholarship.class);
		// Composite Primary Key order: cid,sid
		arp.addMapping("score", "cid,sid", Score.class);
		arp.addMapping("student", "id", Student.class);
		arp.addMapping("teacher", "id", Teacher.class);
	}
}

