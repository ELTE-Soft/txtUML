package hu.elte.txtuml.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;

/**
 * A utility class to create instances of classes specified by their
 * representing {@code Class<?>} objects.
 * <p>
 * Can also create a primitive type instance.
 */
public final class InstanceCreator {

	/**
	 * Creates instance of a class or primitive type specified by its
	 * representing {@code Class<?>} object. If multiple constructors are
	 * applicable there is no guarantee which one will be used. Will throw an
	 * exception if no constructors can be applied.
	 */
	public static <T> T create(Class<T> toInstantiate,
			Object... constructorParams) {
		if (toInstantiate.isPrimitive()) {
			if (constructorParams.length == 0) {
				return getDefaultPrimitiveValue(toInstantiate);
			} else {
				throw new RuntimeException(
						"Primitive values cannot be instantiated with parameters");
			}
		}
		T ret = null;

		// contains the default constructor if no constructor is defined
		Constructor<?>[] ctors = toInstantiate.getDeclaredConstructors();
		for (Constructor<?> ctor : ctors) {
			ret = tryCreateWithConstructor(ctor, constructorParams);
			if (ret != null) {
				return ret;
			}
		}
		throw new RuntimeException(
				"No constructors could be applied to create an instance of "
						+ toInstantiate);
	}

	/**
	 * Tries to create the instance using the given constructor. Omits the
	 * implicit parameters. Checks are left for the newInstance call.
	 */
	@SuppressWarnings("unchecked")
	private static <T> T tryCreateWithConstructor(Constructor<?> ctor,
			Object... givenParams) {
		Parameter[] params = ctor.getParameters();
		Object[] actualParams = new Object[params.length];

		int givenParamInd = 0;
		for (int i = 0; i < params.length && givenParamInd < givenParams.length; ++i) {
			if (!params[i].isImplicit() && !params[i].isSynthetic()) {
				actualParams[i] = givenParams[givenParamInd++];
			}
		}

		// more parameters were given than how many this constructor has
		if (givenParamInd < givenParams.length) {
			return null;
		}

		ctor.setAccessible(true);

		try {
			return (T) ctor.newInstance(actualParams);
		} catch (InvocationTargetException e) {
			// exception raised by the constructor
			throw new RuntimeException("Error while calling constructor", e);
		} catch (IllegalArgumentException | InstantiationException
				| IllegalAccessException e) {
			// constructor is not applicable, null will be returned
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private static <T> T getDefaultPrimitiveValue(Class<?> c) {
		// The 8 primitive types of Java, with the more common ones in front.
		if (c == boolean.class) {
			return (T) new Boolean(false);
		} else if (c == int.class) {
			return (T) new Integer(0);
		} else if (c == long.class) {
			return (T) new Long(0L);
		} else if (c == double.class) {
			return (T) new Double(0.0d);
		} else if (c == float.class) {
			return (T) new Float(0.0f);
		} else if (c == short.class) {
			return (T) new Short((short) 0);
		} else if (c == byte.class) {
			return (T) new Byte((byte) 0);
		} else if (c == char.class) {
			return (T) new Character('\u0000');
		}
		return null;
	}

}
