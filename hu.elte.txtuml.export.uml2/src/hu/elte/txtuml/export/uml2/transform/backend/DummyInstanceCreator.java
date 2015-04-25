package hu.elte.txtuml.export.uml2.transform.backend;

import hu.elte.txtuml.utils.InstanceCreator;

/**
 * This class provides means for creating dummy instances.
 * @author Adam Ancsin
 *
 */
public final class DummyInstanceCreator {

	/**
	 * Indicates whether dummy instance creation is in progress.
	 */
	private static boolean creating=false;
	
	/**
	 * The interface method invocation handler. Necessary for mocking interfaces.
	 */
	private static InterfaceMethodInvocationHandler handler = new InterfaceMethodInvocationHandler();
	
	/**
	 * Decides if dummy instance creation is in progress.
	 * @return The decision.
	 *
	 * @author Adam Ancsin
	 */
	public static boolean isCreating()
	{
		return creating;
	}
	
	/**
	 * Creates a dummy instance of the specified type.
	 * @param typeClass The class of the specified type.
	 * @return The created dummy instance.
	 *
	 * @author Adam Ancsin
	 */
	@SuppressWarnings("unchecked")
	public static <T> T createDummyInstance(Class<T> typeClass)
	{
		creating = true;
		T createdObject;
		
		//if the type is an interface, creates a mock using the "handler" InvocationHandler
		if(typeClass.isInterface())
		{
			createdObject=(T) java.lang.reflect.Proxy.newProxyInstance(
					typeClass.getClassLoader(), 
					new java.lang.Class[] { typeClass},
					handler
					);
		}
		//if the type is not an interface, uses InstanceCreator to create the instance
		else
			createdObject=InstanceCreator.createInstance(typeClass);	

		creating=false;
		return createdObject;
	}
	
	/**
	 * Creates a dummy instance of the specified type with the given owner instance.
	 * @param typeClass The class of the specified type.
	 * @param owner The owner instance.
	 * @return The created dummy instance.
	 *
	 * @author Adam Ancsin
	 */
	@SuppressWarnings("unchecked")
	public static <T> T createDummyInstance(Class<T> typeClass, Object owner)
	{
		
		creating=true;
		T createdObject;
		
		//if the type is an interface, creates a mock using the "handler" InvocationHandler
		if(typeClass.isInterface())
		{
			createdObject=(T) java.lang.reflect.Proxy.newProxyInstance(
					typeClass.getClassLoader(), 
					new java.lang.Class[] { typeClass},
					handler
					);
		}
		//if the type is not an interface, uses InstanceCreator to create the instance
		else
			createdObject=InstanceCreator.createInstanceWithGivenParams(typeClass,owner);	

		creating = false;
		return createdObject;
	}

}
