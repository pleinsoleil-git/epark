package common.lang;

import java.util.HashMap;

import lombok.val;

public class StringUtils extends org.apache.commons.lang3.StringUtils {
	public static <T> boolean isTrimEmpty(final T value) {
		return (value == null ? true : isEmpty(trim(value.toString())));
	}

	public static <T> boolean isNotTrimEmpty(final T value) {
		return (value == null ? false : isNotEmpty(trim(value.toString())));
	}

	public static boolean isNotEmpty(final String str) {
		return org.apache.commons.lang3.StringUtils.isNotEmpty(str);
	}

	public static <T> boolean isNotEmpty(final T value) {
		return (value == null ? false : isNotEmpty(value.toString()));
	}

	public static <T> boolean isEmpty(final T value) {
		return (value == null ? true : isEmpty(value.toString()));
	}

	public static String nullToEmpty(final String str) {
		return (str == null ? "" : str);
	}

	public static <T> String nullToEmpty(final T value) {
		return (value == null ? "" : value.toString());
	}

	public static String emptyToNull(final String str) {
		return (isEmpty(str) == true ? null : str);
	}

	public static <T> String emptyToNull(final T value) {
		return (value == null ? null : emptyToNull(value.toString()));
	}

	public static <T> String valueOf(final T value) {
		return (value == null ? null : value.toString());
	}

	public static String trim(final String str, final char trimChar) {
		if (str != null) {
			int len = str.length();
			int st = 0;
			char val[] = str.toCharArray();

			while ((st < len) && (val[st] == trimChar)) {
				st++;
			}

			while ((st < len) && (val[len - 1] == trimChar)) {
				len--;
			}

			return ((st > 0) || (len < str.length())) ? substring(str, st, len) : str;
		}

		return null;
	}

	public static String rtrim(final String str, final char trimChar) {
		if (str != null) {
			int len = str.length();
			int st = 0;
			char val[] = str.toCharArray();

			while ((st < len) && (val[len - 1] == trimChar)) {
				len--;
			}

			return ((st > 0) || (len < str.length())) ? substring(str, st, len) : str;
		}

		return null;
	}

	public static String Translate(final String str, final char s1[], final char s2[]) {
		val chars = new HashMap<String, String>() {
			{
				for (int i = 0; i < s1.length; i++) {
					put(String.valueOf(s1[i]), valueOf(s2[i]));
				}
			}
		};

		val sb = new StringBuilder();
		for (int i = 0; i < str.length(); i++) {
			val c1 = StringUtils.valueOf(str.charAt(i));
			val c2 = chars.get(c1);
			sb.append(c2 == null ? c1 : c2);
		}

		return sb.toString();
	}

	public static String trim(final String str) {
		if (isEmpty(str) == true) {
			return str;
		}

		int st = 0;
		int len = str.length();
		char[] val = str.toCharArray();
		while ((st < len) && ((val[st] <= ' ') || (val[st] == '　'))) {
			st++;
		}

		while ((st < len) && ((val[len - 1] <= ' ') || (val[len - 1] == '　'))) {
			len--;
		}

		return ((st > 0) || (len < str.length())) ? str.substring(st, len) : str;
	}

	public static String rtrim(final String str) {
		if (isEmpty(str) == true) {
			return str;
		}

		int st = 0;
		int len = str.length();
		char[] val = str.toCharArray();

		while ((st < len) && ((val[len - 1] <= ' ') || (val[len - 1] == '　'))) {
			len--;
		}

		return ((st > 0) || (len < str.length())) ? str.substring(st, len) : str;
	}

	public static String mid(final String str, final int pos) {
		if (isEmpty(str) == true) {
			return str;
		}

		val len = str.length();
		return org.apache.commons.lang3.StringUtils.mid(str, pos, len - pos);
	}
}
