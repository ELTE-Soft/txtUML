package hu.elte.txtuml.export.cpp.structural;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.Port;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.Usage;

import hu.elte.txtuml.export.cpp.CppExporterUtils;
import hu.elte.txtuml.export.cpp.templates.GenerationNames;
import hu.elte.txtuml.export.cpp.templates.GenerationTemplates;
import hu.elte.txtuml.export.cpp.templates.GenerationNames.TypeDelcreationKeywords;
import hu.elte.txtuml.export.cpp.templates.structual.FunctionTemplates;
import hu.elte.txtuml.export.cpp.templates.structual.PortTemplates;
import hu.elte.txtuml.export.cpp.templates.structual.VariableTemplates;
import hu.elte.txtuml.utils.Pair;

public class PortExporter {
	
	private List<Usage> usages;
	private List<Port> 	ports;
	
	public PortExporter(List<Usage> usages, List<Port> 	ports) {
		this.usages = usages;
		this.ports = ports;
	}
	public void createPortSource(String outputDirectory) throws FileNotFoundException, UnsupportedEncodingException {
		CppExporterUtils.writeOutSource(outputDirectory, (PortTemplates.PORT_HEADER),
				CppExporterUtils.format(PortTemplates.portHeaderGuard("")));
	}


	
	public String crearePortRelatedCodes() {
		StringBuilder source = new StringBuilder("");
		source.append(createPortEnumCode());
		source.append(createPortDeclerations());
		source.append(FunctionTemplates.functionDecl(PortTemplates.PORTS_INITIALIZER_FUNCTION_NAME));
		return source.toString();
		
	}
	
	public String createPortEnumCode() {
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
	
	public String createPortDeclerations() {
		return ports.stream().map(p -> createinterfacePrortCode(p)).reduce("", (d1,d2) -> d1 + d2);
	}
	
	public String createInitPortsCode() {
		return ports.stream().map(p -> crteaInterfacePortDefinitionCode(p)).reduce("", (d1,d2) -> d1 + d2);
	}
	
	private String crteaInterfacePortDefinitionCode(Port port) {
		assert(port != null && isInterfacePort(port));
		String portTypeName = getPortTypeName(port);
		Pair<String,String> interfaces = getPortActualInterfaceTypes(port);
		List<String> parameters = new ArrayList<>();
		if(port.isBehavior()) {
			parameters.add(PortTemplates.ponrtEnumName(port.getName()));
			parameters.add(GenerationNames.PointerAndMemoryNames.Self);
		}
		return GenerationTemplates.createObject(portTypeName, port.getName(), 
				Arrays.asList(interfaces.getFirst(),interfaces.getSecond()), parameters, false);
	}
	private String createinterfacePrortCode(Port port) {
		assert(port != null && isInterfacePort(port));
		String portTypeName = getPortTypeName(port);
		Pair<String,String> interfaces = getPortActualInterfaceTypes(port);		
		return VariableTemplates.propertyDecl(portTypeName,port.getName(),"",Arrays.asList(interfaces.getFirst(),interfaces.getSecond()));
	}
	
	private Pair<String,String> getPortActualInterfaceTypes(Port port) {
		Interface portInf = (Interface) port.getType();
		Optional<String> providedInfOptionalName = CppExporterUtils.getFirstGeneralClassName(portInf);
		Optional<String> requiredInfOptionalName = CppExporterUtils.getUsedInterfaceName(usages,portInf);
		String actualProvidedInfName = providedInfOptionalName.isPresent() ? 
				providedInfOptionalName.get() : GenerationNames.InterfaceNames.EmptyInfName;
		String actualRequiredInfName = requiredInfOptionalName.isPresent() ?
				requiredInfOptionalName.get() : GenerationNames.InterfaceNames.EmptyInfName;
				
		return new Pair<String,String>(actualProvidedInfName,actualRequiredInfName);
		
	}
	private String getPortTypeName(Port port) {
		return port.isBehavior()? 
				PortTemplates.BehaviorPortTypeName : 
				PortTemplates.PortTypeName;
	}
	public boolean isInterfacePort(Port port) {
		return port.getType().eClass().equals(UMLPackage.Literals.INTERFACE);
	}
}
