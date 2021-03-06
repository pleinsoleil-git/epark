package app.nakajo.a00100.job.a00100.export.job;

import java.util.Collection;

import org.apache.commons.dbutils.handlers.BeanListHandler;

import app.nakajo.a00100.job.a00100.export.job.request.Request;
import common.app.job.app.JobStatus;
import common.jdbc.JDBCUtils;
import lombok.Data;
import lombok.val;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Accessors(prefix = "m_", chain = false)
public class Job {
	static Job m_instance;
	_Current m_current;

	Job() {
	}

	public static Job getInstance() {
		return (m_instance == null ? m_instance = new Job() : m_instance);
	}

	public static _Current getCurrent() {
		return getInstance().m_current;
	}

	public void execute() throws Exception {
		try {
			// delete();

			for (val x : query()) {
				(m_current = x).execute();
			}
		} finally {
			m_instance = null;
		}
	}

	void delete() throws Exception {
		for (val x : new String[] {
				"j_export_job_status",
				"j_export_request_status",
		}) {
			log.info(String.format("Delete %s", x));
			JDBCUtils.execute(String.format("TRUNCATE TABLE %s CASCADE", x));
			JDBCUtils.commit();
		}
	}

	Collection<_Current> query() throws Exception {
		String sql;
		sql = "SELECT j10.id,\n"
				+ "j10.output_dir AS outputDir\n"
			+ "FROM j_export_job AS j10\n"
			+ "WHERE j10.deleted = FALSE\n"
			+ "AND NOT EXISTS\n"
			+ "(\n"
				+ "SELECT NULL\n"
				+ "FROM j_export_job_status AS j900\n"
				+ "WHERE j900.foreign_id = j10.id\n"
			+ ")\n"
			+ "ORDER BY j10.id\n";

		val rsh = new BeanListHandler<_Current>(_Current.class);
		return JDBCUtils.query(sql, rsh);
	}

	@Data
	public static class _Current {
		Long m_id;
		String m_outputDir;
		Status m_status;

		public Status getStatus() {
			return (m_status == null ? m_status = new Status() : m_status);
		}

		public void execute() throws Exception {
			log.info(String.format("Job[id=%d output=%s]",
					getId(),
					getOutputDir()));

			try (val status = getStatus()) {
				try {
					request();
					status.setStatus(JobStatus.SUCCESS);
					log.info("Done!!");
				} catch (Exception e) {
					log.error("", e);
					status.setStatus(JobStatus.FAILD);
					status.setMessage(e.getMessage());
					JDBCUtils.commit();
				}
			} catch (Exception e) {
				log.error("", e);
			}
		}

		void request() throws Exception {
			Request.getInstance().execute();
		}
	}
}
