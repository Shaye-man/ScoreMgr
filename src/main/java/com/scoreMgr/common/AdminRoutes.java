package com.scoreMgr.common;

import com.jfinal.config.Routes;
import com.scoreMgr.controller.CourseController;
import com.scoreMgr.controller.LoginController;
import com.scoreMgr.controller.StudentController;

public class AdminRoutes extends Routes {

	@Override
	public void config() {
		this.add("/", LoginController.class);
		this.add("/page/user",StudentController.class);
		this.add("/page/course",CourseController.class);
	}

}
