package common.lang;

import lombok.val;

public class LongUtils {
	public static <T> Long valueOf(final T value) {
		if (StringUtils.isEmpty(value) == true) {
			return null;
		}

		return Long.valueOf(value.toString());
	}

	public static Long valueOf(final String value) {
		if (StringUtils.isEmpty(value) == true) {
			return null;
		}

		return Long.valueOf(value);
	}

	public static boolean equals(final Long val1, final Long val2) {
		return (val1 == null ? (val2 == null ? true : false) : (val2 == null ? false : val1.equals(val2)));
	}

	public static int compareTo(final Long val1, final Long val2) {
		val vals = new Long[] {
				val1 == null ? 0 : val1,
				val2 == null ? 0 : val2,
		};

		return vals[0].compareTo(vals[1]);
	}
}
