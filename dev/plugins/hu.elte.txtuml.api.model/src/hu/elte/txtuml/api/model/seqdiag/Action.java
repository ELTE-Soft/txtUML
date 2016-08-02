package hu.elte.txtuml.api.model.seqdiag;

import hu.elte.txtuml.api.model.AssociationEnd;
import hu.elte.txtuml.api.model.Interface;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.ModelClass.Port;
import hu.elte.txtuml.api.model.Signal;
import hu.elte.txtuml.api.model.ConnectorBase.ConnectorEnd;

public class Action {
	
	Action(){}
	
	public static <T extends ModelClass> Lifeline<T> create(Class<T> classType, Object... parameters)
	{
		T instance = hu.elte.txtuml.api.model.Action.create(classType, parameters);
		hu.elte.txtuml.api.model.Action.start(instance);
		return new Lifeline<T>(instance);
	}
	
	public static void delete(ModelClass obj)
	{
		hu.elte.txtuml.api.model.Action.delete(obj);
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
		hu.elte.txtuml.api.model.Action.link(leftEnd, leftObj, rightEnd, rightObj);
	}
	
	public static <L extends ModelClass, R extends ModelClass> void unlink(
			Class<? extends AssociationEnd<L, ?>> leftEnd, L leftObj, Class<? extends AssociationEnd<R, ?>> rightEnd,
			R rightObj)
	{
		hu.elte.txtuml.api.model.Action.unlink(leftEnd, leftObj, rightEnd, rightObj);
	}
	
	public static void start(ModelClass obj)
	{
		hu.elte.txtuml.api.model.Action.start(obj);
	}
	
	public static <S extends Signal> void send(Lifeline<?> from,S signal, Lifeline<?> target) {
		
		hu.elte.txtuml.api.model.Action.send(signal, target.getInstance());
	}
	
	public static void log(String message)
	{
		hu.elte.txtuml.api.model.Action.log(message);
	}
	
	public static void logError(String message)
	{
		hu.elte.txtuml.api.model.Action.logError(message);
	}
	
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
