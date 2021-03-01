package app.takahashi.a00100.job.a00100.crawler.job.request.crawler.top;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import app.takahashi.a00100.job.a00100.crawler.job.Job;
import app.takahashi.a00100.job.a00100.crawler.job.request.Request;
import app.takahashi.a00100.job.a00100.crawler.job.request.crawler.WebData;
import common.jdbc.JDBCParameter;
import common.jdbc.JDBCUtils;
import common.lang.StringUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.val;
import lombok.experimental.Accessors;

@Accessors(prefix = "m_", chain = false)
class PageData implements AutoCloseable {
	static ThreadLocal<PageData> m_instances = new ThreadLocal<PageData>() {
		@Override
		protected PageData initialValue() {
			return new PageData();
		}
	};
	_Current m_current;

	PageData() {
	}

	static PageData getInstance() {
		return m_instances.get();
	}

	static _Current getCurrent() {
		val x = getInstance();
		return (x.m_current == null ? x.m_current = new _Current() : x.m_current);
	}

	@Override
	public void close() throws Exception {
		try {
			if (m_current != null) {
				val request = Request.getCurrent();
				request.getResults().add(m_current);
			}
		} finally {
			m_instances.remove();
		}
	}

	@Data
	@EqualsAndHashCode(callSuper = false)
	static class _Current extends WebData {
		String m_dentalName;
		String m_totalStar;
		String m_reviewCount;
		String m_netReserveType;
		Map<String, ArrayList<String>> m_menuList;

		String getCatalogId() {
			return Request.getCurrent().getCatalogId();
		}

		String getShopownerId() {
			return Request.getCurrent().getShopownerId();
		}

		Map<String, ArrayList<String>> getMenuList() {
			return (m_menuList == null ? m_menuList = new LinkedHashMap<String, ArrayList<String>>() : m_menuList);
		}

		@Override
		public void close() throws Exception {
			saveClinic();
			saveMenuList();
		}

		void saveClinic() throws Exception {
			String sql;
			sql = "WITH s_params AS\n"
				+ "(\n"
					+ "SELECT CAST( ? AS BIGINT ) AS job_id,\n"
						+ "CAST( ? AS VARCHAR ) AS catalog_id,\n"
						+ "CAST( ? AS VARCHAR ) AS dental_name,\n"
						+ "CAST( ? AS VARCHAR ) AS total_star,\n"
						+ "CAST( ? AS VARCHAR ) AS review_count,\n"
						+ "CAST( ? AS VARCHAR ) AS net_reserve_type\n"
				+ ")\n"
				+ "INSERT INTO t_clinic\n"
				+ "(\n"
					+ "foreign_id,\n"
					+ "catalog_id,\n"
					+ "dental_name,\n"
					+ "total_star,\n"
					+ "review_count,\n"
					+ "net_reserve_type\n"
				+ ")\n"
				+ "SELECT t10.job_id,\n"
					+ "t10.catalog_id,\n"
					+ "t10.dental_name,\n"
					+ "t10.total_star,\n"
					+ "t10.review_count,\n"
					+ "t10.net_reserve_type\n"
				+ "FROM s_params AS t10\n"
				+ "WHERE NOT EXISTS\n"
				+ "(\n"
					+ "SELECT NULL\n"
					+ "FROM t_clinic AS t900\n"
					+ "WHERE t900.foreign_id = t10.job_id\n"
					+ "AND t900.catalog_id = t10.catalog_id\n"
				+ ")\n";

			val params = new JDBCParameter() {
				{
					val job = Job.getCurrent();

					add(job.getId());
					add(getCatalogId());
					add(getDentalName());
					add(getTotalStar());
					add(getReviewCount());
					add(getNetReserveType());
				}
			};

			JDBCUtils.execute(sql, params);
		}

		void saveMenuList() throws Exception {
			for (val entry : getMenuList().entrySet()) {
				String sql;
				sql = "WITH s_params AS\n"
					+ "(\n"
						+ "SELECT ?::BIGINT AS job_id,\n"
							+ "?::VARCHAR AS catalog_id,\n"
							+ "?::VARCHAR AS menu,\n"
							+ StringUtils.join(new ArrayList<String>() {
								{
									val items = new ArrayList<String>() {
										{
											for (@SuppressWarnings("unused")val x : entry.getValue()) {
												add("?::VARCHAR");
											}
										}
									};

									if (items.isEmpty() == true) {
										add("NULL::VARCHAR[] AS title\n");
									} else {
										add("ARRAY\n");
										add("[\n");
										add(StringUtils.join(items.toArray(new String[0]), ",\n"));
										add("] AS title\n");
									}
								}
							}.toArray(new String[0]))
					+ "),\n"
					+ "i_menu AS\n"
					+ "(\n"
						+ "INSERT INTO t_top_menu\n"
						+ "(\n"
							+ "foreign_id,\n"
							+ "title,\n"
							+ "olded\n"
						+ ")\n"
						+ "SELECT t30.id,\n"
							+ "t20.menu,\n"
							+ "t10.olded\n"
						+ "FROM\n"
						+ "(\n"
							+ "SELECT CASE WHEN SUM( t10.old_title ) > 0 THEN TRUE\n"
										+ "WHEN SUM( t10.new_title ) > 0 THEN FALSE\n"
										+ "ELSE NULL\n"
									+ "END AS olded\n"
							+ "FROM\n"
							+ "(\n"
								+ "SELECT t10.title,\n"
									+ "SUM( CASE WHEN m10.olded = TRUE THEN 1 ELSE 0 END ) AS old_title,\n"
									+ "SUM( CASE WHEN m10.olded = FALSE THEN 1 ELSE 0 END ) AS new_title\n"
								+ "FROM\n"
								+ "(\n"
									+ "SELECT t10.menu,\n"
										+ "UNNEST( t10.title ) AS title\n"
									+ "FROM s_params AS t10\n"
								+ ") AS t10\n"
								+ "INNER JOIN m_top_menu_item AS m10\n"
									+ "ON m10.menu = t10.menu\n"
									+ "AND m10.title = t10.title\n"
								+ "GROUP BY 1\n"
							+ ") AS t10\n"
							+ "WHERE NOT ( t10.old_title > 0 AND t10.new_title > 0 )\n"
						+ ") AS t10\n"
						+ "CROSS JOIN s_params AS t20\n"
						+ "INNER JOIN t_clinic AS t30\n"
							+ "ON t30.foreign_id = t20.job_id\n"
							+ "AND t30.catalog_id = t20.catalog_id\n"
						+ "RETURNING id AS menu_id\n"
					+ ")\n"
					+ "SELECT t20.menu_id,\n"
						+ "UNNEST( t10.title )\n"
					+ "FROM s_params AS t10\n"
					+ "CROSS JOIN i_menu AS t20\n";

				val params = new JDBCParameter() {
					{
						val job = Job.getCurrent();

						add(job.getId());
						add(getCatalogId());
						add(entry.getKey());
						for (val x : entry.getValue()) {
							add(x);
						}
					}
				};

				JDBCUtils.execute(sql, params);
			}
		}
	}
}
