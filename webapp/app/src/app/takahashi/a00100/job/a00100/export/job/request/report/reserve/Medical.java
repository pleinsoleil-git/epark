package app.takahashi.a00100.job.a00100.export.job.request.report.reserve;

import java.math.BigDecimal;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ArrayListHandler;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellUtil;

import app.takahashi.a00100.job.a00100.export.job.Job;
import app.takahashi.a00100.job.a00100.export.job.request.report.Report;
import common.jdbc.JDBCParameter;
import common.jdbc.JDBCUtils;
import lombok.val;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Accessors(prefix = "m_", chain = false)
class Medical {
	static Medical m_instance;
	_Current m_current;

	Medical() {
	}

	public static Medical getInstance() {
		return (m_instance == null ? m_instance = new Medical() : m_instance);
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
				val book = Report.getCurrent().getBook();

				for (val name : new String[] {
						"診療希望内容",
				}) {
					val sheet = book.getSheet(name);

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
			int rowNum = 1;

			for (val rec : query()) {
				val row = CellUtil.getRow(rowNum++, sheet);
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
							if (x instanceof BigDecimal) {
								cell.setCellValue(((BigDecimal) x).doubleValue());
							} else if (x instanceof Long) {
								cell.setCellValue((Long) x);
							} else {
								cell.setCellValue(x.toString());
							}
						}
					}
				}
			}
		}

		Map<String, Integer> getHeader() throws Exception {
			return new LinkedHashMap<String, Integer>() {
				{
					new QueryRunner() {
						{
							String sql;
							sql = "WITH s_params AS\n"
								+ "(\n"
									+ "SELECT CAST( ? AS BIGINT ) AS job_id\n"
								+ ")\n"
								+ "SELECT t30.title\n"
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
								+ "INNER JOIN t_reserve_menu AS t30\n"
									+ "ON t30.foreign_id = t20.id\n"
								+ "GROUP BY 1\n"
								+ "ORDER BY COUNT( t30.title ) DESC,\n"
									+ "1\n";

							val sheet = getSheet();
							val row = CellUtil.getRow(0, sheet);
							int cellNum;

							for (cellNum = 0;; cellNum++) {
								val cell = CellUtil.getCell(row, cellNum);
								if (StringUtils.isEmpty(cell.getStringCellValue()) == true) {
									break;
								}
							}

							val params = new JDBCParameter() {
								{
									val job = Job.getCurrent();
									add(job.getId());
								}
							};
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
			};
		}

		Collection<Object[]> query() throws Exception {
			String sql;
			sql = "WITH s_params AS\n"
				+ "(\n"
					+ "SELECT CAST( ? AS BIGINT ) AS job_id\n"
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
					+ "t10.title_count,\n"
					+ "t10.title\n"
				+ "FROM\n"
				+ "(\n"
					+ "SELECT m10.id AS clinic_id,\n"
						+ "COUNT( t30.title ) AS title_count,\n"
						+ "ARRAY_AGG( t30.title ) AS title\n"
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
				+ ") AS t10\n"
				+ "INNER JOIN m_clinic AS m10\n"
					+ "ON m10.id = t10.clinic_id\n"
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
