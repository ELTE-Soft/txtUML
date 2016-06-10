package hu.elte.txtuml.api.model.external;

/**
 * Base type for external types in the model. External types are those
 * whose implementation is not part of the model, and from the scope of the
 * model they function as black boxes. They might be used to bring external
 * features into the model or to communicate with components of the program that
 * are not part of the model.
 * 
 * <p>
 * <b>Represents:</b> external type
 * <p>
 * <b>Usage:</b>
 * <p>
 * 
 * This type should not be extended or implemented directly.
 * 
 * <p>
 * <b>Java restrictions:</b>
 * <ul>
 * <li><i>Define subtype:</i> disallowed</li>
 * </ul>
 * 
 * <p>
 * See the documentation of {@link hu.elte.txtuml.api.model.Model Model} for an
 * overview on modeling in JtxtUML.
 */
public interface ExternalType {
}
