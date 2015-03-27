package hu.elte.txtuml.api;

import hu.elte.txtuml.layout.lang.elements.LayoutLink;

/**
 * A base class for associations in the model.
 * 
 * <p>
 * <b>Represents:</b> association
 * <p>
 * <b>Usage:</b>
 * <p>
 * 
 * An association in the model is a subclass of this class, having two inner
 * classes which are both subclasses of {@link AssociationEnd}. These two inner
 * classes will represent the two ends of this association. Their navigability
 * and multiplicity depend on which predefined subclass of association end is
 * extended (it may not be the <code>AssociationEnd</code> class itself).
 * <p>
 * The two model classes which the association connects are defined by the two
 * association ends' generic parameter.
 * 
 * <p>
 * <b>Java restrictions:</b>
 * <ul>
 * <li><i>Instantiate:</i> disallowed
 * <li><i>Define subtype:</i> allowed
 * <p>
 * <b>Subtype requirements:</b>
 * <ul>
 * <li>must have two inner classes which are subclasses of
 * <code>AssociationEnd</code></li>
 * </ul>
 * <p>
 * <b>Subtype restrictions:</b>
 * <ul>
 * <li><i>Generic parameters:</i> disallowed</li>
 * <li><i>Constructors:</i> disallowed</li>
 * <li><i>Initialization blocks:</i> disallowed</li>
 * <li><i>Fields:</i> disallowed</li>
 * <li><i>Methods:</i> disallowed</li>
 * <li><i>Nested interfaces:</i> disallowed</li>
 * <li><i>Static nested classes:</i> disallowed</li>
 * <li><i>Nested enums:</i> disallowed</li>
 * <li><i>Inner classes:</i> allowed at most two, both of which are subclasses of
 * <code>AssociationEnd</code></li>
 * </ul>
 * <li><i>Inherit from defined subtype:</i> disallowed</li></li>
 * </ul>
 * 
 * <p>
 * <b>Example:</b>
 * <p>
 * 
 * <pre>
 * <code>
 * class ExampleAssociation extends Association {
 * 	class ExampleEnd1 extends Many{@code <ExampleClass1>} {}
 * 	class ExampleEnd2 extends HiddenOne{@code <ExampleClass2>} {}
 * }
 * </code>
 * </pre>
 *
 * See the documentation of the {@link hu.elte.txtuml.api} package to get an
 * overview on modeling in txtUML.
 *
 * @author Gábor Ferenc Kovács
 *
 */
public class Association implements ModelElement, LayoutLink {

	/**
	 * Sole constructor of <code>Association</code>.
	 * <p>
	 * <b>Implementation note:</b>
	 * <p>
	 * Protected because this class is intended to be inherited from but not
	 * instantiated. However, it has to be a non-abstract class to make sure
	 * that it is instantiatable when that is needed for the API or the model
	 * exportation.
	 */
	protected Association() {
	}

	/**
	 * An immutable collection which contains the elements of a navigable
	 * association end with 0..* multiplicity.
	 * 
	 * <p>
	 * <b>Represents:</b> navigable association end with 0..* multiplicity
	 * <p>
	 * <b>Usage:</b>
	 * <p>
	 * 
	 * See the documentation of {@link AssociationEnd}.
	 * 
	 * <p>
	 * <b>Java restrictions:</b>
	 * <ul>
	 * <li><i>Instantiate:</i> disallowed
	 * <li><i>Define subtype:</i> allowed
	 * <p>
	 * <b>Subtype requirements:</b>
	 * <ul>
	 * <li>must be the nested class of an association class (a subclass of
	 * {@link Association})</li>
	 * </ul>
	 * <p>
	 * <b>Subtype restrictions:</b>
	 * <ul>
	 * <li><i>Generic parameters:</i> disallowed</li>
	 * <li><i>Constructors:</i> disallowed</li>
	 * <li><i>Initialization blocks:</i> disallowed</li>
	 * <li><i>Fields:</i> disallowed</li>
	 * <li><i>Methods:</i> disallowed</li>
	 * <li><i>Nested interfaces:</i> disallowed</li>
	 * <li><i>Static nested classes:</i> disallowed</li>
	 * <li><i>Nested enums:</i> disallowed</li>
	 * <li><i>Inner classes:</i> disallowed</li>
	 * </ul>
	 * <li><i>Inherit from defined subtype:</i> disallowed</li></li>
	 * </ul>
	 * 
	 * See the documentation of the {@link hu.elte.txtuml.api} package to get an
	 * overview on modeling in txtUML.
	 *
	 * @author Gábor Ferenc Kovács
	 *
	 * @param <T>
	 *            the type of model objects to be contained in this collection
	 */
	public class Many<T extends ModelClass> extends BaseMany<T> implements
			hu.elte.txtuml.api.semantics.Navigability.Navigable,
			hu.elte.txtuml.api.semantics.Multiplicity.ZeroToUnlimited {
	}

