package hu.elte.txtuml.export.papyrus.diagrams.clazz.impl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

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
import org.eclipse.uml2.uml.UMLFactory;
import org.junit.Before;
import org.junit.Test;

import hu.elte.txtuml.export.diagrams.common.layout.IDiagramElementsMapper;

public class ClassDiagramElementsProviderImplTest {

	private List<Class> classes;
	private List<DataType> datatypes;
	private List<Enumeration> enumerations;
	private List<InstanceSpecification> instanceSpecifications;
	private List<Interface> interfaces;
	private List<Package> packages;
	private List<Model> models;
	private List<Comment> comments;
	private List<Signal> singals;
	private List<Association> assocations;
	private List<Generalization> generalizations;
	private List<Realization> realiziations;
	private List<InterfaceRealization> interfaceRealizations;

	private ClassDiagramElementsProviderImpl instance;

	@Before
	public void setUp() throws Exception {
		classes = Arrays.asList(UMLFactory.eINSTANCE.createClass(), UMLFactory.eINSTANCE.createClass());
		datatypes = Arrays.asList(UMLFactory.eINSTANCE.createDataType(), UMLFactory.eINSTANCE.createDataType());
		enumerations = Arrays.asList(UMLFactory.eINSTANCE.createEnumeration(),
				UMLFactory.eINSTANCE.createEnumeration());
		instanceSpecifications = Arrays.asList(UMLFactory.eINSTANCE.createInstanceSpecification(),
				UMLFactory.eINSTANCE.createInstanceSpecification());
		interfaces = Arrays.asList(UMLFactory.eINSTANCE.createInterface(), UMLFactory.eINSTANCE.createInterface());
		packages = Arrays.asList(UMLFactory.eINSTANCE.createPackage(), UMLFactory.eINSTANCE.createPackage());
		models = Arrays.asList(UMLFactory.eINSTANCE.createModel(), UMLFactory.eINSTANCE.createModel());
		comments = Arrays.asList(UMLFactory.eINSTANCE.createComment(), UMLFactory.eINSTANCE.createComment());
		singals = Arrays.asList(UMLFactory.eINSTANCE.createSignal(), UMLFactory.eINSTANCE.createSignal());
		assocations = Arrays.asList(UMLFactory.eINSTANCE.createAssociation(), UMLFactory.eINSTANCE.createAssociation());
		generalizations = Arrays.asList(UMLFactory.eINSTANCE.createGeneralization(),
				UMLFactory.eINSTANCE.createGeneralization());
		realiziations = Arrays.asList(UMLFactory.eINSTANCE.createRealization(),
				UMLFactory.eINSTANCE.createRealization());
		interfaceRealizations = Arrays.asList(UMLFactory.eINSTANCE.createInterfaceRealization(),
				UMLFactory.eINSTANCE.createInterfaceRealization());

		
		Collection<Element> nodes = new ArrayList<>();
		nodes.addAll(classes);
		nodes.addAll(datatypes);
		nodes.addAll(enumerations);
		nodes.addAll(instanceSpecifications);
		nodes.addAll(interfaces);
		nodes.addAll(packages);
		nodes.addAll(models);
		nodes.addAll(comments);
		nodes.addAll(singals);
		
		Collection<Element> connections = new ArrayList<>();
		connections.addAll(assocations);
		connections.addAll(generalizations);
		connections.addAll(realiziations);
		connections.addAll(interfaceRealizations);
		
		IDiagramElementsMapper mapper = mock(IDiagramElementsMapper.class);
		when(mapper.getConnections()).thenReturn(connections);
		when(mapper.getNodes()).thenReturn(nodes);

		instance = new ClassDiagramElementsProviderImpl(mapper);
	}

	@Test
	public void testGetClasses() {
		//when
		Collection<Class> result = instance.getClasses();
		
		//then
		assertThat(result, hasItems(classes.get(0), classes.get(1)));
	}

	@Test
	public void testGetDataTypes() {
		//when
		Collection<DataType> result = instance.getDataTypes();
		
		//then
		assertThat(result, hasItems(datatypes.get(0), datatypes.get(1)));
	}

	@Test
	public void testGetEnumerations() {
		//when
		Collection<Enumeration> result = instance.getEnumerations();
				
		//then
		assertThat(result, hasItems(enumerations.get(0), enumerations.get(1)));
	}

	@Test
	public void testGetInstanceSpecifications() {
		//when
		Collection<InstanceSpecification> result = instance.getInstanceSpecifications();
				
		//then
		assertThat(result, hasItems(instanceSpecifications.get(0), instanceSpecifications.get(1)));
	}

	@Test
	public void testGetInterfaces() {
		//when
		Collection<Interface> result = instance.getInterfaces();
				
		//then
		assertThat(result, hasItems(interfaces.get(0), interfaces.get(1)));
	}

	@Test
	public void testGetModels() {
		//when
		Collection<Model> result = instance.getModels();
				
		//then
		assertThat(result, hasItems(models.get(0), models.get(1)));
	}

	@Test
	public void testGetPackages() {
		//when
		Collection<Package> result = instance.getPackages();
				
		//then
		assertThat(result, hasItems(packages.get(0), packages.get(1)));
	}

	@Test
	public void testGetComments() {
		//when
		Collection<Comment> result = instance.getComments();
				
		//then
		assertThat(result, hasItems(comments.get(0), comments.get(1)));
	}

	@Test
	public void testGetSignals() {
		//when
		Collection<Signal> result = instance.getSignals();
				
		//then
		assertThat(result, hasItems(singals.get(0), singals.get(1)));
	}

	@Test
	public void testGetAssociations() {
		//when
		Collection<Association> result = instance.getAssociations();
				
		//then
		assertThat(result, hasItems(assocations.get(0), assocations.get(1)));
	}

	@Test
	public void testGetGeneralizations() {
		//when
		Collection<Generalization> result = instance.getGeneralizations();
				
		//then
		assertThat(result, hasItems(generalizations.get(0), generalizations.get(1)));
	}

	@Test
	public void testGetInterfaceRealizations() {
		//when
		Collection<InterfaceRealization> result = instance.getInterfaceRealizations();
				
		//then
		assertThat(result, hasItems(interfaceRealizations.get(0), interfaceRealizations.get(1)));
	}

	@Test
	public void testGetRealizations() {
		//when
		Collection<Realization> result = instance.getRealizations();
				
		//then
		assertThat(result, hasItems(realiziations.get(0), realiziations.get(1)));
	}

}
