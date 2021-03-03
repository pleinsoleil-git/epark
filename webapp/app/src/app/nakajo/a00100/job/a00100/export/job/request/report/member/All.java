package app.nakajo.a00100.job.a00100.export.job.request.report.member;

import java.util.ArrayList;
import java.util.Collection;

import lombok.Data;
import lombok.val;
import lombok.experimental.Accessors;

@Accessors(prefix = "m_", chain = false)
class All {
	static All m_instance;
	_Current m_current;

	All() {
	}

	static All getInstance() {
		return (m_instance == null ? m_instance = new All() : m_instance);
	}

	static _Current getCurrent() {
		return getInstance().m_current;
	}

	void execute() throws Exception {
		try {
			for (val x : query()) {
				(m_current = x).execute();
			}
		} finally {
			m_instance = null;
		}
	}

	Collection<_Current> query() throws Exception {
		return new ArrayList<_Current>() {
			{
				add(new _Current());
			}
		};
	}

	@Data
	static class _Current {
		void execute() throws Exception {
		}
	}
}
