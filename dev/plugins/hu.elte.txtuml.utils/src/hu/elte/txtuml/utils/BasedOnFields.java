package hu.elte.txtuml.utils;

import static java.util.Comparator.comparing;

import java.lang.reflect.Field;
import static java.lang.reflect.Modifier.*;
import java.util.Arrays;
import java.util.Objects;

public final class BasedOnFields {

	private BasedOnFields() {
	}

	/**
	 * Checks equality based on the fields of the two objects. Returns true if
	 * they are the same; false if one them is null or their dynamic type is not
	 * the same; checks the fields otherwise. Also takes fields of superclasses
	 * into consideration.
	 * <p>
	 * Compares field values with {@link Objects#equals(Object, Object)}.
	 * 
	 * @throws IllegalAccessException
	 *             if one of the fields cannot be accessed even after
	 *             {@link Field#setAccessible(boolean)} called with true
	 */
	public static boolean equal(Object obj1, Object obj2) throws IllegalAccessException {
		if (obj1 == obj2) {
			return true;
		}

		if (obj1 == null || obj2 == null) {
			// One of them is null and they are not the same.
			return false;
		}

		Class<?> cls = obj1.getClass();

		if (cls != obj2.getClass()) {
			return false;
		}

		for (; cls != Object.class; cls = cls.getSuperclass()) {
			for (Field f : cls.getDeclaredFields()) {
				if (isStatic(f.getModifiers())) {
					continue;
				}

				f.setAccessible(true);
				if (!Objects.equals(f.get(obj1), f.get(obj2))) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Returns a hashCode based on the fields of the object. Returns 0 if the
	 * parameter is null; checks the fields otherwise. Also takes fields of
	 * superclasses into consideration.
	 * <p>
	 * Gets a hashCode for the field values with
	 * {@link Objects#hashCode(Object)}.
	 * 
	 * @throws IllegalAccessException
	 *             if one of the fields cannot be accessed even after
	 *             {@link Field#setAccessible(boolean)} called with true
	 */
	public static int hashCode(Object obj) throws IllegalAccessException {
		if (obj == null) {
			return 0;
		}

		Class<?> cls = obj.getClass();

		final int prime = 1873;
		int result = 1;
		for (; cls != Object.class; cls = cls.getSuperclass()) {
			Field[] array = cls.getDeclaredFields();
			Arrays.sort(array, comparing(Field::getName));
			for (Field f : array) {
				if (isStatic(f.getModifiers())) {
					continue;
				}

				f.setAccessible(true);
				Object fieldValue = f.get(obj);
				result = result * prime + Objects.hashCode(fieldValue);
			}
		}
		return result;
	}

}
