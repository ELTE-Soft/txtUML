package hu.elte.txtuml.api;

/**
 * Base interface for elements of the model.
 * 
 * <p>
 * <b>Represents:</b> subtypes represent elements of the model
 * <p>
 * <b>Usage:</b>
 * <p>
 * 
 * Should not be used directly.
 * 
 * <p>
 * <b>Java restrictions:</b>
 * <ul>
 * <li><i>Define subtype:</i> disallowed</li>
 * </ul>
 * 
 * <p>
 * See the documentation of the {@link hu.elte.txtuml.api} package to get an
 * overview on modeling in txtUML.
 * 
 * <p>
 * <b>Implementation note:</b>
 * <p>
 * It is needed for AspectJ to work well. All types that are used in the model
 * should implement this interface (not directly but through inheritance).
 * 
 * @author Gabor Ferenc Kovacs
 *
 */
public interface ModelElement {
}
