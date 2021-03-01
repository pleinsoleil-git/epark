package app.takahashi.a00100.job.a00100.crawler.job.request.crawler.reserve;

import java.util.ArrayList;
import java.util.Collection;

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
		Collection<String> m_serviceList;

		String getCatalogId() {
			return Request.getCurrent().getCatalogId();
		}

		@Override
		public void close() throws Exception {
			saveClinic();
			saveServiceList();
		}

		void saveClinic() throws Exception {
			String sql;
			sql = "WITH s_params AS\n"
				+ "(\n"
					+ "SELECT ?::BIGINT AS job_id,\n"
						+ "?::VARCHAR AS catalog_id\n"
				+ ")\n"
				+ "INSERT INTO t_clinic\n"
				+ "(\n"
					+ "foreign_id,\n"
					+ "catalog_id\n"
				+ ")\n"
				+ "SELECT t10.job_id,\n"
					+ "t10.catalog_id\n"
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
				}
			};

			JDBCUtils.execute(sql, params);
		}

		void saveServiceList() throws Exception {
			String sql;
			sql = "WITH s_params AS\n"
				+ "(\n"
					+ "SELECT ?::BIGINT AS job_id,\n"
						+ "?::VARCHAR AS catalog_id,\n"
						+ StringUtils.join(new ArrayList<String>() {
							{
								val items = new ArrayList<String>() {
									{
										for (@SuppressWarnings("unused")val x : getServiceList()) {
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
					+ ")\n"
				+ "INSERT INTO t_reserve_menu\n"
				+ "(\n"
					+ "foreign_id,\n"
					+ "title\n"
				+ ")\n"
				+ "SELECT DISTINCT t10.id,\n"
					+ "t10.title\n"
				+ "FROM\n"
				+ "(\n"
					+ "SELECT t20.id,\n"
						+ "UNNEST( t10.title ) AS title\n"
					+ "FROM s_params AS t10\n"
					+ "INNER JOIN t_clinic AS t20\n"
						+ "ON t20.foreign_id = t10.job_id\n"
						+ "AND t20.catalog_id = t10.catalog_id\n"
				+ ") AS t10\n";

			val params = new JDBCParameter() {
				{
					val job = Job.getCurrent();

					add(job.getId());
					add(getCatalogId());

					for (val x : getServiceList()) {
						add(x);
					}
				}
			};

			JDBCUtils.execute(sql, params);
		}
	}
}
