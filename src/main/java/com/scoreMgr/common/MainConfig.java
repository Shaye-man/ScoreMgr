package com.scoreMgr.common;

import com.scoreMgr.handler.BasePathHandler;
import com.scoreMgr.handler.CurrentPathHandler;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.core.JFinal;
import com.jfinal.ext.handler.ContextPathHandler;
import com.jfinal.kit.PathKit;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.plugin.ehcache.EhCachePlugin;
import com.jfinal.render.ViewType;
import com.jfinal.template.Engine;
import com.scoreMgr.model._MappingKit;

public class MainConfig extends JFinalConfig {

		
  		public void configConstant(Constants me) {
			me.setViewType(ViewType.JFINAL_TEMPLATE);
			me.setDevMode(true);
			PropKit.use("config.properties");
			
		}

		public void configRoute(Routes me) {
			me.add(new AdminRoutes());
		}

		public void configEngine(Engine me) {
//			me.setBaseTemplatePath(PathKit.getWebRootPath());
			
//			//共享方法
//			me.addSharedFunction("/common/innerLayout.html");
//			me.addSharedFunction("/common/innerForm.html");
			
		}

		public void configPlugin(Plugins me) {
			// 配置DruidPlugin数据库连接池插件
			DruidPlugin druidPlugin = createDruidPlugin();
			me.add(druidPlugin);
			
			// 配置ActiveRecord插件
			ActiveRecordPlugin arp = new ActiveRecordPlugin(druidPlugin);
			arp.setBaseSqlTemplatePath(PathKit.getRootClassPath());
			arp.addSqlTemplate("scholarship.sql");
			me.add(arp);
			arp.setShowSql(true);
			_MappingKit.mapping(arp);
			
			// 配置缓存插件
//			me.add(new EhCachePlugin());
		}

		public static DruidPlugin createDruidPlugin(){
			return new DruidPlugin(PropKit.get("jdbcUrl"),PropKit.get("user"),PropKit.get("password"));
		}
		
		public void configInterceptor(Interceptors me) {
		}

		public void configHandler(Handlers me) {
			me.add(new ContextPathHandler("CONTEXT_PATH"));
			me.add(new BasePathHandler("BASE_PATH"));
			me.add(new CurrentPathHandler("CURRENT_PATH"));
		}
		
		public static void main(String[] args) {
			JFinal.start("src/main/webapp", 80, "/",5);
		}

}