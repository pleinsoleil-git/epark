package app.nakajo.a00100.job.a00100.load.job.request.load.entry;

import java.util.ArrayList;
import java.util.Collection;

import lombok.Data;
import lombok.val;
import lombok.experimental.Accessors;

@Accessors(prefix = "m_", chain = false)
public class Entry {
	static Entry m_instance;
	_Current m_current;

	Entry() {
	}

	public static Entry getInstance() {
		return (m_instance == null ? m_instance = new Entry() : m_instance);
	}

	public static _Current getCurrent() {
		return getInstance().m_current;
	}

	public void execute() throws Exception {
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
	public static class _Current {
		public void execute() throws Exception {
			insert();
		}

		void insert() throws Exception {
		}
	}
}
