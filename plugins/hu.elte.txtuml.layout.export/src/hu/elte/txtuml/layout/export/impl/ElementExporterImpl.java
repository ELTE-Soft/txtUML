package hu.elte.txtuml.layout.export.impl;

import hu.elte.txtuml.api.layout.Alignment;
import hu.elte.txtuml.api.layout.Contains;
import hu.elte.txtuml.layout.export.DiagramType;
import hu.elte.txtuml.layout.export.diagramexporters.ModelId;
import hu.elte.txtuml.layout.export.diagramexporters.SourceExporter;
import hu.elte.txtuml.layout.export.elementinfo.ConcreteElementInfo;
import hu.elte.txtuml.layout.export.elementinfo.ElementInfo;
import hu.elte.txtuml.layout.export.elementinfo.GroupInfo;
import hu.elte.txtuml.layout.export.elementinfo.LinkGroupInfo;
import hu.elte.txtuml.layout.export.elementinfo.LinkInfo;
import hu.elte.txtuml.layout.export.elementinfo.NodeGroupInfo;
import hu.elte.txtuml.layout.export.elementinfo.NodeInfo;
import hu.elte.txtuml.layout.export.interfaces.ElementExporter;
import hu.elte.txtuml.layout.export.interfaces.LinkGroupMap;
import hu.elte.txtuml.layout.export.interfaces.LinkList;
import hu.elte.txtuml.layout.export.interfaces.LinkMap;
import hu.elte.txtuml.layout.export.interfaces.NodeGroupMap;
import hu.elte.txtuml.layout.export.interfaces.NodeList;
import hu.elte.txtuml.layout.export.interfaces.NodeMap;
import hu.elte.txtuml.layout.export.problems.ElementExportationException;
import hu.elte.txtuml.layout.export.problems.ProblemReporter;
import hu.elte.txtuml.layout.export.problems.Utils;
import hu.elte.txtuml.layout.visualizer.model.LineAssociation;
import hu.elte.txtuml.layout.visualizer.model.RectangleObject;
import hu.elte.txtuml.utils.Pair;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

/**
 * Default implementation for {@link ElementExporter}.
 * 
 * @author Gabor Ferenc Kovacs
 *
 */
public class ElementExporterImpl implements ElementExporter {

	/**
	 * Can be null.
	 */
	private SourceExporter sourceExporter;

	/**
	 * Can be null.
	 */
	private ModelId containingModel = null;

	/**
	 * All nodes, including user defined phantoms.
	 */
	private final NodeMap nodes;
	private final LinkMap links;
	private final NodeGroupMap nodeGroups;
	private final LinkGroupMap linkGroups;
	/**
	 * Internal and user defined phantoms.
	 */
	private final NodeList phantoms;
	/**
	 * Separate container since the lack of element class.
	 */
	private final LinkList generalizations;
	private final ProblemReporter problemReporter;
	private final Set<Class<?>> failedGroups;

	private long phantomCounter = 0;

	public ElementExporterImpl(ProblemReporter problemReporter) {
		this.nodes = NodeMap.create();
		this.links = LinkMap.create();
		this.nodeGroups = NodeGroupMap.create();
		this.linkGroups = LinkGroupMap.create();
		this.phantoms = NodeList.create();
		this.generalizations = LinkList.create();
		this.problemReporter = problemReporter;
		this.failedGroups = new HashSet<Class<?>>();
	}

	@Override
	public DiagramType getDiagramTypeBasedOnElements() {
		return sourceExporter == null ? DiagramType.Unknown : sourceExporter
				.getType();
	}

	@Override
	public String getModelName() {
		return containingModel == null ? null : containingModel.getName();
	}

	@Override
	public NodeMap getNodes() {
		NodeMap result = NodeMap.create();

		nodes.forEach((k, v) -> {
			if (!v.isPhantom()) {
				result.put(k, v);
			}
		});

		return result;
	}

	@Override
	public NodeList getPhantoms() {
		return phantoms;
	}

	@Override
	public LinkMap getLinks() {
		return links;
	}

	@Override
	public Set<RectangleObject> getNodesAsObjects() {
		// for efficiency purposes, phantoms are handled in NodeMap's convert()
		// method
		return nodes.convert();
	}

