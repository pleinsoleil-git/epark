package app.takahashi.a00100.job.a00100.export.job.request.report.top;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.dbutils.handlers.ArrayListHandler;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellUtil;

import app.takahashi.a00100.job.a00100.export.job.Job;
import app.takahashi.a00100.job.a00100.export.job.request.report.Report;
import common.jdbc.JDBCParameter;
import common.jdbc.JDBCUtils;
import common.lang.IntegerUtils;
import common.lang.StringUtils;
import common.poi.CellUtils;
import lombok.Data;
import lombok.val;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Accessors(prefix = "m_", chain = false)
public class MenuOldAndNew {
	static MenuOldAndNew m_instance;
	_Current m_current;

	MenuOldAndNew() {
	}

	public static MenuOldAndNew getInstance() {
		return (m_instance == null ? m_instance = new MenuOldAndNew() : m_instance);
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
						m_sheetName = "メニュー新旧";
					}
				});
			}
		};
	}

	@Data
	public static class _Current {
		String m_sheetName;
		Sheet m_sheet;
		Map<String, Integer> m_oldCells;
		Map<String, Integer> m_newCells;

		public Sheet getSheet() {
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
			header();
		}

		void header() throws Exception {
			val sheet = getSheet();
			val formatter = new DataFormatter();
			val rows = new Row[] {
					CellUtils.getRow(sheet, 0),
					CellUtils.getRow(sheet, 1),
			};
			val cellNums = new Integer[] {
					0,
					0,
			};

			for (;; cellNums[0]++, cellNums[1]++) {
				val value = formatter.formatCellValue(CellUtils.getCell(rows[1], cellNums[1]));
				if (StringUtils.isEmpty(value) == true) {
					break;
				}
			}

			String sql;
			sql = "WITH s_params AS\n"
				+ "(\n"
					+ "SELECT ?::BOOLEAN AS olded,\n"
						+ "?::INT AS cellNum\n"
				+ ")\n"
				+ "SELECT m10.title,\n"
					+ "ROW_NUMBER() OVER() + t10.cellNum\n"
				+ "FROM s_params AS t10\n"
				+ "INNER JOIN m_top_menu_item AS m10\n"
					+ "ON m10.olded = t10.olded\n"
					+ "AND m10.deleted = FALSE\n"
				+ "ORDER BY m10.id\n";

			// --------------------------------------------------
			// Webメニュー（旧）
			// --------------------------------------------------
			m_oldCells = new LinkedHashMap<String, Integer>() {
				{
					val rsh = new ArrayListHandler();

					for (val x : JDBCUtils.query(sql, rsh, new JDBCParameter() {
						{
							add(true);
							add(--cellNums[1]);
						}
					})) {
						val key = StringUtils.valueOf(x[0]);
						val value = IntegerUtils.valueOf(x[1]);
						put(key, value);
					}
				}
			};

			Cell cell = CellUtils.getCell(rows[0], cellNums[0]);
			CellUtils.setCellValue(cell, "Webメニュー（旧）");

			for (val entry : m_oldCells.entrySet()) {
				cell = CellUtils.getCell(rows[1], entry.getValue());
				CellUtils.setCellValue(cell, entry.getKey());
			}

			// --------------------------------------------------
			// Webメニュー（新）
			// --------------------------------------------------
			m_newCells = new LinkedHashMap<String, Integer>() {
				{
					val rsh = new ArrayListHandler();

					for (val x : JDBCUtils.query(sql, rsh, new JDBCParameter() {
						{
							add(false);
							add(cellNums[1] + m_oldCells.size());
						}
					})) {
						val key = StringUtils.valueOf(x[0]);
						val value = IntegerUtils.valueOf(x[1]);
						put(key, value);
					}
				}
			};

			cell = CellUtils.getCell(rows[0], cellNums[0] + m_oldCells.size());
			CellUtils.setCellValue(cell, "Webメニュー（新）");

			for (val entry : m_newCells.entrySet()) {
				cell = CellUtils.getCell(rows[1], entry.getValue());
				CellUtils.setCellValue(cell, entry.getKey());
			}
		}

		void body() throws Exception {
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
					if (cellNum == 0) {
						uri = "https://haisha-yoyaku.jp/bun2sdental/detail/index/id/"
								+ x.toString();
					}

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
				+ "LEFT JOIN i_clinic AS t90\n"
					+ "ON t90.catalog_id = m10.catalog_id\n"
				+ "ORDER BY t90.id\n";

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
