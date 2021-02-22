package app.takahashi.a00100.job.a00100.export.job.request.report.compare;

import java.math.BigDecimal;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;

import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellUtil;

import app.takahashi.a00100.job.a00100.export.job.Job;
import app.takahashi.a00100.job.a00100.export.job.request.report.Report;
import common.jdbc.JDBCParameter;
import common.jdbc.JDBCUtils;
import lombok.Data;
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
				val book = Report.getCurrent().getBook();

				for (val name : new String[] {
						"メニュー比較（診療）",
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

	@Data
	public static class _Record {
		String m_catalogId;
		String m_provName;
		String m_city;
		String m_station;
		Long m_provCount;
		Long m_cityCount;
		Long m_stationCount;
		Long m_topCount;
		Long m_reserveCount;
		Array m_topTitle;
		Array m_reserveTitle;
		Long m_ss;
		Long m_cv;
		BigDecimal m_cvr;
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
			int rowNum = 1;
			val rows = new Row[] {
					null,
					null,
			};
			val cellNums = new Integer[] {
					null,
					null,
			};
			val topMenus = new LinkedHashMap<String, Integer>();

			for (val rec : query()) {
				rows[0] = CellUtil.getRow(rowNum++, sheet);
				rows[1] = CellUtil.getRow(rowNum++, sheet);
				cellNums[0] = cellNums[1] = 0;

				for (int i = 0; i < rows.length; i++) {
					CellUtil.getCell(rows[i], cellNums[i]++).setCellValue(rec.getCatalogId());
				}

				for (int i = 0; i < rows.length; i++) {
					CellUtil.getCell(rows[i], cellNums[i]++).setCellValue(rec.getProvName());
				}

				for (int i = 0; i < rows.length; i++) {
					CellUtil.getCell(rows[i], cellNums[i]++).setCellValue(rec.getCity());
				}

				for (int i = 0; i < rows.length; i++) {
					CellUtil.getCell(rows[i], cellNums[i]++).setCellValue(rec.getStation());
				}

				CellUtil.getCell(rows[0], cellNums[0]++).setCellValue(rec.getProvCount());
				cellNums[1]++;

				CellUtil.getCell(rows[0], cellNums[0]++).setCellValue(rec.getCityCount());
				cellNums[1]++;

				CellUtil.getCell(rows[0], cellNums[0]++).setCellValue(rec.getStationCount());
				cellNums[1]++;

				CellUtil.getCell(rows[0], cellNums[0]++).setCellValue(rec.getSs());
				cellNums[1]++;

				CellUtil.getCell(rows[0], cellNums[0]++).setCellValue(rec.getCv());
				cellNums[1]++;

				CellUtil.getCell(rows[0], cellNums[0]++).setCellValue(rec.getCvr().doubleValue());
				cellNums[1]++;

				CellUtil.getCell(rows[0], cellNums[0]++).setCellValue(rec.getTopCount());
				CellUtil.getCell(rows[1], cellNums[1]++).setCellValue(rec.getReserveCount());

				CellUtil.getCell(rows[0], cellNums[0]++).setCellValue("トップ");
				CellUtil.getCell(rows[1], cellNums[1]++).setCellValue("予約");

				// ---------------------------------------------
				// メニュー一行目
				// ---------------------------------------------
				topMenus.clear();

				for (val x : (String[]) rec.getTopTitle().getArray()) {
					if (StringUtils.isNotEmpty(x) == true) {
						topMenus.put(x, cellNums[0]);
						CellUtil.getCell(rows[0], cellNums[0]++).setCellValue(x);
					}
				}

				cellNums[1] = cellNums[0];

				// --------------------------------------------------
				// メニュー二行目
				// --------------------------------------------------
				for (val x : (String[]) rec.getReserveTitle().getArray()) {
					val cellNum = topMenus.get(x);
					CellUtil.getCell(rows[1], (cellNum == null ? cellNums[1]++ : cellNum)).setCellValue(x);
				}
			}
		}

		Collection<_Record> query() throws Exception {
			String sql;
			sql = "WITH s_params AS\n"
				+ "(\n"
					+ "SELECT ?::BIGINT AS job_id,\n"
						+ "?::VARCHAR AS title\n"
				+ ")\n"
				+ "SELECT m10.catalog_id AS catalogId,\n"
					+ "m10.prov_name AS provName,\n"
					+ "m10.city,\n"
					+ "m10.station1 AS station,\n"
					+ "COUNT( m10.catalog_id )\n"
						+ "OVER\n"
						+ "(\n"
							+ "PARTITION BY m10.prov_name\n"
						+ ") AS provCount,\n"
					+ "COUNT( m10.catalog_id )\n"
						+ "OVER\n"
						+ "(\n"
							+ "PARTITION BY m10.prov_name, m10.city\n"
						+ ") AS cityCount,\n"
					+ "COUNT( m10.catalog_id )\n"
						+ "OVER\n"
						+ "(\n"
							+ "PARTITION BY m10.prov_name, m10.city, m10.station1\n"
						+ ") AS stationCount,\n"
					+ "m10.detail_ss::NUMERIC AS ss,\n"
					+ "m10.cv,\n"
					+ "m10.cvr,\n"
					+ "t10.title_count AS topCount,\n"
					+ "t10.title AS topTitle,\n"
					+ "t20.title_count AS reserveCount,\n"
					+ "t20.title AS reserveTitle\n"
				+ "FROM\n"
				+ "(\n"
					+ "SELECT m10.id AS clinic_id,\n"
						+ "COUNT( t40.title ) AS title_count,\n"
						+ "ARRAY_AGG( t40.title ORDER BY SIGN( t50.id ) NULLS LAST, t40.title ) AS title\n"
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
						+ "COUNT( t30.title ) AS title_count,\n"
						+ "ARRAY_AGG( t30.title ORDER BY t30.title ) AS title\n"
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
				+ "LEFT JOIN i_clinic AS t90\n"
					+ "ON t90.catalog_id = m10.catalog_id\n"
				+ "ORDER BY t90.id\n";

			val params = new JDBCParameter() {
				{
					val job = Job.getCurrent();
					add(job.getId());
					add("治療内容");
				}
			};
			val rsh = new BeanListHandler<_Record>(_Record.class);
			return JDBCUtils.query(sql, rsh, params);
		}
	}
}