	@Override
	public Set<LineAssociation> getLinksAsLines() {
		Set<LineAssociation> res = links.convert();

		generalizations.forEach(gen -> res.add(gen.convert()));

		return res;
	}

	@Override
	public ElementInfo exportElement(Class<?> elementClass)
			throws ElementExportationException {
		if (failedGroups.contains(elementClass)) {
			throw new ElementExportationException();
		}

		ElementInfo info;

		info = nodes.get(elementClass);
		if (info != null) {
			return info;
		}

		info = links.get(elementClass);
		if (info != null) {
			return info;
		}

		info = nodeGroups.get(elementClass);
		if (info != null) {
			if (info.beingExported()) {
				reportSelfContainment(elementClass);
				throw new ElementExportationException();
			}

			return info;
		}

		info = linkGroups.get(elementClass);
		if (info != null) {
			if (info.beingExported()) {
				reportSelfContainment(elementClass);
				throw new ElementExportationException();
			}

			return info;
		}

		info = exportNewNode(elementClass);
		if (info != null) {
			return info;
		}

		info = exportNewPhantom(elementClass);
		if (info != null) {
			return info;
		}

		info = exportNewLink(elementClass);
		if (info != null) {
			return info;
		}

		info = exportNewNodeGroup(elementClass);
		if (info != null) {
			return info;
		}

		info = exportNewLinkGroup(elementClass);
		if (info != null) {
			return info;
		}

		throw new ElementExportationException();
	}

	@Override
	public ConcreteElementInfo exportConcreteElement(Class<?> elementClass)
			throws ElementExportationException {
		ConcreteElementInfo info;

		info = nodes.get(elementClass);
		if (info != null) {
			return info;
		}

		info = links.get(elementClass);
		if (info != null) {
			return info;
		}

		info = exportNewNode(elementClass);
		if (info != null) {
			return info;
		}

		info = exportNewPhantom(elementClass);
		if (info != null) {
			return info;
		}

		info = exportNewLink(elementClass);
		if (info != null) {
			return info;
		}

		throw new ElementExportationException();
	}

	@Override
	public GroupInfo exportGroupElement(Class<?> elementClass)
			throws ElementExportationException {
		if (failedGroups.contains(elementClass)) {
			throw new ElementExportationException();
		}

		GroupInfo info;

		info = nodeGroups.get(elementClass);
		if (info != null) {
			if (info.beingExported()) {
				reportSelfContainment(elementClass);
				throw new ElementExportationException();
			}

			return info;
		}

		info = linkGroups.get(elementClass);
		if (info != null) {
			if (info.beingExported()) {
				reportSelfContainment(elementClass);
				throw new ElementExportationException();
			}

			return info;
		}

		info = exportNewNodeGroup(elementClass);
		if (info != null) {
			return info;
		}

		info = exportNewLinkGroup(elementClass);
		if (info != null) {
			return info;
		}

		throw new ElementExportationException();
	}

	@Override
	public NodeInfo exportNode(Class<?> nodeClass)
			throws ElementExportationException {
		NodeInfo info;

		info = nodes.get(nodeClass);
		if (info != null) {
			return info;
		}

		info = exportNewNode(nodeClass);
		if (info != null) {
			return info;
		}

		throw new ElementExportationException();
	}

	@Override
	public LinkInfo exportLink(Class<?> linkClass)
			throws ElementExportationException {
		LinkInfo info;

		info = links.get(linkClass);
		if (info != null) {
			return info;
		}

		info = exportNewLink(linkClass);
		if (info != null) {
			return info;
		}

		throw new ElementExportationException();
	}

	@Override
	public NodeGroupInfo exportNodeGroup(Class<?> nodeGroupClass)
			throws ElementExportationException {
		if (failedGroups.contains(nodeGroupClass)) {
			throw new ElementExportationException();
		}

		NodeGroupInfo info;

		info = nodeGroups.get(nodeGroupClass);
		if (info != null) {
			if (info.beingExported()) {
				reportSelfContainment(nodeGroupClass);
				throw new ElementExportationException();
			}

			return info;
		}

		info = exportNewNodeGroup(nodeGroupClass);
		if (info != null) {
			return info;
		}

		throw new ElementExportationException();
	}