	/**
	 * An immutable collection which contains the elements of a navigable
	 * association end with 1..* multiplicity.
	 * 
	 * <p>
	 * <b>Represents:</b> navigable association end with 1..* multiplicity
	 * <p>
	 * <b>Usage:</b>
	 * <p>
	 * 
	 * See the documentation of {@link AssociationEnd}.
	 * 
	 * <p>
	 * <b>Java restrictions:</b>
	 * <ul>
	 * <li><i>Instantiate:</i> disallowed
	 * <li><i>Define subtype:</i> allowed
	 * <p>
	 * <b>Subtype requirements:</b>
	 * <ul>
	 * <li>must be the nested class of an association class (a subclass of
	 * {@link Association})</li>
	 * </ul>
	 * <p>
	 * <b>Subtype restrictions:</b>
	 * <ul>
	 * <li><i>Generic parameters:</i> disallowed</li>
	 * <li><i>Constructors:</i> disallowed</li>
	 * <li><i>Initialization blocks:</i> disallowed</li>
	 * <li><i>Fields:</i> disallowed</li>
	 * <li><i>Methods:</i> disallowed</li>
	 * <li><i>Nested interfaces:</i> disallowed</li>
	 * <li><i>Static nested classes:</i> disallowed</li>
	 * <li><i>Nested enums:</i> disallowed</li>
	 * <li><i>Inner classes:</i> disallowed</li>
	 * </ul>
	 * <li><i>Inherit from defined subtype:</i> disallowed</li></li>
	 * </ul>
	 * 
	 * See the documentation of the {@link hu.elte.txtuml.api} package to get an
	 * overview on modeling in txtUML.
	 *
	 * @author Gábor Ferenc Kovács
	 *
	 * @param <T>
	 *            the type of model objects to be contained in this collection
	 */
	public class Some<T extends ModelClass> extends BaseSome<T> implements
			hu.elte.txtuml.api.semantics.Navigability.Navigable,
			hu.elte.txtuml.api.semantics.Multiplicity.OneToUnlimited {
	}

	/**
	 * An immutable collection which contains the elements of a navigable
	 * association end with 0..1 multiplicity.
	 * 
	 * <p>
	 * <b>Represents:</b> navigable association end with 0..1 multiplicity
	 * <p>
	 * <b>Usage:</b>
	 * <p>
	 * 
	 * See the documentation of {@link AssociationEnd}.
	 * 
	 * <p>
	 * <b>Java restrictions:</b>
	 * <ul>
	 * <li><i>Instantiate:</i> disallowed
	 * <li><i>Define subtype:</i> allowed
	 * <p>
	 * <b>Subtype requirements:</b>
	 * <ul>
	 * <li>must be the nested class of an association class (a subclass of
	 * {@link Association})</li>
	 * </ul>
	 * <p>
	 * <b>Subtype restrictions:</b>
	 * <ul>
	 * <li><i>Generic parameters:</i> disallowed</li>
	 * <li><i>Constructors:</i> disallowed</li>
	 * <li><i>Initialization blocks:</i> disallowed</li>
	 * <li><i>Fields:</i> disallowed</li>
	 * <li><i>Methods:</i> disallowed</li>
	 * <li><i>Nested interfaces:</i> disallowed</li>
	 * <li><i>Static nested classes:</i> disallowed</li>
	 * <li><i>Nested enums:</i> disallowed</li>
	 * <li><i>Inner classes:</i> disallowed</li>
	 * </ul>
	 * <li><i>Inherit from defined subtype:</i> disallowed</li></li>
	 * </ul>
	 * 
	 * See the documentation of the {@link hu.elte.txtuml.api} package to get an
	 * overview on modeling in txtUML.
	 *
	 * @author Gábor Ferenc Kovács
	 *
	 * @param <T>
	 *            the type of model objects to be contained in this collection
	 */
	public class MaybeOne<T extends ModelClass> extends BaseMaybeOne<T>
			implements hu.elte.txtuml.api.semantics.Navigability.Navigable,
			hu.elte.txtuml.api.semantics.Multiplicity.ZeroToOne {
	}

