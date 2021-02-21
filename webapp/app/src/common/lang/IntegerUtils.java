package common.lang;

import lombok.val;

public class IntegerUtils {
	public static <T> Integer valueOf(final T value) {
		if (StringUtils.isEmpty(value) == true) {
			return null;
		}

		return Integer.valueOf(value.toString());
	}

	public static Integer valueOf(final String value) {
		if (StringUtils.isEmpty(value) == true) {
			return null;
		}

		return Integer.valueOf(value);
	}

	public static boolean equals(final Integer val1, final Integer val2) {
		return (val1 == null ? (val2 == null ? true : false) : (val2 == null ? false : val1.equals(val2)));
	}

	public static int compareTo(final Integer val1, final Integer val2) {
		val vals = new Integer[] {
				val1 == null ? 0 : val1,
				val2 == null ? 0 : val2,
		};

		return vals[0].compareTo(vals[1]);
	}
}