	@Override
	public LinkGroupInfo exportLinkGroup(Class<?> linkGroupClass)
			throws ElementExportationException {
		if (failedGroups.contains(linkGroupClass)) {
			throw new ElementExportationException();
		}

		LinkGroupInfo info;

		info = linkGroups.get(linkGroupClass);
		if (info != null) {
			if (info.beingExported()) {
				reportSelfContainment(linkGroupClass);
				throw new ElementExportationException();
			}

			return info;
		}

		info = exportNewLinkGroup(linkGroupClass);
		if (info != null) {
			return info;
		}

		throw new ElementExportationException();
	}

	@Override
	public NodeInfo exportPhantom(Class<?> phantomClass)
			throws ElementExportationException {
		NodeInfo info;

		info = nodes.get(phantomClass);
		if (info != null) {
			return info;
		}

		info = exportNewPhantom(phantomClass);
		if (info != null) {
			return info;
		}

		throw new ElementExportationException();
	}

	@Override
	public NodeGroupInfo exportAnonymousNodeGroup(Class<?>[] abstractNodes)
			throws ElementExportationException {
		NodeGroupInfo info = NodeGroupInfo.create(null, null);

		for (Class<?> abstractNode : abstractNodes) {
			ElementInfo innerInfo = exportElement(abstractNode);

			if (innerInfo instanceof NodeInfo) {
				info.addNode((NodeInfo) innerInfo);

			} else if (innerInfo instanceof NodeGroupInfo) {
				for (NodeInfo innerNode : ((NodeGroupInfo) innerInfo)
						.getAllNodes().values()) {
					info.addNode(innerNode);
				}

			} else {
				problemReporter.invalidAnonymousGroup(abstractNodes,
						abstractNode);
				throw new ElementExportationException();

			}
		}

		return info;
	}

	private NodeInfo exportNewNode(Class<?> cls) {
		if (callOnSourceExporter(se -> se.isNode(cls)) && checkModelOf(cls)) {
			NodeInfo info = NodeInfo.create(cls, Utils.classAsString(cls));
			nodes.put(cls, info);
			return info;
		}
		return null;
	}

	private NodeInfo exportNewPhantom(Class<?> cls) {
		if (ElementExporter.isPhantom(cls)) {
			NodeInfo info = NodeInfo
					.create(cls, "#phantom_" + ++phantomCounter);
			nodes.put(cls, info);
			phantoms.add(info);

			return info;
		}

		return null;
	}

	private LinkInfo exportNewLink(Class<?> cls)
			throws ElementExportationException {
		if (callOnSourceExporter(se -> se.isLink(cls)) && checkModelOf(cls)) {
			// sourceExporter cannot be null at this point
			Pair<Class<?>, Class<?>> p = sourceExporter
					.getStartAndEndOfLink(cls);

			LinkInfo info = LinkInfo.create(cls, Utils.classAsString(cls),
					exportNode(p.getFirst()), exportNode(p.getSecond()));
			links.put(cls, info);
			return info;
		}
		return null;
	}

	private NodeGroupInfo exportNewNodeGroup(Class<?> cls)
			throws ElementExportationException {
		if (!ElementExporter.isNodeGroup(cls)) {
			return null;
		}
		NodeGroupInfo info = NodeGroupInfo
				.create(cls, Utils.classAsString(cls));
		nodeGroups.put(cls, info);
		info.setBeingExported(true);

		boolean containsAnnotPresent = false;
		for (Annotation annot : cls.getAnnotations()) {
			if (isOfType(Contains.class, annot)) {
				containsAnnotPresent = true;

				Contains containsAnnot = (Contains) annot;
				if (containsAnnot.value().length == 0) {
					problemReporter.emptyGroup(cls);
				}

				for (Class<?> containedClass : containsAnnot.value()) {
					ElementInfo innerInfo = exportElement(containedClass);

					if (innerInfo instanceof NodeInfo) {
						info.addNode((NodeInfo) innerInfo);

					} else if (innerInfo instanceof NodeGroupInfo) {
						for (NodeInfo node : ((NodeGroupInfo) innerInfo)
								.getAllNodes().values()) {
							info.addNode(node);
						}

					} else {
						failedGroups.add(cls);
						problemReporter.invalidGroup(cls, containedClass);
						throw new ElementExportationException();

					}
				}

			} else if (isOfType(Alignment.class, annot)) {
				info.setAlignment(((Alignment) annot).value());

			} else {
				problemReporter.unknownAnnotationOnClass(annot, cls);

			}
		}

		if (!containsAnnotPresent) {
			problemReporter.groupWithoutContainsAnnotation(cls);
		}

		info.setBeingExported(false);
		return info;
	}

