package hu.elte.txtuml.export.cpp.structural;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.Port;
import org.eclipse.uml2.uml.UMLPackage;

import hu.elte.txtuml.export.cpp.CppExporterUtils;
import hu.elte.txtuml.export.cpp.templates.GenerationNames;
import hu.elte.txtuml.export.cpp.templates.structual.FunctionTemplates;
import hu.elte.txtuml.export.cpp.templates.structual.HeaderTemplates;
import hu.elte.txtuml.export.cpp.templates.structual.HeaderTemplates.HeaderInfo;
import hu.elte.txtuml.export.cpp.templates.structual.PortTemplates;
import hu.elte.txtuml.export.cpp.templates.structual.ObjectDeclDefTemplates;
import hu.elte.txtuml.utils.Pair;

public class PortExporter {
	
	private List<Port> 	ports;
	private String ownerName;
	
	public PortExporter(List<Port> ports, String ownerName) {
		this.ports = ports;
		this.ownerName = ownerName;
	}

	public Set<String> getUsedInterfaces() {
		Set<String> usedInterfaces = new HashSet<>();
		for (Port port : ports) {
			Pair<String,String> interaces = getPortActualInterfaceTypes(port);
			usedInterfaces.add(interaces.getFirst());
			usedInterfaces.add(interaces.getSecond());
		}
		
		return usedInterfaces;
	}
	
	public String crearePortRelatedCodes() {
		StringBuilder source = new StringBuilder("");
		source.append(createPortClassType());
		source.append(createPortDeclerations());
		source.append(FunctionTemplates.functionDecl(PortTemplates.PORTS_INITIALIZER_FUNCTION_NAME));
		return source.toString();
		
	}
	
	public String createPortClassType() {
		if (ports == null || ports.size() == 0) {
			return "";
		}
		StringBuilder portTypes = new StringBuilder("");
		for (Port port : ports) {
			portTypes.append(ObjectDeclDefTemplates.staticPropertyDecl(PortTemplates.portTypeInfoClassName(ownerName), port.getName()));
		}

		return HeaderTemplates.classHeader("", 
				Arrays.asList(PortTemplates.PORT_TYPE_CLASS_INFO_NAME), 
				Collections.emptyList(), 
				portTypes.toString() + PortTemplates.portTypeInfoClassConstructor(ownerName), 
				"", 
				"", 
				new HeaderInfo(PortTemplates.portTypeInfoClassName(ownerName),new HeaderTemplates.RawClassHeaderType()));



	}
	
	public String createPortDeclerations() {
		return ports.stream().map(p -> createinterfacePortCode(p)).reduce("", (d1,d2) -> d1 + d2);
	}
	
	public String createInitPortsCode() {
		return ports.stream().map(p -> crteaInterfacePortDefinitionCode(p)).reduce("", (d1,d2) -> d1 + d2);
	}
	
	public String createPortTypeInfoDefinitions() {
		StringBuilder source = new StringBuilder("");
		Integer id = PortTemplates.ANY_PORT_DEFAULT_VALUE + 1;
		for(Port port : ports) {
			source.append(PortTemplates.portTypeInfoDef(port.getName(), ownerName, id));
			id++;
		}
		
		return source.toString();
	}
	
	private String crteaInterfacePortDefinitionCode(Port port) {
		assert(port != null && isInterfacePort(port));
		String portTypeName = getPortTypeName(port, true);
		Pair<String,String> interfaces = getPortActualInterfaceTypes(port);
		List<String> parameters = new ArrayList<>();
		if(port.isBehavior()) {
			parameters.add(PortTemplates.portTypeInfoName(port.getName(), ownerName));
			parameters.add(GenerationNames.PointerAndMemoryNames.Self);
		}
		return ObjectDeclDefTemplates.setAllocatedObjectToObjectVariable(portTypeName, 
				Optional.of(Arrays.asList(interfaces.getFirst(),interfaces.getSecond())), 
				port.getName(), Optional.of(parameters), true);
	}
	private String createinterfacePortCode(Port port) {
		assert(port != null && isInterfacePort(port));
		String portTypeName = getPortTypeName(port, false);
		Pair<String,String> interfaces = getPortActualInterfaceTypes(port);		
		return ObjectDeclDefTemplates.propertyDecl(portTypeName,port.getName(),"",
				Optional.of(Arrays.asList(interfaces.getFirst(),interfaces.getSecond())), ObjectDeclDefTemplates.VariableType.SharedPtr);
	}
	
	private Pair<String,String> getPortActualInterfaceTypes(Port port) {
		Interface portInf = (Interface) port.getType();
		String actualProvidedInfName = CppExporterUtils.getFirstGeneralClassName(portInf);
		String actualRequiredInfName = CppExporterUtils.getUsedInterfaceName(portInf);
				
		return new Pair<String,String>(actualProvidedInfName,actualRequiredInfName);
		
	}
	private String getPortTypeName(Port port, Boolean alloc) {
		StringBuilder baseType = new StringBuilder(port.isBehavior()? 
				PortTemplates.BehaviorPortTypeName : 
				PortTemplates.PortTypeName);
		if(alloc) {
			baseType.append("Impl");
		}
		
		return baseType.toString();

	}
	public boolean isInterfacePort(Port port) {
		return port.getType().eClass().equals(UMLPackage.Literals.INTERFACE);
	}
}
