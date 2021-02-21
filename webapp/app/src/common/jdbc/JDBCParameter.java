package common.jdbc;

import java.util.ArrayList;

import common.lang.StringUtils;

public class JDBCParameter extends ArrayList<Object> {
	Object trim(final Object value) {
		if (value instanceof String) {
			return StringUtils.trimToNull((String) value);
		}

		return value;
	}

	@Override
	public boolean add(final Object value) {
		return add(value, true);
	}

	public boolean add(final Object value, final boolean trim) {
		return super.add(trim == true ? trim(value) : value);
	}
}
