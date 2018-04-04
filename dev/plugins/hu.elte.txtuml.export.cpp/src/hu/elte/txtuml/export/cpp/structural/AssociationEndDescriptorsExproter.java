package hu.elte.txtuml.export.cpp.structural;

import java.util.List;

import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Property;

import hu.elte.txtuml.export.cpp.ICppCompilationUnit;
import hu.elte.txtuml.export.cpp.templates.GenerationNames;
import hu.elte.txtuml.export.cpp.templates.GenerationTemplates;
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
			
			source.append(GenerationTemplates.forwardDeclaration(e1End.getName(), GenerationTemplates.ClassDeclerationType.AssocDescriptor));
			source.append(GenerationTemplates.forwardDeclaration(e2End.getName(), GenerationTemplates.ClassDeclerationType.AssocDescriptor));
			source.append(endDescriptor(e1End, e2End));
			source.append(endDescriptor(e2End, e1End));

		}

		return source.toString();
	}
	
	private String endDescriptor(Property end, Property otherEnd) {
		String type = end.getType().getName();
		String name = end.getName();		
		dependencies.addHeaderOnlyDependency(type);
		
		return LinkTemplates.createEndPointClass(type, name, otherEnd.getName(), end.getLower(), end.getUpper());
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
