package hu.elte.txtuml.export.uml2.transform.backend;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import hu.elte.txtuml.utils.InstanceCreator;

public class InterfaceMethodInvocationHandler implements InvocationHandler {

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		
		if(Object.class  == method.getDeclaringClass() && method.getName().equals("hashCode"))
		{
		      return System.identityHashCode(proxy); 
		}
		else
		{
			Class<?> returnType=method.getReturnType();
			return InstanceCreator.createInstance(returnType);
		}
	}

	
}