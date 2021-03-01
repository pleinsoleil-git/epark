package app.takahashi.a00100.job.a00100.export.job.request.report.top;

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
public class MenuType {
	static MenuType m_instance;
	_Current m_current;

	MenuType() {
	}

	public static MenuType getInstance() {
		return (m_instance == null ? m_instance = new MenuType() : m_instance);
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
						m_sheetName = "メニュー種別";
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

				for (val x : rec) {
					val cell = CellUtil.getCell(row, cellNum++);
					CellUtils.setCellValue(cell, x);
				}
			}
		}

		Collection<Object[]> query() throws Exception {
			String sql;
			sql = "WITH s_params AS\n"
				+ "(\n"
					+ "SELECT ?::BIGINT AS job_id,\n"
						+ "?::VARCHAR AS title\n"
				+ ")\n"
				+ "SELECT t10.title,\n"
					+ "t10.detail_ss,\n"
					+ "t10.cv,\n"
					+ "t10.cvr\n"
				+ "FROM\n"
				+ "(\n"
					+ "SELECT t10.title,\n"
						+ "t10.detail_ss,\n"
						+ "t10.cv,\n"
						+ "CASE WHEN t10.detail_ss = 0 THEN 0\n"
							 + "ELSE TRUNC( t10.cv / t10.detail_ss, 5 )\n"
						+ "END AS cvr,\n"
						+ "ROW_NUMBER() OVER ( ORDER BY m10.id, t10.title ) AS row_num\n"
					+ "FROM\n"
					+ "(\n"
						+ "SELECT t40.title,\n"
							+ "SUM( m10.detail_ss::NUMERIC ) AS detail_ss,\n"
							+ "SUM( m10.cv ) AS cv\n"
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
					+ ") AS t10\n"
					+ "CROSS JOIN s_params AS t20\n"
					+ "LEFT JOIN m_top_menu_item AS m10\n"
						+ "ON m10.menu = t20.title\n"
						+ "AND m10.title = t10.title\n"
				+ ") AS t10\n"
				+ "ORDER BY t10.cvr DESC, t10.row_num\n";

			val rsh = new ArrayListHandler();
			return JDBCUtils.query(sql, rsh, new JDBCParameter() {
				{
					val job = Job.getCurrent();
					add(job.getId());
					add("治療内容");
				}
			});
		}
	}
}
