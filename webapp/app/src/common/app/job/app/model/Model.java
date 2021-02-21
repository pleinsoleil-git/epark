package common.app.job.app.model;

import common.app.App;

public class Model implements common.app.model.Model {
	public static Model getCurrent() {
		return (Model) App.getCurrent().getModel();
	}

	public void execute() throws Exception {
	}
}
