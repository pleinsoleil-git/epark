package common.jdbc;

import java.sql.Connection;
import java.util.Collection;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.ArrayListHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import common.app.App;

public class JDBCUtils {
	public static JDBCConnection getConnection() throws Exception {
		return App.getCurrent().getConnection();
	}

	public static JDBCStatement createStatement(final Connection conn) throws Exception {
		return new JDBCStatement(conn);
	}

	public static JDBCStatement createStatement(final JDBCConnection conn) throws Exception {
		return new JDBCStatement(conn.getConnection());
	}

	public static JDBCStatement createStatement() throws Exception {
		return createStatement(getConnection());
	}

	public static void commit(final Connection conn) throws Exception {
		conn.commit();
	}

	public static void commit(final JDBCConnection conn) throws Exception {
		commit(conn.getConnection());
	}

	public static void commit() throws Exception {
		commit(getConnection());
	}

	public static void rollback(final Connection conn) throws Exception {
		conn.rollback();
	}

	public static void rollback(final JDBCConnection conn) throws Exception {
		rollback(conn.getConnection());
	}

	public static void rollback() throws Exception {
		rollback(getConnection());
	}

	public static List<Object[]> query(final Connection conn, final String sql, final ArrayListHandler rsh)
			throws Exception {
		return (new QueryRunner()).query(conn, sql, rsh);
	}

	public static List<Object[]> query(final JDBCConnection conn, final String sql, final ArrayListHandler rsh)
			throws Exception {
		return query(conn.getConnection(), sql, rsh);
	}

	public static List<Object[]> query(final String sql, final ArrayListHandler rsh) throws Exception {
		return query(getConnection(), sql, rsh);
	}

	public static List<Object[]> query(final Connection conn, final String sql, final ArrayListHandler rsh, final Collection<Object> params)
			throws Exception {
		return (new QueryRunner()).query(conn, sql, rsh, params.toArray());
	}

	public static List<Object[]> query(final JDBCConnection conn, final String sql, final ArrayListHandler rsh, final Collection<Object> params)
			throws Exception {
		return query(conn.getConnection(), sql, rsh, params);
	}

	public static List<Object[]> query(final String sql, final ArrayListHandler rsh, final Collection<Object> params)
			throws Exception {
		return query(getConnection(), sql, rsh, params);
	}

	public static <T> List<T> query(final Connection conn, final String sql, final BeanListHandler<T> rsh)
			throws Exception {
		return (new QueryRunner()).query(conn, sql, rsh);
	}

	public static <T> List<T> query(final JDBCConnection conn, final String sql, final BeanListHandler<T> rsh)
			throws Exception {
		return query(conn.getConnection(), sql, rsh);
	}

	public static <T> List<T> query(final String sql, final BeanListHandler<T> rsh) throws Exception {
		return query(getConnection(), sql, rsh);
	}

	public static <T> List<T> query(final Connection conn, final String sql, final BeanListHandler<T> rsh, final Collection<Object> params)
			throws Exception {
		return (new QueryRunner()).query(conn, sql, rsh, params.toArray());
	}

	public static <T> List<T> query(final JDBCConnection conn, final String sql, final BeanListHandler<T> rsh, final Collection<Object> params)
			throws Exception {
		return query(conn.getConnection(), sql, rsh, params);
	}

	public static <T> List<T> query(final String sql, final BeanListHandler<T> rsh, final Collection<Object> params) throws Exception {
		return query(getConnection(), sql, rsh, params);
	}

	public static <T> T query(final Connection conn, final String sql, final ResultSetHandler<T> rsh) throws Exception {
		return (new QueryRunner()).query(conn, sql, rsh);
	}

	public static <T> T query(final JDBCConnection conn, final String sql, final ResultSetHandler<T> rsh) throws Exception {
		return query(conn.getConnection(), sql, rsh);
	}

	public static <T> T query(final String sql, final ResultSetHandler<T> rsh) throws Exception {
		return query(getConnection(), sql, rsh);
	}

	public static <T> T query(final Connection conn, final String sql, final ResultSetHandler<T> rsh, final Collection<Object> params)
			throws Exception {
		return (new QueryRunner()).query(conn, sql, rsh, params.toArray());
	}

	public static <T> T query(final JDBCConnection conn, final String sql, final ResultSetHandler<T> rsh, final Collection<Object> params)
			throws Exception {
		return query(conn.getConnection(), sql, rsh, params);
	}

	public static <T> T query(final String sql, final ResultSetHandler<T> rsh, final Collection<Object> params) throws Exception {
		return query(getConnection(), sql, rsh, params);
	}

	public static int execute(final Connection conn, final String sql) throws Exception {
		return (new QueryRunner()).update(conn, sql);
	}

	public static int execute(final JDBCConnection conn, final String sql) throws Exception {
		return execute(conn.getConnection(), sql);
	}

	public static int execute(final String sql) throws Exception {
		return execute(getConnection(), sql);
	}

	public static int execute(final Connection conn, final String sql, final Collection<Object> params) throws Exception {
		return (new QueryRunner()).update(conn, sql, params.toArray());
	}

	public static int execute(final JDBCConnection conn, final String sql, final Collection<Object> params) throws Exception {
		return execute(getConnection(), sql, params);
	}

	public static void truncateTable(final Connection conn, final String tableName, final boolean cascade) throws Exception {
		String sql = String.format("TRUNCATE TABLE %s\n", tableName);
		if (cascade == true) {
			sql = String.format("%s CASCADE\n", sql);
		}

		execute(conn, sql);
	}

	public static void truncateTable(final JDBCConnection conn, final String tableName, final boolean cascade) throws Exception {
		truncateTable(conn.getConnection(), tableName, cascade);
	}

	public static void truncateTable(final Connection conn, final String tableName) throws Exception {
		truncateTable(conn, tableName, false);
	}

	public static void truncateTable(final JDBCConnection conn, final String tableName) throws Exception {
		truncateTable(conn, tableName, false);
	}

	public static void truncateTable(final String tableName, final boolean cascade) throws Exception {
		truncateTable(getConnection(), tableName, cascade);
	}

	public static void truncateTable(final String tableName) throws Exception {
		truncateTable(tableName, false);
	}
}
