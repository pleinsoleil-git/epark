package common.lang;

import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(prefix = "m_", chain = false)
public class UserException extends Exception {
	@Getter
	final String m_code;

	public UserException(final String msg) {
		this(null, msg);
	}

	public UserException(final String code, final String msg) {
		super(msg);
		m_code = code;
	}
}
