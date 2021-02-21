package common.lang;

import lombok.val;

public class DoubleUtils {
	public static <T> Double valueOf(final T value) {
		if (StringUtils.isEmpty(value) == true) {
			return null;
		}

		return Double.valueOf(value.toString());
	}

	public static Double valueOf(final String value) {
		if (StringUtils.isEmpty(value) == true) {
			return null;
		}

		return Double.valueOf(value);
	}

	public static boolean equals(final Double val1, final Double val2) {
		return (val1 == null ? (val2 == null ? true : false) : (val2 == null ? false : val1.equals(val2)));
	}

	public static int compareTo(final Double val1, final Double val2) {
		val vals = new Double[] {
				val1 == null ? 0 : val1,
				val2 == null ? 0 : val2,
		};

		return vals[0].compareTo(vals[1]);
	}
}
