package hu.elte.txtuml.api.model;

import static java.util.Comparator.comparing;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import hu.elte.txtuml.utils.Logger;

/**
 * Base class for data types in the model.
 * 
 * <p>
 * <b>Represents:</b> a data type
 * <p>
 * <b>Usage:</b>
 * <p>
 * 
 * Inherit from this class to define data types of the model. Fields of the
 * subclass will represent attributes of the data type, while inheritance
 * between subclasses of <code>DataType</code> will represent extending data
 * types in the model. That means, due to the restrictions of Java, each data
 * type can extend at most one data type.
 * <p>
 * Data types are value types, their instances have no identity, therefore their
 * representing classes in Java must be immutable (all of their fields must be
 * {code final}).
 * <p>
 * The {@linkplain DataType} class provides an implementation for the
 * {@link #equals} and {@link #hashCode} methods which use reflection to
 * calculate their results based on the dynamic type and all the fields defined
 * in the dynamic type and all its superclasses.
 * 
 * <p>
 * <b>Java restrictions:</b>
 * <ul>
 * <li><i>Instantiate:</i> disallowed</li>
 * <li><i>Define subtype:</i> allowed
 * <p>
 * <b>Subtype requirements:</b>
 * <ul>
 * <li>must be a top level class (not a nested or local class)</li>
 * </ul>
 * <p>
 * <b>Subtype restrictions:</b>
 * <ul>
 * <li><i>Be abstract:</i> disallowed</li>
 * <li><i>Generic parameters:</i> disallowed</li>
 * <li><i>Constructors:</i> allowed, only with parameters of types which are
 * data types, model enums or primitives (including {@code String})</li>
 * <li><i>Initialization blocks:</i> allowed, containing only simple assignments
 * to set the default values of its fields</li>
 * <li><i>Fields:</i> allowed, only with parameters of types which are data
 * types, model enums or primitives (including {@code String} ); they represent
 * attributes of the data type</li>
 * <li><i>Methods:</i> allowed, only with parameters and return values of types
 * which are subclasses of <code>ModelClass</code>, signals, data types, model
 * enums or primitives (including {@code String}); they represent operations of
 * the data type</li>
 * <li><i>Nested interfaces:</i> disallowed</li>
 * <li><i>Nested classes:</i> disallowed</li>
 * <li><i>Nested enums:</i> disallowed</li>
 * </ul>
 * </li>
 * <li><i>Inherit from the defined subtype:</i> allowed, to represent extending
 * data types</li>
 * </ul>
 * 
 * <p>
 * <b>Example:</b>
 * 
 * <pre>
 * <code>
 * class Employee extends DataType {
 * 
 *  public Employee(String name, int id) {
 *    this.name = name;
 *    this.id = id;
 *  }
 * 
 * 	public final String name;
 * 
 * 	public final int id;
 *  
 * 	{@literal //...}
 *  
 * }
 * </code>
 * </pre>
 * 
 * See the documentation of {@link Model} for an overview on modeling in
 * JtxtUML.
 */
public abstract class DataType {

	@ExternalBody
	@Override
	public final boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		for (Field[] array : getAllFields()) {
			for (Field f : array) {
				f.setAccessible(true);
				try {
					if (!Objects.equals(f.get(this), f.get(obj))) {
						return false;
					}
				} catch (IllegalAccessException e) {
					Logger.sys.fatal("Data type field cannot be accessed", e);
				}
			}
		}
		return true;
	}

	@External
	@Override
	public final int hashCode() {
		final int prime = 1873;
		int result = 1;
		for (Field[] array : getAllFields()) {
			Arrays.sort(array, comparing(Field::getName));
			for (Field f : array) {
				f.setAccessible(true);
				try {
					Object obj = f.get(this);
					result = result * prime + Objects.hashCode(obj);
				} catch (IllegalAccessException e) {
					Logger.sys.fatal("Data type field cannot be accessed", e);
				}
			}
		}
		return result;
	}

	@Override
	public String toString() {
		return "data_type:" + getClass().getSimpleName();
	}

	private final List<Field[]> getAllFields() {
		List<Field[]> fields = new ArrayList<>();
		for (Class<?> cls = getClass(); cls != DataType.class; cls = cls.getSuperclass()) {
			fields.add(cls.getDeclaredFields());
		}
		return fields;
	}

}
