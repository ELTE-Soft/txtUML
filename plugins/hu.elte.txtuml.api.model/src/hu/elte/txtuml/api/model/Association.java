package hu.elte.txtuml.api.model;

/**
 * A base class for associations in the model.
 * 
 * <p>
 * <b>Represents:</b> association
 * <p>
 * <b>Usage:</b>
 * <p>
 * 
 * An association in the model is a subclass of <code>Association</code>, having
 * two inner classes which both extend {@link AssociationEnd}. These two inner
 * classes will represent the two ends of this association. Their navigability
 * and multiplicity depend on which predefined subclass of
 * <code>AssociationEnd</code> is extended ({@code AssociationEnd} itself may
 * not be extended).
 * <p>
 * The two model classes which the association connects are defined by the two
 * association ends' generic parameters.
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
 * <li>must have two inner classes which are subclasses of
 * <code>AssociationEnd</code></li>
 * </ul>
 * <p>
 * <b>Subtype restrictions:</b>
 * <ul>
 * <li><i>Be abstract:</i> disallowed</li>
 * <li><i>Generic parameters:</i> disallowed</li>
 * <li><i>Constructors:</i> disallowed</li>
 * <li><i>Initialization blocks:</i> disallowed</li>
 * <li><i>Fields:</i> disallowed</li>
 * <li><i>Methods:</i> disallowed</li>
 * <li><i>Nested interfaces:</i> disallowed</li>
 * <li><i>Nested classes:</i> allowed at most two, both of which are non-static
 * and are subclasses of <code>AssociationEnd</code></li>
 * <li><i>Nested enums:</i> disallowed</li>
 * </ul>
 * </li>
 * <li><i>Inherit from the defined subtype:</i> disallowed</li>
 * </ul>
 * 
 * <p>
 * <b>Example:</b>
 * 
 * <pre>
 * <code>
 * class SampleAssociation extends Association {
 * 	class SampleEnd1 extends {@literal Many<SampleClass1>} {}
 * 	class SampleEnd2 extends {@literal HiddenOne<SampleClass2>} {}
 * }
 * </code>
 * </pre>
 * 
 * See the documentation of {@link Model} for an overview on modeling in
 * JtxtUML.
 *
 * @author Gabor Ferenc Kovacs
 * @see Association
 * @see Association.Many
 * @see Association.One
 * @see Association.MaybeOne
 * @see Association.Some
 * @see Association.Multiple
 * @see Association.HiddenMany
 * @see Association.HiddenOne
 * @see Association.HiddenMaybeOne
 * @see Association.HiddenSome
 * @see Association.HiddenMultiple
 */
public class Association implements ModelElement {

	/**
	 * Sole constructor of <code>Association</code>.
	 */
	protected Association() {
	}

	/**
	 * An immutable collection which contains the elements of a navigable
	 * association end with a multiplicity of 0..*.
	 * 
	 * <p>
	 * <b>Represents:</b> navigable association end with a multiplicity of 0..*
	 * <p>
	 * <b>Usage:</b>
	 * <p>
	 * 
	 * See the documentation of {@link AssociationEnd}.
	 * 
	 * <p>
	 * <b>Java restrictions:</b>
	 * <ul>
	 * <li><i>Instantiate:</i> disallowed</li>
	 * <li><i>Define subtype:</i> allowed
	 * <p>
	 * <b>Subtype requirements:</b>
	 * <ul>
	 * <li>must be the inner class of an association class (a subclass of
	 * {@link Association})</li>
	 * </ul>
	 * <p>
	 * <b>Subtype restrictions:</b>
	 * <ul>
	 * <li><i>Be abstract:</i> disallowed</li>
	 * <li><i>Generic parameters:</i> disallowed</li>
	 * <li><i>Constructors:</i> disallowed</li>
	 * <li><i>Initialization blocks:</i> disallowed</li>
	 * <li><i>Fields:</i> disallowed</li>
	 * <li><i>Methods:</i> disallowed</li>
	 * <li><i>Nested interfaces:</i> disallowed</li>
	 * <li><i>Nested classes:</i> disallowed</li>
	 * <li><i>Nested enums:</i> disallowed</li>
	 * </ul>
	 * </li>
	 * <li><i>Inherit from the defined subtype:</i> disallowed</li>
	 * </ul>
	 * 
	 * <p>
	 * See the documentation of {@link Model} for an overview on modeling in
	 * JtxtUML.
	 * 
	 * @author Gabor Ferenc Kovacs
	 * 
	 * @param <T>
	 *            the type of model objects to be contained in this collection
	 */
	public class Many<T extends ModelClass> extends ManyBase<T> implements
			hu.elte.txtuml.api.model.assocends.Navigability.Navigable,
			hu.elte.txtuml.api.model.assocends.Multiplicity.ZeroToUnlimited {

		/**
		 * Sole constructor of <code>Many</code>.
		 */
		protected Many() {
		}

	}

