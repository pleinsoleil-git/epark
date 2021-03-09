package app.nakajo.a00100.job.a00100.load.job.request.load;

import java.util.ArrayList;
import java.util.Collection;

import app.nakajo.a00100.job.a00100.load.job.request.Request;
import app.nakajo.a00100.job.a00100.load.job.request.RequestType;
import app.nakajo.a00100.job.a00100.load.job.request.load.usage.Usage;
import lombok.Data;
import lombok.val;
import lombok.experimental.Accessors;

@Accessors(prefix = "m_", chain = false)
public class Load {
	static Load m_instance;
	_Current m_current;

	Load() {
	}

	public static Load getInstance() {
		return (m_instance == null ? m_instance = new Load() : m_instance);
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
			switch (RequestType.valueOf(Request.getCurrent().getRequestType())) {
			case USAGE:
				usage();
				break;
			case REQUEST:
				request();
				break;
			default:
				throw new IllegalArgumentException();
			}
		}

		void usage() throws Exception {
			Usage.getInstance().execute();
		}

		void request() throws Exception {
			app.nakajo.a00100.job.a00100.load.job.request.load.request.Request.getInstance().execute();
		}
	}
}
