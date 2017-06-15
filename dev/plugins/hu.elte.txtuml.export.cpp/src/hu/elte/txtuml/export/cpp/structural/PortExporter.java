package hu.elte.txtuml.export.cpp.structural;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.Port;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.Usage;

import hu.elte.txtuml.export.cpp.CppExporterUtils;
import hu.elte.txtuml.export.cpp.templates.GenerationNames;
import hu.elte.txtuml.export.cpp.templates.GenerationNames.TypeDelcreationKeywords;
import hu.elte.txtuml.export.cpp.templates.structual.PortTemplates;
import hu.elte.txtuml.export.cpp.templates.structual.VariableTemplates;

public class PortExporter {
	
	private List<Usage> usages;
	
	public void createPortSource(String outputDirectory) throws FileNotFoundException, UnsupportedEncodingException {
		CppExporterUtils.writeOutSource(outputDirectory, (PortTemplates.PORT_HEADER),
				CppExporterUtils.format(PortTemplates.portHeaderGuard("")));
	}

	public String createPortEnumCode(List<Port> ports) {
		if (ports == null || ports.size() == 0) {
			return "";
		}

		StringBuilder source = new StringBuilder("\n");
		source.append(TypeDelcreationKeywords.EnumType).append(" ");
		source.append(PortTemplates.PORT_ENUM_NAME);
		source.append(" { ");
		for (Port port : ports) {
			source.append(PortTemplates.ponrtEnumName(port.getName()));
			source.append(",");
		}
		source.deleteCharAt(source.length() - 1);
		source.append(" }; ");

		return source.toString();
	}
	public String createPortDeclerations(List<Usage> usages, List<Port> ports) {
		this.usages = usages;
		return ports.stream().map(p -> createinterfacePrortCode(p)).reduce("", (d1,d2) -> d1 + d2);
	}
	private String createinterfacePrortCode(Port port) {
		assert(port != null && isInterfacePort(port));
		String portTypeName = port.isBehavior() ? 
				PortTemplates.BehaviorPortTypeName : 
				PortTemplates.PortTypeName;
		Interface portInf = (Interface) port.getType();
		Optional<String> providedInfOptionalName = CppExporterUtils.getFirstGeneralClassName(portInf);
		Optional<String> requiredInfOptionalName = CppExporterUtils.getUsedInterfaceName(usages,portInf);
		String actualProvidedInfName = providedInfOptionalName.isPresent() ? 
				providedInfOptionalName.get() : GenerationNames.InterfaceNames.EmptyInfName;
		String actualRequiredInfName = requiredInfOptionalName.isPresent() ?
				requiredInfOptionalName.get() : GenerationNames.InterfaceNames.EmptyInfName;
		
		return VariableTemplates.propertyDecl(portTypeName,port.getName(),"",Arrays.asList(actualProvidedInfName,actualRequiredInfName));
	}
	
	public boolean isInterfacePort(Port port) {
		return port.getType().eClass().equals(UMLPackage.Literals.INTERFACE);
	}
}
