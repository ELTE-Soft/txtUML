package txtuml.importer.utils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import txtuml.utils.InstanceCreator;

public class InterfaceMethodInvocationHandler implements InvocationHandler {

	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		Class<?> returnType=method.getReturnType();
		return InstanceCreator.createInstance(returnType);
	}

}
