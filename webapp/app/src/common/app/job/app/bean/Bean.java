package common.app.job.app.bean;

import common.app.App;

public class Bean implements common.app.bean.Bean {
	public static Bean getCurrent() {
		return (Bean) App.getCurrent().getBean();
	}
}
