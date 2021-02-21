package app.takahashi.app.job.app.a00100.test;

import common.app.job.app.bean.Bean;
import common.app.job.app.model.Model;

public class App extends common.app.App {
	public App() {
		super(new Bean(), new Model());
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
