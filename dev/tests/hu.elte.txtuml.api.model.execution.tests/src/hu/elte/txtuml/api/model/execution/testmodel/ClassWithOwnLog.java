package hu.elte.txtuml.api.model.execution.testmodel;

import java.util.ArrayList;
import java.util.List;

import hu.elte.txtuml.api.model.External;
import hu.elte.txtuml.api.model.ExternalBody;
import hu.elte.txtuml.api.model.ModelClass;

public class ClassWithOwnLog extends ModelClass {
	/**
	 * This is a log owned by this class which is extended when the appropriate
	 * entry and exit actions are performed.
	 */
	@External
	public final List<String> ownLog = new ArrayList<>();

	@ExternalBody
	protected void addToOwnLog(String msg) {
		ownLog.add(msg);
	}
}