	/**
	 * An immutable collection which contains the elements of a navigable
	 * association end with a multiplicity of 1..*.
	 * 
	 * <p>
	 * <b>Represents:</b> navigable association end with a multiplicity of 1..*
	 * <p>
	 * <b>Usage:</b>
	 * <p>
	 * 
	 * See the documentation of {@link AssociationEnd}.
	 * 
	 * <p>
	 * <b>Java restrictions:</b>
	 * <ul>
	 * <li><i>Instantiate:</i> disallowed</li>
	 * <li><i>Define subtype:</i> allowed
	 * <p>
	 * <b>Subtype requirements:</b>
	 * <ul>
	 * <li>must be the inner class of an association class (a subclass of
	 * {@link Association})</li>
	 * </ul>
	 * <p>
	 * <b>Subtype restrictions:</b>
	 * <ul>
	 * <li><i>Be abstract:</i> disallowed</li>
	 * <li><i>Generic parameters:</i> disallowed</li>
	 * <li><i>Constructors:</i> disallowed</li>
	 * <li><i>Initialization blocks:</i> disallowed</li>
	 * <li><i>Fields:</i> disallowed</li>
	 * <li><i>Methods:</i> disallowed</li>
	 * <li><i>Nested interfaces:</i> disallowed</li>
	 * <li><i>Nested classes:</i> disallowed</li>
	 * <li><i>Nested enums:</i> disallowed</li>
	 * </ul>
	 * </li>
	 * <li><i>Inherit from the defined subtype:</i> disallowed</li>
	 * </ul>
	 * 
	 * <p>
	 * See the documentation of {@link Model} for an overview on modeling in
	 * JtxtUML.
	 * 
	 * @author Gabor Ferenc Kovacs
	 * 
	 * @param <T>
	 *            the type of model objects to be contained in this collection
	 */
	public class Some<T extends ModelClass> extends SomeBase<T> implements
			hu.elte.txtuml.api.model.assocends.Navigability.Navigable,
			hu.elte.txtuml.api.model.assocends.Multiplicity.OneToUnlimited {

		/**
		 * Sole constructor of <code>Some</code>.
		 */
		protected Some() {
		}

	}

	/**
	 * An immutable collection which contains the elements of a navigable
	 * association end with a multiplicity of 0..1.
	 * 
	 * <p>
	 * <b>Represents:</b> navigable association end with a multiplicity of 0..1
	 * <p>
	 * <b>Usage:</b>
	 * <p>
	 * 
	 * See the documentation of {@link AssociationEnd}.
	 * 
	 * <p>
	 * <b>Java restrictions:</b>
	 * <ul>
	 * <li><i>Instantiate:</i> disallowed</li>
	 * <li><i>Define subtype:</i> allowed
	 * <p>
	 * <b>Subtype requirements:</b>
	 * <ul>
	 * <li>must be the inner class of an association class (a subclass of
	 * {@link Association})</li>
	 * </ul>
	 * <p>
	 * <b>Subtype restrictions:</b>
	 * <ul>
	 * <li><i>Be abstract:</i> disallowed</li>
	 * <li><i>Generic parameters:</i> disallowed</li>
	 * <li><i>Constructors:</i> disallowed</li>
	 * <li><i>Initialization blocks:</i> disallowed</li>
	 * <li><i>Fields:</i> disallowed</li>
	 * <li><i>Methods:</i> disallowed</li>
	 * <li><i>Nested interfaces:</i> disallowed</li>
	 * <li><i>Nested classes:</i> disallowed</li>
	 * <li><i>Nested enums:</i> disallowed</li>
	 * </ul>
	 * </li>
	 * <li><i>Inherit from the defined subtype:</i> disallowed</li>
	 * </ul>
	 * 
	 * <p>
	 * See the documentation of {@link Model} for an overview on modeling in
	 * JtxtUML.
	 * 
	 * @author Gabor Ferenc Kovacs
	 * 
	 * @param <T>
	 *            the type of model objects to be contained in this collection
	 */
	public class MaybeOne<T extends ModelClass> extends MaybeOneBase<T>
			implements
			hu.elte.txtuml.api.model.assocends.Navigability.Navigable,
			hu.elte.txtuml.api.model.assocends.Multiplicity.ZeroToOne {

		/**
		 * Sole constructor of <code>MaybeOne</code>.
		 */
		protected MaybeOne() {
		}

	}