	/**
	 * An immutable collection which contains the elements of a navigable
	 * association end with 1 multiplicity.
	 * 
	 * <p>
	 * <b>Represents:</b> navigable association end with 1 multiplicity
	 * <p>
	 * <b>Usage:</b>
	 * <p>
	 * 
	 * See the documentation of {@link AssociationEnd}.
	 * 
	 * <p>
	 * <b>Java restrictions:</b>
	 * <ul>
	 * <li><i>Instantiate:</i> disallowed
	 * <li><i>Define subtype:</i> allowed
	 * <p>
	 * <b>Subtype requirements:</b>
	 * <ul>
	 * <li>must be the nested class of an association class (a subclass of
	 * {@link Association})</li>
	 * </ul>
	 * <p>
	 * <b>Subtype restrictions:</b>
	 * <ul>
	 * <li><i>Generic parameters:</i> disallowed</li>
	 * <li><i>Constructors:</i> disallowed</li>
	 * <li><i>Initialization blocks:</i> disallowed</li>
	 * <li><i>Fields:</i> disallowed</li>
	 * <li><i>Methods:</i> disallowed</li>
	 * <li><i>Nested interfaces:</i> disallowed</li>
	 * <li><i>Static nested classes:</i> disallowed</li>
	 * <li><i>Nested enums:</i> disallowed</li>
	 * <li><i>Inner classes:</i> disallowed</li>
	 * </ul>
	 * <li><i>Inherit from defined subtype:</i> disallowed</li></li>
	 * </ul>
	 * 
	 * See the documentation of the {@link hu.elte.txtuml.api} package to get an
	 * overview on modeling in txtUML.
	 *
	 * @author Gábor Ferenc Kovács
	 *
	 * @param <T>
	 *            the type of model objects to be contained in this collection
	 */
	public class One<T extends ModelClass> extends BaseOne<T> implements
			hu.elte.txtuml.api.semantics.Navigability.Navigable,
			hu.elte.txtuml.api.semantics.Multiplicity.One {
	}

	/**
	 * An immutable collection which contains the elements of a non-navigable
	 * association end with 0..* multiplicity.
	 * 
	 * <p>
	 * <b>Represents:</b> non-navigable association end with 0..* multiplicity
	 * <p>
	 * <b>Usage:</b>
	 * <p>
	 * 
	 * See the documentation of {@link AssociationEnd}.
	 * 
	 * <p>
	 * <b>Java restrictions:</b>
	 * <ul>
	 * <li><i>Instantiate:</i> disallowed
	 * <li><i>Define subtype:</i> allowed
	 * <p>
	 * <b>Subtype requirements:</b>
	 * <ul>
	 * <li>must be the nested class of an association class (a subclass of
	 * {@link Association})</li>
	 * </ul>
	 * <p>
	 * <b>Subtype restrictions:</b>
	 * <ul>
	 * <li><i>Generic parameters:</i> disallowed</li>
	 * <li><i>Constructors:</i> disallowed</li>
	 * <li><i>Initialization blocks:</i> disallowed</li>
	 * <li><i>Fields:</i> disallowed</li>
	 * <li><i>Methods:</i> disallowed</li>
	 * <li><i>Nested interfaces:</i> disallowed</li>
	 * <li><i>Static nested classes:</i> disallowed</li>
	 * <li><i>Nested enums:</i> disallowed</li>
	 * <li><i>Inner classes:</i> disallowed</li>
	 * </ul>
	 * <li><i>Inherit from defined subtype:</i> disallowed</li></li>
	 * </ul>
	 * 
	 * See the documentation of the {@link hu.elte.txtuml.api} package to get an
	 * overview on modeling in txtUML.
	 *
	 * @author Gábor Ferenc Kovács
	 *
	 * @param <T>
	 *            the type of model objects to be contained in this collection
	 */
	public class HiddenMany<T extends ModelClass> extends BaseMany<T> implements
			hu.elte.txtuml.api.semantics.Navigability.NonNavigable,
			hu.elte.txtuml.api.semantics.Multiplicity.ZeroToUnlimited {
	}

	/**
	 * An immutable collection which contains the elements of a non-navigable
	 * association end with 1..* multiplicity.
	 * 
	 * <p>
	 * <b>Represents:</b> non-navigable association end with 1..* multiplicity
	 * <p>
	 * <b>Usage:</b>
	 * <p>
	 * 
	 * See the documentation of {@link AssociationEnd}.
	 * 
	 * <p>
	 * <b>Java restrictions:</b>
	 * <ul>
	 * <li><i>Instantiate:</i> disallowed
	 * <li><i>Define subtype:</i> allowed
	 * <p>
	 * <b>Subtype requirements:</b>
	 * <ul>
	 * <li>must be the nested class of an association class (a subclass of
	 * {@link Association})</li>
	 * </ul>
	 * <p>
	 * <b>Subtype restrictions:</b>
	 * <ul>
	 * <li><i>Generic parameters:</i> disallowed</li>
	 * <li><i>Constructors:</i> disallowed</li>
	 * <li><i>Initialization blocks:</i> disallowed</li>
	 * <li><i>Fields:</i> disallowed</li>
	 * <li><i>Methods:</i> disallowed</li>
	 * <li><i>Nested interfaces:</i> disallowed</li>
	 * <li><i>Static nested classes:</i> disallowed</li>
	 * <li><i>Nested enums:</i> disallowed</li>
	 * <li><i>Inner classes:</i> disallowed</li>
	 * </ul>
	 * <li><i>Inherit from defined subtype:</i> disallowed</li></li>
	 * </ul>
	 * 
	 * See the documentation of the {@link hu.elte.txtuml.api} package to get an
	 * overview on modeling in txtUML.
	 *
	 * @author Gábor Ferenc Kovács
	 *
	 * @param <T>
	 *            the type of model objects to be contained in this collection
	 */
	public class HiddenSome<T extends ModelClass> extends BaseSome<T> implements
			hu.elte.txtuml.api.semantics.Navigability.NonNavigable,
			hu.elte.txtuml.api.semantics.Multiplicity.OneToUnlimited {
	}

