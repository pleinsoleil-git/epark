package app.takahashi.a00100.job.a00100.export.job.request.report.top;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.dbutils.handlers.ArrayListHandler;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellUtil;

import app.takahashi.a00100.job.a00100.export.job.Job;
import app.takahashi.a00100.job.a00100.export.job.request.report.Report;
import common.jdbc.JDBCParameter;
import common.jdbc.JDBCUtils;
import common.poi.CellUtils;
import lombok.val;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Accessors(prefix = "m_", chain = false)
class Menu {
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
				String sql;
				sql = "WITH s_params AS\n"
					+ "(\n"
						+ "SELECT ?::BIGINT AS job_id\n"
					+ ")\n"
					+ "SELECT DISTINCT t30.title\n"
					+ "FROM s_params AS t10\n"
					+ "INNER JOIN j_job AS j10\n"
						+ "ON j10.id = t10.job_id\n"
					+ "INNER JOIN j_request AS j20\n"
						+ "ON j20.foreign_id = j10.id\n"
						+ "AND j20.deleted = FALSE\n"
					+ "INNER JOIN m_clinic AS m10\n"
						+ "ON m10.foreign_id = j10.id\n"
						+ "AND m10.catalog_id = j20.catalog_id\n"
					+ "INNER JOIN t_clinic AS t20\n"
						+ "ON t20.foreign_id = j10.id\n"
						+ "AND t20.catalog_id = j20.catalog_id\n"
					+ "INNER JOIN t_top_menu AS t30\n"
						+ "ON t30.foreign_id = t20.id\n"
					+ "WHERE EXISTS\n"
					+ "(\n"
						+ "SELECT NULL\n"
						+ "FROM t_top_menu_item AS t900\n"
						+ "WHERE t900.foreign_id = t30.id\n"
					+ ")\n";

				val params = new JDBCParameter() {
					{
						val job = Job.getCurrent();
						add(job.getId());
					}
				};
				val rsh = new ArrayListHandler();
				val book = Report.getCurrent().getBook();

				for (val rec : JDBCUtils.query(sql, rsh, params)) {
					val sheet = book.getSheet(rec[0].toString());

					if (sheet != null) {
						add(new _Current() {
							{
								m_sheetName = sheet.getSheetName();
							}
						});
					}
				}
			}
		};
	}

	static class _Current {
		String m_sheetName;
		Sheet m_sheet;

		String getSheetName() {
			return m_sheetName;
		}

		Sheet getSheet() throws Exception {
			if (m_sheet == null) {
				m_sheet = Report.getCurrent().getBook().getSheet(getSheetName());
			}

			return m_sheet;
		}

		public void execute() throws Exception {
			log.info(getSheetName());
			output();
		}

		void output() throws Exception {
			val sheet = getSheet();
			val header = getHeader();
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

				for (val x : rec) {
					if (x != null) {
						if (x instanceof Array) {
							for (val v : header.values()) {
								val cell = CellUtil.getCell(row, v);
								cell.setCellValue(0L);
							}

							for (val v : (String[]) ((Array) x).getArray()) {
								if (StringUtils.isNotEmpty(v) == true) {
									val cell = CellUtil.getCell(row, header.get(v));
									cell.setCellValue(1L);
								}
							}
						} else {
							val cell = CellUtil.getCell(row, cellNum++);
							CellUtils.setCellValue(cell, x);
						}
					}
				}
			}
		}

		Map<String, Integer> getHeader() throws Exception {
			return new LinkedHashMap<String, Integer>() {
				{
					String sql;
					sql = "WITH s_params AS\n"
						+ "(\n"
							+ "SELECT ?::BIGINT AS job_id,\n"
								+ "?::VARCHAR AS title\n"
						+ ")\n"
						+ "SELECT t40.title\n"
						+ "FROM s_params AS t10\n"
						+ "INNER JOIN j_job AS j10\n"
							+ "ON j10.id = t10.job_id\n"
						+ "INNER JOIN j_request AS j20\n"
							+ "ON j20.foreign_id = j10.id\n"
							+ "AND j20.deleted = FALSE\n"
						+ "INNER JOIN m_clinic AS m10\n"
							+ "ON m10.foreign_id = j10.id\n"
							+ "AND m10.catalog_id = j20.catalog_id\n"
						+ "INNER JOIN t_clinic AS t20\n"
							+ "ON t20.foreign_id = j10.id\n"
							+ "AND t20.catalog_id = j20.catalog_id\n"
						+ "INNER JOIN t_top_menu AS t30\n"
							+ "ON t30.foreign_id = t20.id\n"
							+ "AND t30.title = t10.title\n"
						+ "INNER JOIN t_top_menu_item AS t40\n"
							+ "ON t40.foreign_id = t30.id\n"
						+ "GROUP BY 1\n"
						+ "ORDER BY COUNT( t40.title ) DESC,\n"
							+ "1\n";

					val sheet = getSheet();
					val params = new JDBCParameter() {
						{
							val job = Job.getCurrent();
							add(job.getId());
							add(sheet.getSheetName());
						}
					};

					val row = CellUtil.getRow(0, sheet);
					int cellNum;

					for (cellNum = 0;; cellNum++) {
						val cell = CellUtil.getCell(row, cellNum);
						if (StringUtils.isEmpty(cell.getStringCellValue()) == true) {
							break;
						}
					}

					val rsh = new ArrayListHandler();

					for (val rec : JDBCUtils.query(sql, rsh, params)) {
						val value = rec[0].toString();

						if (StringUtils.isNotEmpty(value) == true) {
							val cell = CellUtil.getCell(row, cellNum++);
							cell.setCellValue(value);
							put(value, cell.getColumnIndex());
						}
					}
				}
			};
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
					+ "m10.detail_ss::NUMERIC,\n"
					+ "m10.cv,\n"
					+ "m10.cvr,\n"
					+ "t10.title_count,\n"
					+ "t10.title\n"
				+ "FROM\n"
				+ "(\n"
					+ "SELECT m10.id AS clinic_id,\n"
						+ "COUNT( t40.title ) AS title_count,\n"
						+ "ARRAY_AGG( t40.title ) AS title\n"
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
					+ "GROUP BY m10.id\n"
				+ ") AS t10\n"
				+ "INNER JOIN m_clinic AS m10\n"
					+ "ON m10.id = t10.clinic_id\n"
				+ "LEFT JOIN i_clinic AS t90\n"
					+ "ON t90.catalog_id = m10.catalog_id\n"
				+ "ORDER BY t90.id\n";

			val sheet = getSheet();
			val params = new JDBCParameter() {
				{
					val job = Job.getCurrent();
					add(job.getId());
					add(sheet.getSheetName());
				}
			};
			val rsh = new ArrayListHandler();
			return JDBCUtils.query(sql, rsh, params);
		}
	}
}
