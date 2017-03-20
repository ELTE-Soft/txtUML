package vending_machine.glue;

import hu.elte.txtuml.api.model.external.ExternalClass;

public interface View extends ExternalClass {

	public static View getInstance() {
		return ViewImpl.getInstance();
	}

	void showMessage(String msg);

}
