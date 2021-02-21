package common.app.job.app.action;

import java.sql.SQLException;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import common.app.App;
import common.app.job.app.model.Model;
import lombok.Getter;
import lombok.val;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Accessors(prefix = "m_", chain = true)
public class Action implements org.quartz.Job {
	@Getter
	final App m_app;

	public Action(final App app) {
		m_app = app;
	}

	@Override
	public void execute(final JobExecutionContext context) throws JobExecutionException {
		new Thread() {
			@Override
			public void run() {
				// インスタンス設定
				App.setCurrent(getApp());

				execute();
			}
		}.start();
	}

	public void execute() {
		log.debug("Action execute");

		try (val app = App.getCurrent()) {
			Model.getCurrent().execute();
		} catch (Exception e) {
			print(e);
		}
	}

	void print(final Exception e) {
		if (e instanceof SQLException) {
			print((SQLException) e);
		} else {
			log.error("", e);
		}
	}

	void print(final SQLException e) {
		for (SQLException x = e; x != null;) {
			log.error("", x);
			x = x.getNextException();
		}
	}
}
