package app.takahashi.a00100.job.a00100.crawler.job.request;

import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Executors;

import org.apache.commons.dbutils.handlers.BeanListHandler;

import app.takahashi.a00100.job.a00100.crawler.job.Job;
import app.takahashi.a00100.job.a00100.crawler.job.request.crawler.Crawler;
import app.takahashi.a00100.job.a00100.crawler.job.request.crawler.WebBrowser;
import common.app.job.app.JobStatus;
import common.jdbc.JDBCParameter;
import common.jdbc.JDBCUtils;
import lombok.Data;
import lombok.val;
import lombok.experimental.Accessors;

@Accessors(prefix = "m_", chain = false)
public class Request {
	static Request m_instance;
	_Current m_current;

	Request() {
	}

	public static Request getInstance() {
		return (m_instance == null ? m_instance = new Request() : m_instance);
	}

	public static _Current getCurrent() {
		return getInstance().m_current;
	}

	public void execute() throws Exception {
		try (val browser = WebBrowser.getInstance()) {
			val executor = Executors.newFixedThreadPool(1);

			try {
				val completion = new ExecutorCompletionService<_Task>(executor);
				int taskNums = 0;

				for (val x : query()) {
					taskNums++;
					completion.submit(x);
				}

				for (int taskNum = 0; taskNum < taskNums; taskNum++) {
					(m_current = completion.take().get()).execute();
				}

				executor.shutdown();
			} catch (Exception e) {
				executor.shutdownNow();
				throw e;
			}
		} finally {
			m_instance = null;
		}
	}

	Collection<_Task> query() throws Exception {
		String sql;
		sql = "WITH s_params AS\n"
			+ "(\n"
				+ "SELECT ?::BIGINT AS job_id\n"
			+ ")\n"
			+ "SELECT j10.id,\n"
				+ "j10.catalog_id AS catalogId,\n"
				+ "j10.shopowner_id AS shopownerId\n"
			+ "FROM s_params AS t10\n"
			+ "INNER JOIN j_request AS j10\n"
				+ "ON j10.foreign_id = t10.job_id\n"
			+ "WHERE j10.deleted = FALSE\n"
			+ "AND NOT EXISTS\n"
			+ "(\n"
				+ "SELECT NULL\n"
				+ "FROM j_request_status AS j900\n"
				+ "WHERE j900.foreign_id = j10.id\n"
			+ ")\n"
			+ "ORDER BY 1\n";

		val params = new JDBCParameter() {
			{
				val job = Job.getCurrent();
				add(job.getId());
			}
		};
		val rsh = new BeanListHandler<_Task>(_Task.class);
		return JDBCUtils.query(sql, rsh, params);
	}

	@Data
	public static class _Current {
		Long m_id;
		String m_catalogId;
		String m_shopownerId;
		Status m_status;
		Crawler m_crawler;

		Status getStatus() {
			return (m_status == null ? m_status = new Status() : m_status);
		}

		Crawler getCrawler() {
			return (m_crawler == null ? m_crawler = new Crawler() : m_crawler);
		}

		public void execute() throws Exception {
			try (val status = getStatus()) {
				if (status.getStatus() == JobStatus.SUCCESS) {
					try {
					} catch (Exception e) {
						status.setStatus(JobStatus.FAILD);
						status.setMessage(e.getMessage());
					}
				}
			}
		}
	}

	public static class _Task extends _Current implements Callable<_Task> {
		@Override
		public _Task call() throws Exception {
			val status = getStatus();

			try {
				crawler();
				status.setStatus(JobStatus.SUCCESS);
			} catch (Exception e) {
				status.setStatus(JobStatus.FAILD);
				status.setMessage(e.getMessage());
			}

			return this;
		}

		void crawler() throws Exception {
			getCrawler().execute();
		}
	}
}
