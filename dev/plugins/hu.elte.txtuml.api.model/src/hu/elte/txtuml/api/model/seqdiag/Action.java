package hu.elte.txtuml.api.model.seqdiag;

import hu.elte.txtuml.api.model.AssociationEnd;
import hu.elte.txtuml.api.model.Interface;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.ModelClass.Port;
import hu.elte.txtuml.api.model.Signal;
import hu.elte.txtuml.api.model.ConnectorBase.ConnectorEnd;

public class Action {
	
	Action(){}
	
	public static <T extends ModelClass> T create(Class<T> classType, Object... parameters)
	{
		return hu.elte.txtuml.api.model.Action.create(classType, parameters);
	}

	public static void delete(ModelClass obj)
	{
		RuntimeContext context = (RuntimeContext)Thread.currentThread();
		InteractionWrapper wrapper = context.getInteractionWrapper();
		wrapper.findLifeline(obj).delete();
	}
	
	public static <C1 extends ConnectorEnd<?, P1>, P1 extends Port<I1, I2>, C2 extends ConnectorEnd<?, P2>, P2 extends Port<I2, I1>, I1 extends Interface, I2 extends Interface> void connect(
			Class<C1> leftEnd, P1 leftPort, Class<C2> rightEnd, P2 rightPort)
	{
		hu.elte.txtuml.api.model.Action.connect(leftEnd, leftPort, rightEnd, rightPort);
	}
	
	public static <P1 extends Port<I1, I2>, C extends ConnectorEnd<?, P2>, P2 extends Port<I1, I2>, I1 extends Interface, I2 extends Interface> void connect(
			P1 parentPort, Class<C> childEnd, P2 childPort)
	{
		hu.elte.txtuml.api.model.Action.connect(parentPort, childEnd, childPort);
	}
	
	public static <L extends ModelClass, R extends ModelClass> void link(Class<? extends AssociationEnd<L, ?>> leftEnd,
			L leftObj, Class<? extends AssociationEnd<R, ?>> rightEnd, R rightObj)
	{
		RuntimeContext context = (RuntimeContext)Thread.currentThread();
		InteractionWrapper wrapper = context.getInteractionWrapper();
		@SuppressWarnings("unchecked")
		LifelineWrapper<R> rightObjWrapper = (LifelineWrapper<R>) wrapper.findLifeline(rightObj);
		@SuppressWarnings("unchecked")
		LifelineWrapper<L> leftObjWrapper = (LifelineWrapper<L>) wrapper.findLifeline(leftObj);
		leftObjWrapper.link(leftEnd, rightEnd, rightObjWrapper);
	}
	
	public static <L extends ModelClass, R extends ModelClass> void unlink(
			Class<? extends AssociationEnd<L, ?>> leftEnd, L leftObj, Class<? extends AssociationEnd<R, ?>> rightEnd,
			R rightObj)
	{
		RuntimeContext context = (RuntimeContext)Thread.currentThread();
		InteractionWrapper wrapper = context.getInteractionWrapper();
		@SuppressWarnings("unchecked")
		LifelineWrapper<R> rightObjWrapper = (LifelineWrapper<R>) wrapper.findLifeline(rightObj);
		@SuppressWarnings("unchecked")
		LifelineWrapper<L> leftObjWrapper = (LifelineWrapper<L>) wrapper.findLifeline(leftObj);
		leftObjWrapper.unlink(leftEnd, rightEnd, rightObjWrapper);
	}
	
	public static void start(ModelClass obj)
	{
		RuntimeContext context = (RuntimeContext)Thread.currentThread();
		InteractionWrapper wrapper = context.getInteractionWrapper();
		wrapper.findLifeline(obj).start();
	}
	
	public static <S extends Signal> void send(ModelClass from,S signal, ModelClass target) {
		RuntimeContext context = RuntimeContext.getCurrentExecutorThread();
		InteractionWrapper wrapper = context.getInteractionWrapper();
		wrapper.storeMessage(from, signal, target);
		context.getTraceListener().addToPattern(from, signal, target);
	}
	
	public static void log(String message)
	{
		hu.elte.txtuml.api.model.Action.log(message);
	}
	
	public static void logError(String message)
	{
		hu.elte.txtuml.api.model.Action.logError(message);
	}
	
	/**
	 * @deprecated not needed since new version
	 * @param units units of time needed to process signal
	 */
	public static void duration(int units)
	{
		//TODO Code Action
	}
	
	/**
	 * @deprecated not needed since new version
	 * @param signal signal received
	 * @param from sender ModelClass
	 */
	public static void receive(Signal signal,ModelClass from)
	{
		//TODO code Action
	}
}