	/**
	 * An immutable collection which contains the elements of a navigable
	 * association end with a multiplicity of 1.
	 * 
	 * <p>
	 * <b>Represents:</b> navigable association end with a multiplicity of 1
	 * <p>
	 * <b>Usage:</b>
	 * <p>
	 * 
	 * See the documentation of {@link AssociationEnd}.
	 * 
	 * <p>
	 * <b>Java restrictions:</b>
	 * <ul>
	 * <li><i>Instantiate:</i> disallowed</li>
	 * <li><i>Define subtype:</i> allowed
	 * <p>
	 * <b>Subtype requirements:</b>
	 * <ul>
	 * <li>must be the inner class of an association class (a subclass of
	 * {@link Association})</li>
	 * </ul>
	 * <p>
	 * <b>Subtype restrictions:</b>
	 * <ul>
	 * <li><i>Be abstract:</i> disallowed</li>
	 * <li><i>Generic parameters:</i> disallowed</li>
	 * <li><i>Constructors:</i> disallowed</li>
	 * <li><i>Initialization blocks:</i> disallowed</li>
	 * <li><i>Fields:</i> disallowed</li>
	 * <li><i>Methods:</i> disallowed</li>
	 * <li><i>Nested interfaces:</i> disallowed</li>
	 * <li><i>Nested classes:</i> disallowed</li>
	 * <li><i>Nested enums:</i> disallowed</li>
	 * </ul>
	 * </li>
	 * <li><i>Inherit from the defined subtype:</i> disallowed</li>
	 * </ul>
	 * 
	 * <p>
	 * See the documentation of {@link Model} for an overview on modeling in
	 * JtxtUML.
	 * 
	 * @author Gabor Ferenc Kovacs
	 * 
	 * @param <T>
	 *            the type of model objects to be contained in this collection
	 */
	public class One<T extends ModelClass> extends OneBase<T> implements
			hu.elte.txtuml.api.model.assocends.Navigability.Navigable,
			hu.elte.txtuml.api.model.assocends.Multiplicity.One {

		/**
		 * Sole constructor of <code>One</code>.
		 */
		protected One() {
		}

	}

