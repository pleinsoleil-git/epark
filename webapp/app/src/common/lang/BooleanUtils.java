package common.lang;

public class BooleanUtils extends org.apache.commons.lang3.BooleanUtils {
	public static <T> Boolean valueOf(final T value) {
		if (StringUtils.isEmpty(value) == true) {
			return null;
		}

		return Boolean.valueOf(value.toString());
	}

	public static Boolean valueOf(final String value) {
		if (StringUtils.isEmpty(value) == true) {
			return null;
		}

		return Boolean.valueOf(value);
	}

	public static boolean equals(final Boolean val1, final Boolean val2) {
		return (val1 == null ? (val2 == null ? true : false) : (val2 == null ? false : val1.equals(val2)));
	}

	public static <T> Boolean isTrue(final T value) {
		return org.apache.commons.lang3.BooleanUtils.isTrue(valueOf(value));
	}

	public static <T> Boolean isFalse(final T value) {
		return org.apache.commons.lang3.BooleanUtils.isFalse(valueOf(value));
	}
}
