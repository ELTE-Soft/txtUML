package hu.elte.txtuml.layout.export.source;

import java.util.Arrays;
import java.util.List;

import hu.elte.txtuml.layout.export.DiagramType;
import hu.elte.txtuml.layout.export.interfaces.ElementExporter;
import hu.elte.txtuml.layout.export.problems.ElementExportationException;
import hu.elte.txtuml.utils.Pair;

/**
 * Implementations of this interface are used by the diagram exportation to get
 * information about the source model for which the diagram is defined. A single
 * {@link SourceExporter} instance exports a certain type of diagram.
 * <p>
 * For example, different {@code SourceExporter}s should be defined for class
 * diagrams and state machine diagrams.
 */
public interface SourceExporter {

	List<SourceExporter> ALL = Arrays.asList(new ClassDiagramExporter(), new StateMachineDiagramExporter());

	DiagramType getType();

	boolean isNode(Class<?> cls);

	boolean isLink(Class<?> cls);

	//String getReferencedElementName(NodeMap nodes);
	
	/**
	 * Called to get an identifier object of the model which contains
	 * {@code element}.
	 * 
	 * @param element
	 *            must be a {@code Class<?>} instance for which either
	 *            {@link isNode} or {@link isLink} returns {@code true} (it is
	 *            not checked explicitly)
	 * @param elementExporter
	 *            the current element exporter instance
	 * @return identifier of model
	 * @throws ElementExportationException
	 *             if {@code element} is not contained in a model (if
	 *             {@code element} is not a node or a link or is badly defined)
	 */
	ModelId getModelOf(Class<?> element, ElementExporter elementExporter) throws ElementExportationException;
	
	Pair<Class<?>, Class<?>> getStartAndEndOfLink(Class<?> link) throws ElementExportationException;

	/**
	 * This method is called with a model identifier returned by one of this
	 * exporter's {@link getModelOf} calls. This method should export all the
	 * implied links from the specified model (links which's source and target
	 * are shown on the diagram).
	 */
	void exportImpliedLinks(ModelId modelId, ElementExporter elementExporter);

}
