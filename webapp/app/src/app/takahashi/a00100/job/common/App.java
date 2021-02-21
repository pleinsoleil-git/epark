package app.takahashi.a00100.job.common;

import common.app.job.app.bean.Bean;
import common.app.job.app.model.Model;

public class App extends common.app.App {
	public App(final Bean bean, final Model model) {
		super(bean, model);
	}

	@Override
	public String getConfigFileName() {
		return null;
	}

	@Override
	public String getDataSourceName() {
		return null;
	}
}