	/**
	 * An immutable collection which contains the elements of a navigable
	 * association end with a user-defined multiplicity.
	 * 
	 * <p>
	 * <b>Represents:</b> navigable association end with a user-defined
	 * multiplicity
	 * <p>
	 * <b>Usage:</b>
	 * <p>
	 * 
	 * For general information about association ends, see the documentation of
	 * {@link AssociationEnd}.
	 * <p>
	 * A class that extends <code>Multiple</code> (or its non-navigable
	 * counterpart, {@link HiddenMultiple}), represents an association end that
	 * may have a custom, user-specified multiplicity by applying the
	 * {@link Min} and/or {@link Max} annotations on the class. <code>Min</code>
	 * sets the lower, <code>Max</code> the upper bound of the multiplicity. If
	 * one of the annotations is not present, that means that there is no lower
	 * and/or upper bound. Therefore, an omitted <code>Min</code> equals to an
	 * explicitly specified lower bound of zero, whereas a missing
	 * <code>Max</code> shows that any number of object might be present at the
	 * association end (if their count satisfies the lower bound).
	 * 
	 * <p>
	 * <b>Java restrictions:</b>
	 * <ul>
	 * <li><i>Instantiate:</i> disallowed</li>
	 * <li><i>Define subtype:</i> allowed
	 * <p>
	 * <b>Subtype requirements:</b>
	 * <ul>
	 * <li>must be the inner class of an association class (a subclass of
	 * {@link Association})</li>
	 * </ul>
	 * <p>
	 * <b>Subtype restrictions:</b>
	 * <ul>
	 * <li><i>Be abstract:</i> disallowed</li>
	 * <li><i>Generic parameters:</i> disallowed</li>
	 * <li><i>Constructors:</i> disallowed</li>
	 * <li><i>Initialization blocks:</i> disallowed</li>
	 * <li><i>Fields:</i> disallowed</li>
	 * <li><i>Methods:</i> disallowed</li>
	 * <li><i>Nested interfaces:</i> disallowed</li>
	 * <li><i>Nested classes:</i> disallowed</li>
	 * <li><i>Nested enums:</i> disallowed</li>
	 * </ul>
	 * </li>
	 * <li><i>Inherit from the defined subtype:</i> disallowed</li>
	 * </ul>
	 * 
	 * <p>
	 * See the documentation of {@link Model} for an overview on modeling in
	 * JtxtUML.
	 * 
	 * @author Gabor Ferenc Kovacs
	 * 
	 * @param <T>
	 *            the type of model objects to be contained in this collection
	 */
	public class Multiple<T extends ModelClass> extends MultipleBase<T>
			implements
			hu.elte.txtuml.api.model.assocends.Navigability.Navigable,
			hu.elte.txtuml.api.model.assocends.Multiplicity.MinToMax {

		/**
		 * Sole constructor of <code>Multiple</code>.
		 */
		protected Multiple() {
		}

	}

	/**
	 * An immutable collection which contains the elements of a non-navigable
	 * association end with a multiplicity of 0..*.
	 * 
	 * <p>
	 * <b>Represents:</b> non-navigable association end with a multiplicity of
	 * 0..*
	 * <p>
	 * <b>Usage:</b>
	 * <p>
	 * 
	 * See the documentation of {@link AssociationEnd}.
	 * 
	 * <p>
	 * <b>Java restrictions:</b>
	 * <ul>
	 * <li><i>Instantiate:</i> disallowed</li>
	 * <li><i>Define subtype:</i> allowed
	 * <p>
	 * <b>Subtype requirements:</b>
	 * <ul>
	 * <li>must be the inner class of an association class (a subclass of
	 * {@link Association})</li>
	 * </ul>
	 * <p>
	 * <b>Subtype restrictions:</b>
	 * <ul>
	 * <li><i>Be abstract:</i> disallowed</li>
	 * <li><i>Generic parameters:</i> disallowed</li>
	 * <li><i>Constructors:</i> disallowed</li>
	 * <li><i>Initialization blocks:</i> disallowed</li>
	 * <li><i>Fields:</i> disallowed</li>
	 * <li><i>Methods:</i> disallowed</li>
	 * <li><i>Nested interfaces:</i> disallowed</li>
	 * <li><i>Nested classes:</i> disallowed</li>
	 * <li><i>Nested enums:</i> disallowed</li>
	 * </ul>
	 * </li>
	 * <li><i>Inherit from the defined subtype:</i> disallowed</li>
	 * </ul>
	 * 
	 * <p>
	 * See the documentation of {@link Model} for an overview on modeling in
	 * JtxtUML.
	 * 
	 * @author Gabor Ferenc Kovacs
	 * 
	 * @param <T>
	 *            the type of model objects to be contained in this collection
	 */
	public class HiddenMany<T extends ModelClass> extends ManyBase<T> implements
			hu.elte.txtuml.api.model.assocends.Navigability.NonNavigable,
			hu.elte.txtuml.api.model.assocends.Multiplicity.ZeroToUnlimited {

		/**
		 * Sole constructor of <code>HiddenMany</code>.
		 */
		protected HiddenMany() {
		}

	}

