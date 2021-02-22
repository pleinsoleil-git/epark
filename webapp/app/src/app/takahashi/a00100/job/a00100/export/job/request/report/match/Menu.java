package app.takahashi.a00100.job.a00100.export.job.request.report.match;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.apache.commons.dbutils.handlers.ArrayListHandler;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.util.CellUtil;

import app.takahashi.a00100.job.a00100.export.job.Job;
import app.takahashi.a00100.job.a00100.export.job.request.report.Report;
import common.jdbc.JDBCParameter;
import common.jdbc.JDBCUtils;
import common.poi.CellUtils;
import lombok.Data;
import lombok.val;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Accessors(prefix = "m_", chain = false)
public class Menu {
	static Menu m_instance;
	_Current m_current;

	Menu() {
	}

	public static Menu getInstance() {
		return (m_instance == null ? m_instance = new Menu() : m_instance);
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
				add(new _Current() {
					{
						m_sheetName = "メニュー一致率";
					}
				});
			}
		};
	}

	@Data
	public static class _Current {
		String m_sheetName;

		public void execute() throws Exception {
			log.info(getSheetName());
			output();
		}

		void output() throws Exception {
			val sheet = Report.getCurrent().getBook().getSheet(getSheetName());
			val styles = new HashMap<Integer, CellStyle>() {
				{
					for (val cell : sheet.getRow(1)) {
						val style = cell.getCellStyle();
						if (style != null) {
							put(cell.getColumnIndex(), style);
						}
					}
				}
			}.entrySet();
			int rowNum = 1;

			for (val rec : query()) {
				val row = CellUtil.getRow(rowNum++, sheet);
				for (val style : styles) {
					val cell = CellUtils.getCell(row, style.getKey());
					cell.setCellStyle(style.getValue());
				}

				int cellNum = 0;
				String uri = null;

				for (val x : rec) {
					val cell = CellUtil.getCell(row, cellNum++);
					CellUtils.setCellValue(cell, x);
				}

				val cell = CellUtil.getCell(row, cellNum++);
				cell.setCellValue(uri);
			}
		}

		Collection<Object[]> query() throws Exception {
			String sql;
			sql = "WITH s_params AS\n"
				+ "(\n"
					+ "SELECT ?::BIGINT AS job_id,\n"
						+ "?::VARCHAR AS title\n"
				+ ")\n"
				+ "SELECT m10.catalog_id,\n"
					+ "m10.prov_name,\n"
					+ "m10.city,\n"
					+ "m10.station1,\n"
					+ "COUNT( m10.catalog_id )\n"
						+ "OVER\n"
						+ "(\n"
							+ "PARTITION BY m10.prov_name\n"
						+ "),\n"
					+ "COUNT( m10.catalog_id )\n"
						+ "OVER\n"
						+ "(\n"
							+ "PARTITION BY m10.prov_name, m10.city\n"
						+ "),\n"
					+ "COUNT( m10.catalog_id )\n"
						+ "OVER\n"
						+ "(\n"
							+ "PARTITION BY m10.prov_name, m10.city, m10.station1\n"
						+ "),\n"
					+ "t10.top_count,\n"
					+ "t20.reserve_count,\n"
					+ "t10.match_count,\n"
					+ "CASE WHEN t10.top_count = 0 THEN 0\n"
						 + "ELSE TRUNC( t10.match_count / t10.top_count::NUMERIC, 5 )\n"
					+ "END,\n"
					+ "m10.detail_ss::NUMERIC,\n"
					+ "m10.cv,\n"
					+ "m10.cvr\n"
				+ "FROM\n"
				+ "(\n"
					+ "SELECT m10.id AS clinic_id,\n"
						+ "COUNT( t40.title ) AS top_count,\n"
						+ "COUNT( t50.id ) AS match_count\n"
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
					+ "LEFT JOIN t_top_menu AS t30\n"
						+ "ON t30.foreign_id = t20.id\n"
						+ "AND t30.title = t10.title\n"
					+ "LEFT JOIN t_top_menu_item AS t40\n"
						+ "ON t40.foreign_id = t30.id\n"
					+ "LEFT JOIN t_reserve_menu AS t50\n"
						+ "ON t50.foreign_id = t20.id\n"
						+ "AND t50.title = t40.title\n"
					+ "GROUP BY m10.id\n"
				+ ") AS t10\n"
				+ "INNER JOIN\n"
				+ "(\n"
					+ "SELECT m10.id AS clinic_id,\n"
						+ "COUNT( t30.title ) AS reserve_count\n"
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
					+ "LEFT JOIN t_reserve_menu AS t30\n"
						+ "ON t30.foreign_id = t20.id\n"
					+ "GROUP BY m10.id\n"
				+ ") AS t20\n"
					+ "ON t20.clinic_id = t10.clinic_id\n"
				+ "INNER JOIN m_clinic AS m10\n"
					+ "ON m10.id = t10.clinic_id\n"
				+ "ORDER BY m10.cvr_rank,\n"
					+ "m10.id\n";

			val params = new JDBCParameter() {
				{
					val job = Job.getCurrent();
					add(job.getId());
					add("治療内容");
				}
			};
			val rsh = new ArrayListHandler();
			return JDBCUtils.query(sql, rsh, params);
		}
	}
}
