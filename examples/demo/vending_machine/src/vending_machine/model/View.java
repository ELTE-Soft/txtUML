package vending_machine.model;

import hu.elte.txtuml.api.model.External;
import hu.elte.txtuml.api.model.ExternalBody;
import hu.elte.txtuml.api.model.ModelClass;
import vending_machine.ui.UI;

public class View extends ModelClass {

	@External
	private volatile UI ui;

	private View() {
	}

	@External
	private static class SingletonHelper {
		private static final View INSTANCE = new View();
	}

	@ExternalBody
	public static View getInstance() {
		return SingletonHelper.INSTANCE;
	}

	@External
	public void setUI(UI ui) {
		this.ui = ui;
	}

	@ExternalBody
	public void showMessage(String msg) {
		ui.showMessage(msg);
	}

}
