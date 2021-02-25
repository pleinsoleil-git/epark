package app.takahashi.a00100.job.a00100.export.job.request.report.top;

import java.sql.Array;
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
import common.lang.BooleanUtils;
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
		Row m_row;
		Map<String, Integer> m_oldCells;
		Map<String, Integer> m_newCells;
		Map<String, Integer> m_originalCells;

		Sheet getSheet() {
			if (m_sheet == null) {
				m_sheet = Report.getCurrent().getBook().getSheet(getSheetName());
			}

			return m_sheet;
		}

		void zerofill() throws Exception {
			for (val x : m_oldCells.values()) {
				val cell = CellUtils.getCell(m_row, x);
				cell.setCellValue(0);
			}

			for (val x : m_newCells.values()) {
				val cell = CellUtils.getCell(m_row, x);
				cell.setCellValue(0);
			}

			for (val x : m_originalCells.values()) {
				val cell = CellUtils.getCell(m_row, x);
				cell.setCellValue(0);
			}
		}

		boolean setOldCellValue(final String title, final Object value) throws Exception {
			val cellNum = m_oldCells.get(title);
			if (cellNum != null) {
				val cell = CellUtils.getCell(m_row, cellNum);
				CellUtils.setCellValue(cell, value);
				return true;
			}

			return false;
		}

		boolean setNewCellValue(final String title, final Object value) throws Exception {
			val cellNum = m_newCells.get(title);
			if (cellNum != null) {
				val cell = CellUtils.getCell(m_row, cellNum);
				CellUtils.setCellValue(cell, value);
				return true;
			}

			return false;
		}

		void setOriginalCellValue(final String title, final Object value) throws Exception {
			Integer cellNum = m_originalCells.get(title);
			if (cellNum == null) {
				if (m_originalCells.isEmpty() == true) {
					for (val v : m_newCells.values()) {
						cellNum = v;
					}
				} else {
					for (val v : m_originalCells.values()) {
						cellNum = v;
					}
				}

				cellNum++;

				if (m_originalCells.isEmpty() == true) {
					val row = CellUtils.getRow(getSheet(), 0);
					val cell = CellUtils.getCell(row, cellNum);
					CellUtils.setCellValue(cell, "Webメニュー（独自）");
				}

				val row = CellUtils.getRow(getSheet(), 1);
				val cell = CellUtils.getCell(row, cellNum);
				CellUtils.setCellValue(cell, title);

				m_originalCells.put(title, cellNum);
			}

			val cell = CellUtils.getCell(m_row, cellNum);
			CellUtils.setCellValue(cell, value);
		}

		public void execute() throws Exception {
			log.info(getSheetName());
			output();
		}

		void output() throws Exception {
			header();
			body();
		}

		void header() throws Exception {
			val sheet = getSheet();
			val formatter = new DataFormatter();
			val rows = new Row[] {
					CellUtils.getRow(sheet, 0),
					CellUtils.getRow(sheet, 1),
			};
			val cells = new Cell[] {
					null,
					null,
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

			cells[0] = CellUtils.getCell(rows[0], cellNums[0]);
			CellUtils.setCellValue(cells[0], "Webメニュー（旧）");

			for (val entry : m_oldCells.entrySet()) {
				cells[1] = CellUtils.getCell(rows[1], entry.getValue());
				CellUtils.setCellValue(cells[1], entry.getKey());
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

			cells[0] = CellUtils.getCell(rows[0], cellNums[0] + m_oldCells.size());
			CellUtils.setCellValue(cells[0], "Webメニュー（新）");

			for (val entry : m_newCells.entrySet()) {
				cells[1] = CellUtils.getCell(rows[1], entry.getValue());
				CellUtils.setCellValue(cells[1], entry.getKey());
			}

			// --------------------------------------------------
			// Webメニュー（独自）
			// --------------------------------------------------
			m_originalCells = new LinkedHashMap<String, Integer>();
		}

		void body() throws Exception {
			val FIRST_ROW = 2;
			val sheet = Report.getCurrent().getBook().getSheet(getSheetName());
			val styles = new HashMap<Integer, CellStyle>() {
				{
					for (val cell : sheet.getRow(FIRST_ROW)) {
						val style = cell.getCellStyle();
						if (style != null) {
							put(cell.getColumnIndex(), style);
						}
					}
				}
			}.entrySet();
			int rowNum = FIRST_ROW;

			for (val rec : query()) {
				m_row = CellUtils.getRow(sheet, rowNum++);
				for (val style : styles) {
					val cell = CellUtils.getCell(m_row, style.getKey());
					cell.setCellStyle(style.getValue());
				}

				Boolean olded = null;
				int cellNum = 0;

				for (int colNum = 0; colNum < rec.length; colNum++) {
					if (colNum == 0) {
						olded = (Boolean) rec[colNum];
						continue;
					}

					val value = rec[colNum];
					if (value != null) {
						if (value instanceof Array) {
							zerofill();

							for (val v : (String[]) ((Array) value).getArray()) {
								if (StringUtils.isNotEmpty(v) == false) {
									continue;
								}

								// --------------------------------------------------
								// Webメニュー（旧）
								// --------------------------------------------------
								if (BooleanUtils.isTrue(olded) == true) {
									if (setOldCellValue(v, 1L) == true) {
										continue;
									}
								}

								// --------------------------------------------------
								// Webメニュー（新）
								// --------------------------------------------------
								if (setNewCellValue(v, 1) == true) {
									continue;
								}

								// --------------------------------------------------
								// Webメニュー（独自）
								// --------------------------------------------------
								setOriginalCellValue(v, 1);
							}
						} else {
							val cell = CellUtil.getCell(m_row, cellNum++);
							CellUtils.setCellValue(cell, value);
						}
					}
				}
			}
		}

		Collection<Object[]> query() throws Exception {
			String sql;
			sql = "CREATE TEMP TABLE tmp_clinic\n"
				+ "(\n"
					+ "catalog_id	VARCHAR( 512 ),\n"
					+ "menu_olded	BOOLEAN,\n"
					+ "UNIQUE\n"
					+ "(\n"
						+ "catalog_id\n"
					+ ")\n"
				+ ")\n";

			JDBCUtils.execute(sql);
			// --------------------------------------------------
			// 確認が必要な時はtmp_clinicを作成する
			// --------------------------------------------------
			// JDBCUtils.execute("TRUNCATE TABLE tmp_clinic");
			JDBCUtils.commit();

			sql = "WITH s_params AS\n"
				+ "(\n"
					+ "SELECT ?::BIGINT AS job_id,\n"
						+ "?::VARCHAR AS title\n"
				+ ")\n"
				+ "INSERT INTO tmp_clinic\n"
				+ "(\n"
					+ "catalog_id,\n"
					+ "menu_olded\n"
				+ ")\n"
				+ "SELECT m10.catalog_id,\n"
					+ "t10.has_old\n"
				+ "FROM\n"
				+ "(\n"
					+ "SELECT t10.clinic_id,\n"
						+ "MAX( t10.has_old )::BOOLEAN AS has_old\n"
					+ "FROM\n"
					+ "(\n"
						+ "SELECT m10.id AS clinic_id,\n"
							+ "t40.title,\n"
							+ "MAX( CASE WHEN m20.olded = TRUE THEN 1 ELSE 0 END ) AS has_old,\n"
							+ "MAX( CASE WHEN m20.olded = FALSE THEN 1 ELSE 0 END ) AS has_new\n"
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
						+ "INNER JOIN m_top_menu_item AS m20\n"
							+ "ON m20.title = t40.title\n"
							+ "AND m20.deleted = FALSE\n"
						+ "GROUP BY 1, 2\n"
					+ ") AS t10\n"
					+ "WHERE t10.has_old != t10.has_new\n"
					+ "GROUP BY 1\n"
				+ ") AS t10\n"
				+ "INNER JOIN m_clinic AS m10\n"
					+ "ON m10.id = t10.clinic_id\n";

			JDBCUtils.execute(sql, new JDBCParameter() {
				{
					val job = Job.getCurrent();
					add(job.getId());
					add("治療内容");
				}
			});
			JDBCUtils.commit();

			sql = "WITH s_params AS\n"
				+ "(\n"
					+ "SELECT ?::BIGINT AS job_id,\n"
						+ "?::VARCHAR AS title\n"
				+ ")\n"
				+ "SELECT t20.menu_olded,\n"
					+ "m10.catalog_id,\n"
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
				+ "LEFT JOIN tmp_clinic AS t20\n"
					+ "ON t20.catalog_id = m10.catalog_id\n"
				+ "LEFT JOIN i_clinic AS t90\n"
					+ "ON t90.catalog_id = m10.catalog_id\n"
				+ "ORDER BY t90.id\n";

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
