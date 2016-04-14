package hu.elte.txtuml.export.papyrus.elementproviders;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Comment;
import org.eclipse.uml2.uml.DataType;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Enumeration;
import org.eclipse.uml2.uml.Generalization;
import org.eclipse.uml2.uml.InstanceSpecification;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.InterfaceRealization;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Realization;
import org.eclipse.uml2.uml.Signal;

import hu.elte.txtuml.export.papyrus.layout.txtuml.TxtUMLElementsMapper;
import hu.elte.txtuml.layout.export.DiagramExportationReport;

public class TxtUMLClassDiagramElementsProvider implements ClassDiagramElementsProvider {

	private DiagramExportationReport report;
	private List<Element> nodes;
	private List<Element> connections;

	public TxtUMLClassDiagramElementsProvider(DiagramExportationReport report, TxtUMLElementsMapper mapper) {
		this.report = report;
		cacheNodes(mapper);
		cacheConnections(mapper);
	}

	private void cacheNodes(TxtUMLElementsMapper mapper) {
		this.nodes = mapper.getNodes(this.report);
	}

	private void cacheConnections(TxtUMLElementsMapper mapper) {
		this.connections = mapper.getConnections(this.report);
	}

	@Override
	@SuppressWarnings("unchecked")
	public Collection<Class> getClasses() {
		List<Class> classes = (List<Class>) (Object) this.nodes.stream().filter((e) -> e instanceof Class)
				.collect(Collectors.toList());
		return classes;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Collection<DataType> getDataTypes() {
		List<DataType> datatypes = (List<DataType>) (Object) this.nodes.stream().filter((e) -> e instanceof DataType)
				.collect(Collectors.toList());
		return datatypes;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Collection<Enumeration> getEnumerations() {
		List<Enumeration> enumerations = (List<Enumeration>) (Object) this.nodes.stream()
				.filter((e) -> e instanceof Enumeration).collect(Collectors.toList());
		return enumerations;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Collection<InstanceSpecification> getInstanceSpecifications() {
		List<InstanceSpecification> instanceSpecifications = (List<InstanceSpecification>) (Object) this.nodes.stream()
				.filter((e) -> e instanceof InstanceSpecification).collect(Collectors.toList());
		return instanceSpecifications;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Collection<Interface> getInterfaces() {
		List<Interface> interfaces = (List<Interface>) (Object) this.nodes.stream()
				.filter((e) -> e instanceof Interface).collect(Collectors.toList());
		return interfaces;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Collection<Model> getModels() {
		List<Model> models = (List<Model>) (Object) this.nodes.stream().filter((e) -> e instanceof Model)
				.collect(Collectors.toList());
		return models;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Collection<Package> getPackages() {
		List<Package> packages = (List<Package>) (Object) this.nodes.stream().filter((e) -> e instanceof Package)
				.collect(Collectors.toList());
		return packages;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Collection<Comment> getComments() {
		List<Comment> comments = (List<Comment>) (Object) this.nodes.stream().filter((e) -> e instanceof Comment)
				.collect(Collectors.toList());
		return comments;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Collection<Signal> getSignals() {
		List<Signal> singals = (List<Signal>) (Object) this.nodes.stream().filter((e) -> e instanceof Signal)
				.collect(Collectors.toList());
		return singals;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Collection<Association> getAssociations() {
		List<Association> associations = (List<Association>) (Object) this.connections.stream()
				.filter((e) -> e instanceof Association).collect(Collectors.toList());
		return associations;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Collection<Generalization> getGeneralizations() {
		List<Generalization> generalizations = (List<Generalization>) (Object) this.connections.stream()
				.filter((e) -> e instanceof Generalization).collect(Collectors.toList());
		return generalizations;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Collection<InterfaceRealization> getInterfaceRealizations() {
		List<InterfaceRealization> intarfaceRealizations = (List<InterfaceRealization>) (Object) this.connections.stream()
				.filter((e) -> e instanceof InterfaceRealization).collect(Collectors.toList());
		return intarfaceRealizations;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Collection<Realization> getRealizations() {
		List<Realization> realizations = (List<Realization>) (Object) this.connections.stream()
				.filter((e) -> e instanceof Realization).collect(Collectors.toList());
		return realizations;
	}

}
