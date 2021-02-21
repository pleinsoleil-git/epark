package app.takahashi.a00100.job.common;

import common.lang.StringUtils;
import lombok.val;

public class App extends app.takahashi.a00100.App {
	public static final String DEFAULT_DSN;
	public static final String CONFIGFILE_NAME;

	static {
		val names = StringUtils.split(App.class.getPackage().getName(), ".");
		DEFAULT_DSN = String.format("jdbc/%s/%s/default", names[1], names[2]);
		CONFIGFILE_NAME = String.format("config-%s-%s-job.xml", names[1], names[2]);
	}

	public App(final Bean bean, final Model model) {
		super(bean, model);
	}

	@Override
	public String getConfigFileName() {
		return CONFIGFILE_NAME;
	}

	@Override
	public String getDataSourceName() {
		return DEFAULT_DSN;
	}
}
