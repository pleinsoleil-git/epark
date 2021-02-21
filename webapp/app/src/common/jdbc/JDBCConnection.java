package common.jdbc;

import java.sql.Connection;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JDBCConnection implements AutoCloseable {
	final String m_dataSourceName;
	Connection m_connection;

	public JDBCConnection(final String dataSourceName) {
		m_dataSourceName = dataSourceName;
	}

	public Connection getConnection() throws Exception {
		if (m_connection == null) {
			log.debug(String.format("JDBC connection open[%s]", m_dataSourceName));
			val icx = new InitialContext();

			try {
				val dataSource = (DataSource) icx.lookup("java:comp/env/" + m_dataSourceName);
				m_connection = dataSource.getConnection();
				m_connection.setAutoCommit(false);
			} finally {
				icx.close();
			}
		}

		return m_connection;
	}

	public JDBCStatement createStatement() throws Exception {
		return new JDBCStatement(getConnection());
	}

	public void commit() throws Exception {
		getConnection().commit();
	}

	public void rollback() throws Exception {
		getConnection().rollback();
	}

	@Override
	public void close() throws Exception {
		if (m_connection != null) {
			try (val x = m_connection) {
				log.debug(String.format("JDBC connection close[%s]", m_dataSourceName));
			}
		}
	}
}
