package common.lang;

import java.nio.charset.Charset;

public class StandardCharsets {
	public static final Charset US_ASCII = java.nio.charset.StandardCharsets.US_ASCII;
	public static final Charset ISO_8859_1 = java.nio.charset.StandardCharsets.ISO_8859_1;
	public static final Charset UTF_8 = java.nio.charset.StandardCharsets.UTF_8;
	public static final Charset UTF_16BE = java.nio.charset.StandardCharsets.UTF_16BE;
	public static final Charset UTF_16LE = java.nio.charset.StandardCharsets.UTF_16LE;
	public static final Charset UTF_16 = java.nio.charset.StandardCharsets.UTF_16;
	public static final Charset MS932 = Charset.forName("MS932");
	public static final Charset WINDOWS_31J = Charset.forName("Windows-31J");
	public static final Charset SHIFT_JIS = Charset.forName("Shift_JIS");
	public static final Charset UNICODE = Charset.forName("UNICODE");
	public static final Charset EUC_JP = Charset.forName("EUC-JP");
	public static final Charset ISO2022_JP = Charset.forName("ISO-2022-JP");
}
