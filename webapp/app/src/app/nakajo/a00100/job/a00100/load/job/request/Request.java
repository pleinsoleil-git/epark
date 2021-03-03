package app.nakajo.a00100.job.a00100.load.job.request;

import java.io.File;
import java.util.Collection;

import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import app.nakajo.a00100.job.a00100.load.job.Job;
import app.nakajo.a00100.job.a00100.load.job.request.load.Load;
import app.nakajo.a00100.job.a00100.load.job.request.load.report.Report;
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
		try {
			delete();

			for (val x : query()) {
				(m_current = x).execute();
			}

			report();
		} finally {
			m_instance = null;
		}
	}

	void delete() throws Exception {
		for (val x : new String[] {
				"t_usage_history",
				"t_repeat_report",
		}) {
			log.info(String.format("Delete %s", x));
			JDBCUtils.execute(String.format("TRUNCATE TABLE %s CASCADE", x));
			JDBCUtils.commit();
		}
	}

	void report() throws Exception {
		Report.getInstance().execute();
	}

	Collection<_Current> query() throws Exception {
		String sql;
		sql = "WITH s_params AS\n"
			+ "(\n"
				+ "SELECT ?::BIGINT AS job_id\n"
			+ ")\n"
			+ "SELECT j10.id,\n"
				+ "j10.data_type AS dataType,\n"
				+ "j10.input_file AS inputFile,\n"
				+ "j10.input_sheet AS inputSheet\n"
			+ "FROM s_params AS t10\n"
			+ "INNER JOIN j_load_request AS j10\n"
				+ "ON j10.foreign_id = t10.job_id\n"
			+ "WHERE j10.deleted = FALSE\n"
			+ "AND NOT EXISTS\n"
			+ "(\n"
				+ "SELECT NULL\n"
				+ "FROM j_load_request_status AS j900\n"
				+ "WHERE j900.foreign_id = j10.id\n"
			+ ")\n"
			+ "ORDER BY 1\n";

		val rsh = new BeanListHandler<_Current>(_Current.class);
		return JDBCUtils.query(sql, rsh,
				new JDBCParameter() {
					{
						val job = Job.getCurrent();
						add(job.getId());
					}
				});
	}

	@Data
	public static class _Current {
		Long m_id;
		String m_dataType;
		String m_inputFile;
		String m_inputSheet;
		Workbook m_workbook;
		Sheet m_sheet;
		Status m_status;

		public Workbook getWorkbook() {
			if (m_workbook == null) {
				try {
					m_workbook = WorkbookFactory.create(new File(getInputFile()));
				} catch (Exception e) {
					log.error("", e);
				}
			}

			return m_workbook;
		}

		public Sheet getSheet() {
			if (m_sheet == null) {
				m_sheet = getWorkbook().getSheet(getInputSheet());
			}

			return m_sheet;
		}

		public Status getStatus() {
			return (m_status == null ? m_status = new Status() : m_status);
		}

		public void execute() throws Exception {
			log.info(String.format("Request[id=%d type=%s file=%s sheet=%s]",
					getId(),
					getDataType(),
					getInputFile(),
					getInputSheet()));

			try (val status = getStatus()) {
				try {
					load();
					status.setStatus(JobStatus.SUCCESS);
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

		void load() throws Exception {
			Load.getInstance().execute();
		}
	}
}