	/**
	 * An immutable collection which contains the elements of a non-navigable
	 * association end with 0..1 multiplicity.
	 * 
	 * <p>
	 * <b>Represents:</b> non-navigable association end with 0..1 multiplicity
	 * <p>
	 * <b>Usage:</b>
	 * <p>
	 * 
	 * See the documentation of {@link AssociationEnd}.
	 * 
	 * <p>
	 * <b>Java restrictions:</b>
	 * <ul>
	 * <li><i>Instantiate:</i> disallowed
	 * <li><i>Define subtype:</i> allowed
	 * <p>
	 * <b>Subtype requirements:</b>
	 * <ul>
	 * <li>must be the nested class of an association class (a subclass of
	 * {@link Association})</li>
	 * </ul>
	 * <p>
	 * <b>Subtype restrictions:</b>
	 * <ul>
	 * <li><i>Generic parameters:</i> disallowed</li>
	 * <li><i>Constructors:</i> disallowed</li>
	 * <li><i>Initialization blocks:</i> disallowed</li>
	 * <li><i>Fields:</i> disallowed</li>
	 * <li><i>Methods:</i> disallowed</li>
	 * <li><i>Nested interfaces:</i> disallowed</li>
	 * <li><i>Static nested classes:</i> disallowed</li>
	 * <li><i>Nested enums:</i> disallowed</li>
	 * <li><i>Inner classes:</i> disallowed</li>
	 * </ul>
	 * <li><i>Inherit from defined subtype:</i> disallowed</li></li>
	 * </ul>
	 * 
	 * See the documentation of the {@link hu.elte.txtuml.api} package to get an
	 * overview on modeling in txtUML.
	 *
	 * @author Gábor Ferenc Kovács
	 *
	 * @param <T>
	 *            the type of model objects to be contained in this collection
	 */
	public class HiddenMaybeOne<T extends ModelClass> extends BaseMaybeOne<T>
			implements hu.elte.txtuml.api.semantics.Navigability.NonNavigable,
			hu.elte.txtuml.api.semantics.Multiplicity.ZeroToOne {
	}

	/**
	 * An immutable collection which contains the elements of a non-navigable
	 * association end with 1 multiplicity.
	 * 
	 * <p>
	 * <b>Represents:</b> non-navigable association end with 1 multiplicity
	 * <p>
	 * <b>Usage:</b>
	 * <p>
	 * 
	 * See the documentation of {@link AssociationEnd}.
	 * 
	 * <p>
	 * <b>Java restrictions:</b>
	 * <ul>
	 * <li><i>Instantiate:</i> disallowed
	 * <li><i>Define subtype:</i> allowed
	 * <p>
	 * <b>Subtype requirements:</b>
	 * <ul>
	 * <li>must be the nested class of an association class (a subclass of
	 * {@link Association})</li>
	 * </ul>
	 * <p>
	 * <b>Subtype restrictions:</b>
	 * <ul>
	 * <li><i>Generic parameters:</i> disallowed</li>
	 * <li><i>Constructors:</i> disallowed</li>
	 * <li><i>Initialization blocks:</i> disallowed</li>
	 * <li><i>Fields:</i> disallowed</li>
	 * <li><i>Methods:</i> disallowed</li>
	 * <li><i>Nested interfaces:</i> disallowed</li>
	 * <li><i>Static nested classes:</i> disallowed</li>
	 * <li><i>Nested enums:</i> disallowed</li>
	 * <li><i>Inner classes:</i> disallowed</li>
	 * </ul>
	 * <li><i>Inherit from defined subtype:</i> disallowed</li></li>
	 * </ul>
	 * 
	 * See the documentation of the {@link hu.elte.txtuml.api} package to get an
	 * overview on modeling in txtUML.
	 *
	 * @author Gábor Ferenc Kovács
	 *
	 * @param <T>
	 *            the type of model objects to be contained in this collection
	 */
	public class HiddenOne<T extends ModelClass> extends BaseOne<T> implements
			hu.elte.txtuml.api.semantics.Navigability.NonNavigable,
			hu.elte.txtuml.api.semantics.Multiplicity.One {
	}

}
