package hu.elte.txtuml.export.cpp.structural;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.Signal;

import hu.elte.txtuml.export.cpp.CppExporterUtils;
import hu.elte.txtuml.export.cpp.templates.GenerationNames;
import hu.elte.txtuml.export.cpp.templates.GenerationTemplates;
import hu.elte.txtuml.export.cpp.templates.PrivateFunctionalTemplates;
import hu.elte.txtuml.export.cpp.templates.statemachine.EventTemplates;
import hu.elte.txtuml.export.cpp.templates.structual.HeaderTemplates;
import hu.elte.txtuml.export.cpp.templates.structual.InterfaceTemplates;

public class InterfaceExporter extends StructuredElementExporter<Interface> {

	@Override
	public void exportStructuredElement(Interface structuredElement, String sourceDestination)
			throws FileNotFoundException, UnsupportedEncodingException {

		this.name = structuredElement.getName();
		List<Signal> receptionSignals = structuredElement.getOwnedReceptions().stream().map(r -> r.getSignal())
				.collect(Collectors.toList());

		String interfaceDecl = InterfaceTemplates.createInterface(structuredElement.getName(), receptionSignals);

		String source = GenerationTemplates.putNamespace(interfaceDecl, GenerationNames.Namespaces.ModelNamespace);
		String dependency = PrivateFunctionalTemplates.include(EventTemplates.EventHeaderName)
				+ PrivateFunctionalTemplates.include(GenerationNames.FileNames.InterfaceUtilsPath);
		CppExporterUtils.writeOutSource(sourceDestination, GenerationTemplates.headerName(name),
				HeaderTemplates.headerGuard(dependency + source, name));

	}

}
