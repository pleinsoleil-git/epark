package app.takahashi.a00100.job.a00100.crawler.job.request.crawler.reserve;

import java.util.Collection;

import app.takahashi.a00100.job.a00100.crawler.job.Job;
import app.takahashi.a00100.job.a00100.crawler.job.request.Request;
import app.takahashi.a00100.job.a00100.crawler.job.request.crawler.WebData;
import common.jdbc.JDBCParameter;
import common.jdbc.JDBCUtils;
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
		}

		void saveClinic() throws Exception {
			String sql;
			sql = "WITH s_params AS\n"
				+ "(\n"
					+ "SELECT CAST( ? AS BIGINT ) AS job_id,\n"
						+ "CAST( ? AS VARCHAR ) AS catalog_id\n"
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
					+ "SELECT CAST( ? AS BIGINT ) AS job_id,\n"
						+ "CAST( ? AS VARCHAR ) AS catalog_id,\n"
						+ "CAST( ? AS VARCHAR ) AS title\n"
				+ ")\n"
				+ "INSERT INTO t_reserve_menu\n"
				+ "(\n"
					+ "foreign_id,\n"
					+ "title\n"
				+ ")\n"
				+ "SELECT t20.id,\n"
					+ "t10.title\n"
				+ "FROM s_params AS t10\n"
				+ "INNER JOIN t_clinic AS t20\n"
					+ "ON t20.foreign_id = t10.job_id\n"
					+ "AND t20.catalog_id = t10.catalog_id\n";
/*
			val job = Job.getCurrent();
			val conn = JDBCConnection.getCurrent().getConnection();

			try (val stmt = conn.prepareStatement(sql)) {
				for (val x : getServiceList()) {
					int colNum = 1;
					stmt.setLong(colNum++, job.getId());
					stmt.setString(colNum++, getCatalogId());
					stmt.setString(colNum++, x);
					stmt.addBatch();
				}

				stmt.executeBatch();
			}
*/
		}
	}
}
