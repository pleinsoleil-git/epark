package app.takahashi.a00100.job.a00100.crawler.job.request;

import java.util.ArrayList;
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
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Accessors(prefix = "m_", chain = false)
public class Request {
	static Request m_instance;
	static ThreadLocal<_Current> m_currents = new ThreadLocal<_Current>() {
	};

	Request() {
	}

	public static Request getInstance() {
		return (m_instance == null ? m_instance = new Request() : m_instance);
	}

	public static _Current getCurrent() {
		return m_currents.get();
	}

	public void execute() throws Exception {
		try (val browser = WebBrowser.getInstance()) {
			val job = Job.getCurrent();
			val executor = Executors.newFixedThreadPool(job.getThreadNums());

			try {
				val completion = new ExecutorCompletionService<_Task>(executor);
				int taskNums = 0;

				for (val x : query()) {
					taskNums++;
					completion.submit(x);
				}

				for (int taskNum = 0; taskNum < taskNums; taskNum++) {
					m_currents.set(completion.take().get());
					m_currents.get().execute();
				}

				executor.shutdown();
			} catch (Exception e) {
				executor.shutdownNow();
				throw e;
			}
		} finally {
			m_currents.remove();
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
		Collection<Result> m_results;
		Status m_status;

		public Collection<Result> getResults() {
			return (m_results == null ? m_results = new ArrayList<>() : m_results);
		}

		public Status getStatus() {
			return (m_status == null ? m_status = new Status() : m_status);
		}

		public void execute() throws Exception {
			try (val status = getStatus()) {
				try {
					for (val result : getResults()) {
						try (val x = result) {
						}
					}
				} catch (Exception e) {
					status.setStatus(JobStatus.FAILD);
					status.setMessage(e.getMessage());
					log.error("", e);
				} finally {
					JDBCUtils.commit();
				}
			}
		}
	}

	public static class _Task extends _Current implements Callable<_Task> {
		@Override
		public _Task call() throws Exception {
			try {
				m_currents.set(this);

				val status = getStatus();

				try {
					crawler();
					status.setStatus(JobStatus.SUCCESS);
				} catch (Exception e) {
					status.setStatus(JobStatus.FAILD);
					status.setMessage(e.getMessage());
					log.error("", e);
				}
			} finally {
				m_currents.remove();
			}

			return this;
		}

		void crawler() throws Exception {
			Crawler.getInstance().execute();
		}
	}
}