	private LinkGroupInfo exportNewLinkGroup(Class<?> cls)
			throws ElementExportationException {
		if (!ElementExporter.isLinkGroup(cls)) {
			return null;
		}
		LinkGroupInfo info = LinkGroupInfo
				.create(cls, Utils.classAsString(cls));
		linkGroups.put(cls, info);
		info.setBeingExported(true);

		boolean containsAnnotPresent = false;
		for (Annotation annot : cls.getAnnotations()) {
			if (isOfType(Contains.class, annot)) {
				containsAnnotPresent = true;

				Contains containsAnnot = (Contains) annot;
				if (containsAnnot.value().length == 0) {
					problemReporter.emptyGroup(cls);
				}

				for (Class<?> containedClass : containsAnnot.value()) {
					ElementInfo innerInfo = exportElement(containedClass);

					if (innerInfo instanceof LinkInfo) {
						info.addLink((LinkInfo) innerInfo);

					} else if (innerInfo instanceof LinkGroupInfo) {
						for (LinkInfo link : ((LinkGroupInfo) innerInfo)
								.getAllLinks().values()) {
							info.addLink(link);
						}

					} else {
						failedGroups.add(cls);
						problemReporter.invalidGroup(cls, containedClass);
						throw new ElementExportationException();
					}
				}

			} else {
				problemReporter.unknownAnnotationOnClass(annot, cls);
			}
		}

		if (!containsAnnotPresent) {
			problemReporter.groupWithoutContainsAnnotation(cls);
		}

		info.setBeingExported(false);
		return info;
	}

	@Override
	public NodeInfo createPhantom() {
		NodeInfo newPhantom = NodeInfo.create(null, "#phantom_"
				+ ++phantomCounter);
		phantoms.add(newPhantom);

		return newPhantom;
	}

	@Override
	public void exportGeneralization(Class<?> base, Class<?> derived)
			throws ElementExportationException {
		NodeInfo baseInfo = exportNode(base);
		NodeInfo derivedInfo = exportNode(derived);

		LinkInfo info = LinkInfo.createGeneralization(null, baseInfo.toString()
				+ "#" + derivedInfo.toString(), baseInfo, derivedInfo);

		generalizations.add(info);
	}

	@Override
	public void exportImpliedLinks() {
		if (sourceExporter != null) {
			sourceExporter.exportImpliedLinks(containingModel, this);
		}
	}

	// helper functions

	private void reportSelfContainment(Class<?> cls) {
		failedGroups.add(cls);
		problemReporter.selfContainment(cls);
	}

	private boolean callOnSourceExporter(Function<SourceExporter, Boolean> func) {
		if (sourceExporter != null) {
			return func.apply(sourceExporter);
		} else {
			for (SourceExporter se : SourceExporter.ALL) {
				if (func.apply(se)) {
					sourceExporter = se;
					return true;
				}
			}
		}
		return false;
	}
	
	private static boolean isOfType(
			Class<? extends Annotation> annotationClass, Annotation annot) {
		return annot.annotationType() == annotationClass;
	}

	private boolean checkModelOf(Class<?> cls) {
		try {
			// sourceExporter cannot be null when this method is called
			ModelId containerOfCls = sourceExporter.getModelOf(cls);
			if (!containerOfCls.equals(containingModel)) {
				if (containingModel == null) {
					containingModel = containerOfCls;
				} else {
					problemReporter.elementFromAnotherModels(containingModel,
							containerOfCls, cls);
					return false;
				}
			}
			return true;
		} catch (ElementExportationException e) {
			problemReporter.unknownContainingModel(cls);
			return false;
		}
	}

}
