package hu.elte.txtuml.utils;

import java.lang.reflect.Constructor;
import java.util.HashSet;
import java.util.Set;

/**
 * A utility class to create instances of classes specified by their
 * representing {@code Class<?>} objects.
 *
 * @author Gabor Ferenc Kovacs
 *
 */
public final class InstanceCreator {

	public static <T> T createInstance(Class<T> c) {
		return createInstanceRecursively(c, new HashSet<>());
	}

	@SuppressWarnings("unchecked")
	public static <T> T createInstanceWithGivenParams(Class<T> c,
			Object... givenParams) {
		if (c.isPrimitive()) {
			if (givenParams.length == 0) {
				return createPrimitiveInstance(c);
			} else {
				return null;
			}
		}
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

						if (paramTypes[i].isPrimitive()
								&& checkPrimitive(paramTypes[i],
										givenParams[i].getClass())) {
							params[i] = givenParams[i];
						} else {
							break;
						}

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
				// Using this constructor failed, 'ret' is null,
				// loop again if there are more constructors.
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
		if (c.isPrimitive()) {
			return createPrimitiveInstance(c);
		}
		ancestors.add(c);

		T ret = null;
		for (Constructor<?> ctor : c.getDeclaredConstructors()) {
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
				// Using this constructor failed, 'ret' is null,
				// loop again if there are more constructors.
			}

			if (ret != null) {
				break;
			}
		}

		ancestors.remove(c);
		return ret;
	}

	@SuppressWarnings("unchecked")
	private static <T> T createPrimitiveInstance(Class<?> c) {
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

	private static boolean checkPrimitive(Class<?> expected, Class<?> actual) {
		if (expected == boolean.class) {
			return actual == Boolean.class;
		} else if (expected == int.class) {
			return actual == Integer.class;
		} else if (expected == long.class) {
			return actual == Long.class;
		} else if (expected == double.class) {
			return actual == Double.class;
		} else if (expected == float.class) {
			return actual == Float.class;
		} else if (expected == short.class) {
			return actual == Short.class;
		} else if (expected == byte.class) {
			return actual == Byte.class;
		} else if (expected == char.class) {
			return actual == Character.class;
		}
		return false;
	}

}
