package app.takahashi.a00100.job.a00100.export.job.request.report.top;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.dbutils.handlers.ArrayListHandler;
import org.apache.poi.ss.util.CellUtil;

import app.takahashi.a00100.job.a00100.export.job.Job;
import app.takahashi.a00100.job.a00100.export.job.request.report.Report;
import common.jdbc.JDBCParameter;
import common.jdbc.JDBCUtils;
import common.poi.CellUtils;
import lombok.val;
import lombok.experimental.Accessors;

@Accessors(prefix = "m_", chain = false)
public class Clinic {
	static Clinic m_instance;
	_Current m_current;

	Clinic() {
	}

	public static Clinic getInstance() {
		return (m_instance == null ? m_instance = new Clinic() : m_instance);
	}

	public static _Current getCurrent() {
		return getInstance().m_current;
	}

	public void execute() throws Exception {
		try {
			for (val x : query()) {
				(m_current = x).execute();
			}
		} finally {
			m_instance = null;
		}
	}

	Collection<_Current> query() throws Exception {
		return new ArrayList<_Current>() {
			{
				add(new _Current());
			}
		};
	}

	public static class _Current {
		public void execute() throws Exception {
			output();
		}

		void output() throws Exception {
			val sheet = Report.getCurrent().getBook().getSheet("医院");
			int rowNum = 1;

			for (val rec : query()) {
				val row = CellUtil.getRow(rowNum++, sheet);
				int cellNum = 0;
				String uri = null;

				for (val value : rec) {
					if (cellNum == 0) {
						uri = "https://haisha-yoyaku.jp/bun2sdental/detail/index/id/"
								+ value.toString();
					}

					val cell = CellUtil.getCell(row, cellNum++);
					CellUtils.setCellValue(cell, value);
				}

				val cell = CellUtil.getCell(row, cellNum++);
				cell.setCellValue(uri);
			}
		}

		Collection<Object[]> query() throws Exception {
			String sql;
			sql = "WITH s_params AS\n"
				+ "(\n"
					+ "SELECT ?::BIGINT AS job_id\n"
				+ ")\n"
				+ "SELECT m10.catalog_id,\n"
					+ "m10.shopowner_id,\n"
					+ "t20.dental_name,\n"
					+ "t20.total_star::NUMERIC,\n"
					+ "t20.review_count::NUMERIC,\n"
					+ "t20.net_reserve_type,\n"
					+ "m10.latest_opening_time::NUMERIC,\n"
					+ "m10.rich_type,\n"
					+ "m10.post_code,\n"
					+ "m10.prov_name,\n"
					+ "m10.city,\n"
					+ "m10.station1,\n"
					+ "m10.detail_ss::NUMERIC,\n"
					+ "m10.cv,\n"
					+ "m10.cvr,\n"
					+ "m10.cvr_rank\n"
				+ "FROM s_params AS t10\n"
				+ "INNER JOIN j_job AS j10\n"
					+ "ON j10.id = t10.job_id\n"
				+ "INNER JOIN j_request AS j20\n"
					+ "ON j20.foreign_id = j10.id\n"
					+ "AND j20.deleted = FALSE\n"
				+ "INNER JOIN m_clinic AS m10\n"
					+ "ON m10.foreign_id = j10.id\n"
					+ "AND m10.catalog_id = j20.catalog_id\n"
				+ "LEFT JOIN t_clinic AS t20\n"
					+ "ON t20.foreign_id = j10.id\n"
					+ "AND t20.catalog_id = j20.catalog_id\n"
				+ "ORDER BY m10.cvr_rank,\n"
					+ "m10.id\n";

			val params = new JDBCParameter() {
				{
					val job = Job.getCurrent();
					add(job.getId());
				}
			};
			val rsh = new ArrayListHandler();
			return JDBCUtils.query(sql, rsh, params);
		}
	}
}
