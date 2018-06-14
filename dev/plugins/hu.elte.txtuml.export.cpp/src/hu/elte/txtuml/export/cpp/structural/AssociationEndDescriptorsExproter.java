package hu.elte.txtuml.export.cpp.structural;

import java.util.List;

import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.UMLPackage;

import hu.elte.txtuml.export.cpp.ICppCompilationUnit;
import hu.elte.txtuml.export.cpp.templates.GenerationNames;
import hu.elte.txtuml.export.cpp.templates.PrivateFunctionalTemplates;
import hu.elte.txtuml.export.cpp.templates.structual.LinkTemplates;

public class AssociationEndDescriptorsExproter implements ICppCompilationUnit {

	private String sourceDestination;
	private List<Association> associations;
	private DependencyExporter dependencies;

	
	public AssociationEndDescriptorsExproter(List<Association> associationList,String sourceDestination) {
		this.associations = associationList;
		this.sourceDestination = sourceDestination;
		dependencies = new DependencyExporter();
	}
	
	
	@Override
	public String getUnitName() {
		return GenerationNames.AssociationNames.AssociationEndDescriptorUnitName;
	}

	@Override
	public String getUnitNamespace() {
		return GenerationNames.Namespaces.ModelNamespace;
	}

	@Override
	public String createUnitCppCode() {
		return "";
	}

	@Override
	public String createUnitHeaderCode() {
		StringBuilder source = new StringBuilder("");

		for (Association assoc : associations) {
			Property e1End = assoc.getMemberEnds().get(0);
			Property e2End = assoc.getMemberEnds().get(1);		
			source.append(assocEndDescriptors(assoc.getName(), e1End, e2End));

		}

		return source.toString();
	}
	
	private String endDescriptor(Property end, String leftEndPointName, String rigthEndPointName) {
		String type = end.getType().getName();
		String name = end.getName();
		if(end.getType().eClass().equals(UMLPackage.Literals.INTERFACE)) {
			dependencies.addHeaderOnlyIncludeDependency(type);
		} else {
			dependencies.addHeaderOnlyDependency(type);
		}
		
		
		return LinkTemplates.createEndPointClass(type, name, leftEndPointName, rigthEndPointName, end.getLower(), end.getUpper());
	}
	
	private String assocEndDescriptors(String assocName, Property leftDescriptor, Property rightDescriptor) {
		StringBuilder descriptors = new StringBuilder("");
		
		descriptors.append(LinkTemplates.assocEndPreDecl(leftDescriptor.getName()));
		descriptors.append(LinkTemplates.assocEndPreDecl(rightDescriptor.getName()));
		descriptors.append(endDescriptor(leftDescriptor, leftDescriptor.getName(), rightDescriptor.getName()));
		descriptors.append(endDescriptor(rightDescriptor, leftDescriptor.getName(), rightDescriptor.getName()));
		
		
		return LinkTemplates.createAssociationDescriptor(assocName, descriptors.toString());
	}

	@Override
	public String getUnitDependencies(UnitType type) {		
		return PrivateFunctionalTemplates.include(GenerationNames.FileNames.AssociationUtilsPath) + 
				dependencies.createDependencyHeaderIncludeCode(GenerationNames.Namespaces.ModelNamespace);
	}

	@Override
	public String getDestination() {
		return sourceDestination;
	}

}
