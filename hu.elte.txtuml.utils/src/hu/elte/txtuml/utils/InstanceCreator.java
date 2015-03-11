package hu.elte.txtuml.utils;

import java.lang.reflect.*;
import java.util.HashSet;
import java.util.Set;

public class InstanceCreator {

	public static <T> T createInstance(Class<T> c) {
		return createInstanceRecursively(c, new HashSet<>());
	}

	@SuppressWarnings("unchecked")
	public static <T> T createInstanceWithGivenParams(Class<T> c,
			Object... givenParams) {
		Set<Class<?>> ancestors = new HashSet<>();
		T ret = null;
		int givenParamsLen = givenParams.length;
		Constructor<?>[] ctors = c.getDeclaredConstructors();
		for (Constructor<?> ctor : ctors) {
			ctor.setAccessible(true);
			Class<?>[] paramTypes = ctor.getParameterTypes();
			int len = paramTypes.length;
			if (len < givenParamsLen) {
				continue;
			}
			Object[] params = new Object[len];

			{
				int i;
				for (i = 0; i < givenParamsLen; ++i) {
					try {
						params[i] = paramTypes[i].cast(givenParams[i]);
					} catch (ClassCastException e) {
						break;
					}
				}
				if (i < givenParamsLen) { // one of the parameters could not be
											// casted
					continue;
				}
				for (; i < len; ++i) {
					Object param = createInstanceRecursively(paramTypes[i],
							ancestors);
					if (param == null) {
						break;
					}
					params[i] = param;
				}
				if (i < len) { // one of the parameters could not be created
					continue;
				}
			}

			try {
				ret = (T) ctor.newInstance(params);
			} catch (InstantiationException e) {
				break;
				// This type is abstract, so it is not instantiatable.
			} catch (Exception e) {
				// Using this ctor failed, 'ret' is null,
				// loop again if there are more ctors.
			}

			if (ret != null) {
				break;
			}
		}
		return ret;
	}

	@SuppressWarnings("unchecked")
	private static <T> T createInstanceRecursively(Class<T> c,
			Set<Class<?>> ancestors) {
		if (ancestors.contains(c)) {
			return null;
		}
		ancestors.add(c);

		T ret = null;
		Constructor<?>[] ctors = c.getDeclaredConstructors();
		for (Constructor<?> ctor : ctors) {
			ctor.setAccessible(true);
			Class<?>[] paramTypes = ctor.getParameterTypes();
			int len = paramTypes.length;
			Object[] params = new Object[len];

			{
				int i; // we use variable 'i' after the loop to check whether it
						// finished with 'break' command.
				for (i = 0; i < len; ++i) {
					Object param = createInstanceRecursively(paramTypes[i],
							ancestors);
					if (param == null) {
						break;
					}
					params[i] = param;
				}
				if (i < len) { // one of the parameters could not be created
					continue;
				}
			}

			try {
				ret = (T) ctor.newInstance(params);
			} catch (InstantiationException e) {
				break;
				// This type is abstract, so it is not instantiatable.
			} catch (Exception e) {
				// Using this ctor failed, 'ret' is null,
				// loop again if there are more ctors.
			}

			if (ret != null) {
				break;
			}
		}

		ancestors.remove(c);
		return ret;
	}

}
