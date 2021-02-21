package common.app.form.app.action;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.SessionAware;

import com.google.common.collect.Iterables;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.config.entities.Parameterizable;

import common.app.App;
import common.app.form.app.bean.Bean;
import common.app.form.app.model.Model;
import lombok.Getter;
import lombok.val;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Accessors(prefix = "m_", chain = false)
public abstract class Action extends ActionSupport implements Parameterizable, SessionAware {
	@Getter
	final Bean m_bean;

	@Getter
	Map<String, Object> m_session;

	Map<String, String> m_params;

	public Action(final App app) {
		// インスタンス設定
		App.setCurrent(app);

		m_bean = (Bean) app.getBean();
	}

	@Override
	public void addParam(final String name, final String value) {
		getParams().put(name, value);
	}

	@Override
	public void setParams(final Map<String, String> params) {
		getParams().putAll(params);
	}

	@Override
	public Map<String, String> getParams() {
		return (m_params == null ? m_params = new HashMap<>() : m_params);
	}

	@Override
	public void setSession(final Map<String, Object> session) {
		m_session = session;
	}

	public static Action getCurrent() {
		val ctx = ActionContext.getContext();
		if (ctx == null) {
			return null;
		}

		val proxy = ctx.getActionInvocation().getProxy();
		if (proxy == null) {
			return null;
		}

		val action = proxy.getAction();
		if (action == null) {
			return null;
		}

		return (action instanceof Action ? (Action) action : null);
	}

	public static HttpServletRequest getServletRequest() {
		return ServletActionContext.getRequest();
	}

	public static HttpServletResponse getServletResponse() {
		return ServletActionContext.getResponse();
	}

	public String getActionError() {
		return Iterables.getFirst(getActionErrors(), null);
	}

	public String getActionMessage() {
		return Iterables.getFirst(getActionMessages(), null);
	}

	@Override
	public void validate() {
		log.debug("Action validate");

		try {
			Model.getCurrent().validate();
		} catch (Exception e) {
			print(e);
		} finally {
			if (hasErrors() == true) {
				// --------------------------------------------------
				// エラーが発生したため終了
				// --------------------------------------------------
				try (val app = App.getCurrent()) {
				} catch (Exception e) {
					print(e);
				}
			}
		}
	}

	@Override
	public String execute() {
		log.debug("Action execute");

		try (val app = App.getCurrent()) {
			if (login() == false) {
				return LOGIN;
			}

			return Model.getCurrent().execute();
		} catch (Exception e) {
			print(e);
		}

		return ERROR;
	}

	public abstract boolean login() throws Exception;

	void print(final Exception e) {
		addActionError(e.getMessage());

		if (e instanceof SQLException) {
			print((SQLException) e);
		} else {
			log.error("", e);
		}
	}

	void print(final SQLException e) {
		for (SQLException x = e; x != null;) {
			log.error("", x);
			x = x.getNextException();
		}
	}
}