	/**
	 * An immutable collection which contains the elements of a non-navigable
	 * association end with a multiplicity of 1..*.
	 * 
	 * <p>
	 * <b>Represents:</b> non-navigable association end with a multiplicity of
	 * 1..*
	 * <p>
	 * <b>Usage:</b>
	 * <p>
	 * 
	 * See the documentation of {@link AssociationEnd}.
	 * 
	 * <p>
	 * <b>Java restrictions:</b>
	 * <ul>
	 * <li><i>Instantiate:</i> disallowed</li>
	 * <li><i>Define subtype:</i> allowed
	 * <p>
	 * <b>Subtype requirements:</b>
	 * <ul>
	 * <li>must be the inner class of an association class (a subclass of
	 * {@link Association})</li>
	 * </ul>
	 * <p>
	 * <b>Subtype restrictions:</b>
	 * <ul>
	 * <li><i>Be abstract:</i> disallowed</li>
	 * <li><i>Generic parameters:</i> disallowed</li>
	 * <li><i>Constructors:</i> disallowed</li>
	 * <li><i>Initialization blocks:</i> disallowed</li>
	 * <li><i>Fields:</i> disallowed</li>
	 * <li><i>Methods:</i> disallowed</li>
	 * <li><i>Nested interfaces:</i> disallowed</li>
	 * <li><i>Nested classes:</i> disallowed</li>
	 * <li><i>Nested enums:</i> disallowed</li>
	 * </ul>
	 * </li>
	 * <li><i>Inherit from the defined subtype:</i> disallowed</li>
	 * </ul>
	 * 
	 * <p>
	 * See the documentation of {@link Model} for an overview on modeling in
	 * JtxtUML.
	 * 
	 * @author Gabor Ferenc Kovacs
	 * 
	 * @param <T>
	 *            the type of model objects to be contained in this collection
	 */
	public class HiddenSome<T extends ModelClass> extends SomeBase<T> implements
			hu.elte.txtuml.api.model.assocends.Navigability.NonNavigable,
			hu.elte.txtuml.api.model.assocends.Multiplicity.OneToUnlimited {

		/**
		 * Sole constructor of <code>HiddenSome</code>.
		 */
		protected HiddenSome() {
		}

	}

	/**
	 * An immutable collection which contains the elements of a non-navigable
	 * association end with a multiplicity of 0..1.
	 * 
	 * <p>
	 * <b>Represents:</b> non-navigable association end with a multiplicity of
	 * 0..1
	 * <p>
	 * <b>Usage:</b>
	 * <p>
	 * 
	 * See the documentation of {@link AssociationEnd}.
	 * 
	 * <p>
	 * <b>Java restrictions:</b>
	 * <ul>
	 * <li><i>Instantiate:</i> disallowed</li>
	 * <li><i>Define subtype:</i> allowed
	 * <p>
	 * <b>Subtype requirements:</b>
	 * <ul>
	 * <li>must be the inner class of an association class (a subclass of
	 * {@link Association})</li>
	 * </ul>
	 * <p>
	 * <b>Subtype restrictions:</b>
	 * <ul>
	 * <li><i>Be abstract:</i> disallowed</li>
	 * <li><i>Generic parameters:</i> disallowed</li>
	 * <li><i>Constructors:</i> disallowed</li>
	 * <li><i>Initialization blocks:</i> disallowed</li>
	 * <li><i>Fields:</i> disallowed</li>
	 * <li><i>Methods:</i> disallowed</li>
	 * <li><i>Nested interfaces:</i> disallowed</li>
	 * <li><i>Nested classes:</i> disallowed</li>
	 * <li><i>Nested enums:</i> disallowed</li>
	 * </ul>
	 * </li>
	 * <li><i>Inherit from the defined subtype:</i> disallowed</li>
	 * </ul>
	 * 
	 * <p>
	 * See the documentation of {@link Model} for an overview on modeling in
	 * JtxtUML.
	 * 
	 * @author Gabor Ferenc Kovacs
	 * 
	 * @param <T>
	 *            the type of model objects to be contained in this collection
	 */
	public class HiddenMaybeOne<T extends ModelClass> extends MaybeOneBase<T>
			implements
			hu.elte.txtuml.api.model.assocends.Navigability.NonNavigable,
			hu.elte.txtuml.api.model.assocends.Multiplicity.ZeroToOne {

		/**
		 * Sole constructor of <code>HiddenMaybeOne</code>.
		 */
		protected HiddenMaybeOne() {
		}

	}

