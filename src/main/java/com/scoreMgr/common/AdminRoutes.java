package com.scoreMgr.common;

import com.jfinal.config.Routes;
import com.scoreMgr.controller.ClazzController;
import com.scoreMgr.controller.CourseController;
import com.scoreMgr.controller.LoginController;
import com.scoreMgr.controller.MainIndexController;
import com.scoreMgr.controller.NewsController;
import com.scoreMgr.controller.ScholarshipController;
import com.scoreMgr.controller.ScoreController;
import com.scoreMgr.controller.StudentController;
import com.scoreMgr.controller.TeacherController;

public class AdminRoutes extends Routes {

	@Override
	public void config() {
		this.add("/", LoginController.class);
		this.add("/page/user",StudentController.class);
		this.add("/page/user/teacher",TeacherController.class);
		this.add("/page/course",CourseController.class);
		this.add("/page/news",NewsController.class);
		this.add("/page/clazz",ClazzController.class);
		this.add("/page/scholarship",ScholarshipController.class);
		this.add("/page/score",ScoreController.class);
		this.add("/page/mainIndex",MainIndexController.class);
	}
}
