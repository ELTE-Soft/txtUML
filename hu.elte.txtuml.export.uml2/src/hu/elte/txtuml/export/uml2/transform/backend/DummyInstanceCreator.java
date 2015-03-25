package hu.elte.txtuml.export.uml2.transform.backend;

import hu.elte.txtuml.utils.InstanceCreator;

public final class DummyInstanceCreator {

	private static boolean creating=false;
	private static InterfaceMethodInvocationHandler handler = new InterfaceMethodInvocationHandler();
	
	public static boolean isCreating()
	{
		return creating;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T createDummyInstance(Class<T> typeClass)
	{
		creating = true;
		T createdObject;
		
		if(typeClass.isInterface())
		{
			createdObject=(T) java.lang.reflect.Proxy.newProxyInstance(
					typeClass.getClassLoader(), 
					new java.lang.Class[] { typeClass},
					handler
					);
		}
		else
			createdObject=InstanceCreator.createInstance(typeClass);	

		creating=false;
		return createdObject;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T createDummyInstance(Class<T> typeClass, Object owner)
	{
		
		creating=true;
		T createdObject;
		if(typeClass.isInterface())
		{
			createdObject=(T) java.lang.reflect.Proxy.newProxyInstance(
					typeClass.getClassLoader(), 
					new java.lang.Class[] { typeClass},
					handler
					);
		}
		else
			createdObject=InstanceCreator.createInstanceWithGivenParams(typeClass,owner);	

		creating = false;
		return createdObject;
	}

}