	/**
	 * An immutable collection which contains the elements of a non-navigable
	 * association end with a multiplicity of 1.
	 * 
	 * <p>
	 * <b>Represents:</b> non-navigable association end with a multiplicity of 1
	 * <p>
	 * <b>Usage:</b>
	 * <p>
	 * 
	 * See the documentation of {@link AssociationEnd}.
	 * 
	 * <p>
	 * <b>Java restrictions:</b>
	 * <ul>
	 * <li><i>Instantiate:</i> disallowed</li>
	 * <li><i>Define subtype:</i> allowed
	 * <p>
	 * <b>Subtype requirements:</b>
	 * <ul>
	 * <li>must be the inner class of an association class (a subclass of
	 * {@link Association})</li>
	 * </ul>
	 * <p>
	 * <b>Subtype restrictions:</b>
	 * <ul>
	 * <li><i>Be abstract:</i> disallowed</li>
	 * <li><i>Generic parameters:</i> disallowed</li>
	 * <li><i>Constructors:</i> disallowed</li>
	 * <li><i>Initialization blocks:</i> disallowed</li>
	 * <li><i>Fields:</i> disallowed</li>
	 * <li><i>Methods:</i> disallowed</li>
	 * <li><i>Nested interfaces:</i> disallowed</li>
	 * <li><i>Nested classes:</i> disallowed</li>
	 * <li><i>Nested enums:</i> disallowed</li>
	 * </ul>
	 * </li>
	 * <li><i>Inherit from the defined subtype:</i> disallowed</li>
	 * </ul>
	 * 
	 * <p>
	 * See the documentation of {@link Model} for an overview on modeling in
	 * JtxtUML.
	 * 
	 * @author Gabor Ferenc Kovacs
	 * 
	 * @param <T>
	 *            the type of model objects to be contained in this collection
	 */
	public class HiddenOne<T extends ModelClass> extends OneBase<T> implements
			hu.elte.txtuml.api.model.assocends.Navigability.NonNavigable,
			hu.elte.txtuml.api.model.assocends.Multiplicity.One {

		/**
		 * Sole constructor of <code>HiddenOne</code>.
		 */
		protected HiddenOne() {
		}

	}

	/**
	 * An immutable collection which contains the elements of a non-navigable
	 * association end with a user-defined multiplicity.
	 * 
	 * <p>
	 * <b>Represents:</b> non-navigable association end with a user-defined
	 * multiplicity.
	 * <p>
	 * <b>Usage:</b>
	 * <p>
	 * 
	 * See the documentation of {@link Multiple} and {@link AssociationEnd}.
	 * 
	 * <p>
	 * <b>Java restrictions:</b>
	 * <ul>
	 * <li><i>Instantiate:</i> disallowed</li>
	 * <li><i>Define subtype:</i> allowed
	 * <p>
	 * <b>Subtype requirements:</b>
	 * <ul>
	 * <li>must be the inner class of an association class (a subclass of
	 * {@link Association})</li>
	 * </ul>
	 * <p>
	 * <b>Subtype restrictions:</b>
	 * <ul>
	 * <li><i>Be abstract:</i> disallowed</li>
	 * <li><i>Generic parameters:</i> disallowed</li>
	 * <li><i>Constructors:</i> disallowed</li>
	 * <li><i>Initialization blocks:</i> disallowed</li>
	 * <li><i>Fields:</i> disallowed</li>
	 * <li><i>Methods:</i> disallowed</li>
	 * <li><i>Nested interfaces:</i> disallowed</li>
	 * <li><i>Nested classes:</i> disallowed</li>
	 * <li><i>Nested enums:</i> disallowed</li>
	 * </ul>
	 * </li>
	 * <li><i>Inherit from the defined subtype:</i> disallowed</li>
	 * </ul>
	 * 
	 * <p>
	 * See the documentation of {@link Model} for an overview on modeling in
	 * JtxtUML.
	 * 
	 * @author Gabor Ferenc Kovacs
	 * 
	 * @param <T>
	 *            the type of model objects to be contained in this collection
	 */
	public class HiddenMultiple<T extends ModelClass> extends MultipleBase<T>
			implements
			hu.elte.txtuml.api.model.assocends.Navigability.NonNavigable,
			hu.elte.txtuml.api.model.assocends.Multiplicity.MinToMax {

		/**
		 * Sole constructor of <code>HiddenMultiple</code>.
		 */
		protected HiddenMultiple() {
		}

	}
}
