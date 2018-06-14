package hu.elte.txtuml.export.cpp.structural;

import java.util.List;

import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Property;

import hu.elte.txtuml.export.cpp.ICppCompilationUnit;
import hu.elte.txtuml.export.cpp.templates.GenerationNames;
import hu.elte.txtuml.export.cpp.templates.PrivateFunctionalTemplates;
import hu.elte.txtuml.export.cpp.templates.structual.LinkTemplates;

public class AssociationInstancesExporter implements ICppCompilationUnit {
	private List<Association> associations;
	private String sourceDestination;

	public AssociationInstancesExporter(List<Association> associationList, String sourceDestination) {
		this.associations = associationList;
		this.sourceDestination = sourceDestination;
	}

	@Override
	public String getUnitName() {
		return GenerationNames.AssociationNames.AssociationInstancesUnitName;
	}

	@Override
	public String getUnitNamespace() {
		return GenerationNames.Namespaces.ModelNamespace;
	}

	@Override
	public String createUnitCppCode() {
		StringBuilder source = new StringBuilder("");

		for (Association assoc : associations) {
			Property e1End = assoc.getMemberEnds().get(0);
			Property e2End = assoc.getMemberEnds().get(1);
			String e1Name = e1End.getName();
			String e2Name = e2End.getName();
			source.append(LinkTemplates.associationDef(assoc.getName(), e1Name, e2Name));
		}

		return source.toString();

	}

	@Override
	public String createUnitHeaderCode() {
		StringBuilder source = new StringBuilder("");

		for (Association assoc : associations) {
			Property e1End = assoc.getMemberEnds().get(0);
			Property e2End = assoc.getMemberEnds().get(1);
			String e1Name = e1End.getName();
			String e2Name = e2End.getName();
			source.append(LinkTemplates.associationDecl(assoc.getName(), e1Name, e2Name));
		}

		return source.toString();
	}

	@Override
	public String getUnitDependencies(UnitType type) {
		
		if (type == UnitType.Cpp) {
			return PrivateFunctionalTemplates.include(getUnitName());
		} else if (type == UnitType.Header) {
			return PrivateFunctionalTemplates.include(GenerationNames.FileNames.AssociationUtilsPath)
					+ PrivateFunctionalTemplates.include(GenerationNames.AssociationNames.AssociationEndDescriptorUnitName);

		}

		return "";
	}

	@Override
	public String getDestination() {
		return sourceDestination;
	}

}
