package hu.elte.txtuml.export.uml2.transform.backend;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * An InvocationHandler for handling method invocation of mocked interfaces
 * (Java Reflection proxies) during model import.
 * @author Ádám Ancsin
 *
 */
public final class InterfaceMethodInvocationHandler implements InvocationHandler 
{
	/**
	 * Provides return value for methods of mocked interfaces (proxies).
	 * If the method is "hashCode", the return value is the identityHashCode of the object (the proxy)
	 * Otherwise, the return value is a dummy instance of the return type.
	 * 
	 * @param proxy The proxy. (the mock instance of the interface)
	 * @param method The invoked method.
	 * @param args The current arguments of the method invoked.
	 * 
	 * @return Dummy return value for the method.
	 * 
	 * @author Ádám Ancsin
	 */
	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable 
	{	
		if(Object.class  == method.getDeclaringClass() && method.getName().equals("hashCode"))
		      return System.identityHashCode(proxy); 
		else
			return DummyInstanceCreator.createDummyInstance(method.getReturnType());
	}
}