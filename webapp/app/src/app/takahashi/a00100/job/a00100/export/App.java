package app.takahashi.a00100.job.a00100.export;

import common.app.job.app.bean.Bean;
import common.app.job.app.model.Model;

public class App extends app.takahashi.a00100.job.common.App {
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
