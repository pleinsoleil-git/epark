package common.app;

import common.app.bean.Bean;
import common.app.model.Model;
import common.jdbc.JDBCConnection;
import lombok.Getter;
import lombok.val;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Accessors(prefix = "m_", chain = false)
public abstract class App implements AutoCloseable {
	static final ThreadLocal<App> m_currents = new ThreadLocal<App>();

	@Getter
	final Bean m_bean;

	@Getter
	final Model m_model;

	JDBCConnection m_connection;

	public App(final Bean bean, final Model model) {
		m_bean = bean;
		m_model = model;
	}

	public static void setCurrent(final App app) {
		log.debug(String.format("Init app[thread=%d]", Thread.currentThread().getId()));
		m_currents.set(app);
	}

	public static App getCurrent() {
		return m_currents.get();
	}

	public abstract String getConfigFileName();

	public abstract String getDataSourceName();

	public JDBCConnection getConnection() {
		return (m_connection == null ? m_connection = new JDBCConnection(getDataSourceName()) : m_connection);
	}

	@Override
	public void close() throws Exception {
		try {
			log.debug(String.format("Close app[thread=%d]", Thread.currentThread().getId()));

			if (m_connection != null) {
				try (val x = m_connection) {
				} catch (Exception e) {
					log.error("", e);
				}
			}
		} finally {
			m_currents.remove();
		}
	}
}
