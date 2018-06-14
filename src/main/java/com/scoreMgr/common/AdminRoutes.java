package com.scoreMgr.common;

import com.jfinal.config.Routes;
import com.scoreMgr.controller.LoginController;
import com.scoreMgr.controller.StudentController;
import com.scoreMgr.model.Student;

public class AdminRoutes extends Routes {

	@Override
	public void config() {
		this.add("/", LoginController.class);
		this.add("/page/user",StudentController.class);
	}

}
