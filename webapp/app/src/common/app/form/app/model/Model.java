package common.app.form.app.model;

import common.app.App;
import common.app.form.app.action.Action;
import lombok.experimental.Accessors;

@Accessors(prefix = "m_", chain = false)
public class Model implements common.app.model.Model {
	public static Model getCurrent() {
		return (Model) App.getCurrent().getModel();
	}

	public void validate() throws Exception {
	}

	public String execute() throws Exception {
		return Action.SUCCESS;
	}
}
