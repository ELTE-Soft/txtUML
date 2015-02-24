package txtuml.api;

import txtuml.api.Association.*;
import txtuml.utils.InstanceCreator;

public abstract class Action implements ModelElement {

	protected Action() {
	}

	public static <T extends ModelClass> T create(Class<T> classType) {
		return InstanceCreator.createInstance(classType);
	}

	public static <MODELCLASS1 extends ModelClass, MODELCLASS2 extends ModelClass> void link(
			Class<? extends AssociationEnd<MODELCLASS1>> leftEnd,
			MODELCLASS1 leftObj,
			Class<? extends AssociationEnd<MODELCLASS2>> rightEnd,
			MODELCLASS2 rightObj) {

		leftObj.addToAssoc(rightEnd, rightObj);
		rightObj.addToAssoc(leftEnd, leftObj);
	}

	public static void start(ModelClass obj) {
		obj.start();
	}

	public static void send(ModelClass target, Signal signal) {
		target.send(signal);
	}

	public static void If(Condition cond, BlockBody thenBody, BlockBody elseBody) {

		if (cond.check().getValue()) {
			thenBody.run();
		} else {
			elseBody.run();
		}
	}

	public static void If(Condition cond, BlockBody thenBody) {
		If(cond, thenBody, () -> {
		});
	}

	public static void While(Condition cond, BlockBody body) {

		while (cond.check().getValue()) {
			body.run();
		}
	}

	public static <T extends ModelClass> void For(Collection<T> collection,
			ParameterizedBlockBody<T> body) {
		// TODO import 'For' (foreach) into UML2

		for (T element : collection) {
			body.run(element);
		}
	}

	public static void For(ModelInt begin, ModelInt end,
			ParameterizedBlockBody<ModelInt> body) {

		for (int i = begin.getValue(); i <= end.getValue(); ++i) {
			body.run(new ModelInt(i));
		}
	}

	public static void log(String message) { // user log
			ModelExecutor.log(message);
	}

	public static void logError(String message) { // user log
			ModelExecutor.logError(message);
	}

	static void executorLog(String message) { // api log
			ModelExecutor.executorLog(message);
	}

	static void executorFormattedLog(String format, Object... args) { // api log
			ModelExecutor.executorFormattedLog(format, args);
	}

	static void executorErrorLog(String message) { // api log
			ModelExecutor.executorErrorLog(message);
	}
}