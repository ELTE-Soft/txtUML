package hu.elte.txtuml.export.uml2.utils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import hu.elte.txtuml.utils.InstanceCreator;

public class InterfaceMethodInvocationHandler implements InvocationHandler {

	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		Class<?> returnType=method.getReturnType();
		return InstanceCreator.createInstance(returnType);
	}

}