package vending_machine.glue;

import vending_machine.ui.UI;

public class ViewImpl implements View {

	private volatile UI ui;

	private ViewImpl() {
	}

	private static class SingletonHelper {
		private static final ViewImpl INSTANCE = new ViewImpl();
	}

	public synchronized static ViewImpl getInstance() {
		return SingletonHelper.INSTANCE;
	}

	public void setUI(UI ui) {
		this.ui = ui;
	}

	@Override
	public void showMessage(String msg) {
		ui.showMessage(msg);
	}

}
