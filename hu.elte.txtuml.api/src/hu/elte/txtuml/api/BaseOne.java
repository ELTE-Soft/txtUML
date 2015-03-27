package hu.elte.txtuml.api;

/**
 * Base class of association ends having a 1 multiplicity.
 * <p>
 * Inherits its implementation from <code>BaseMaybeOne</code>.
 * <p>
 * See the documentation of the {@link hu.elte.txtuml.api} package to get an
 * overview on modeling in txtUML.
 * 
 * @author Gábor Ferenc Kovács
 *
 * @param <T> the type of model objects to be contained in this collection
 */
class BaseOne<T extends ModelClass> extends BaseMaybeOne<T> {
}
