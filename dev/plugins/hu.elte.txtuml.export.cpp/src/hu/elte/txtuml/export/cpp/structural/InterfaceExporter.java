package hu.elte.txtuml.export.cpp.structural;

import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.Signal;

import hu.elte.txtuml.export.cpp.templates.GenerationNames;
import hu.elte.txtuml.export.cpp.templates.PrivateFunctionalTemplates;
import hu.elte.txtuml.export.cpp.templates.statemachine.EventTemplates;
import hu.elte.txtuml.export.cpp.templates.structual.InterfaceTemplates;

public class InterfaceExporter extends StructuredElementExporter<Interface> {

	public InterfaceExporter(Interface structuredElement, String name, String dest) {
		super(structuredElement, name, dest);
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
		List<Signal> receptionSignals = structuredElement.getOwnedReceptions().stream().map(r -> r.getSignal())
				.collect(Collectors.toList());

		String interfaceDecl = InterfaceTemplates.createInterface(structuredElement.getName(), receptionSignals);
		
		return interfaceDecl;
	}

	@Override
	public String getUnitDependencies(UnitType type) {
		
		return PrivateFunctionalTemplates.include(EventTemplates.EventHeaderName)
				+ PrivateFunctionalTemplates.include(GenerationNames.FileNames.InterfaceUtilsPath);
	}

}
