package hu.elte.txtuml.export.cpp.structural;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Region;
import org.eclipse.uml2.uml.StateMachine;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.Usage;

import hu.elte.txtuml.export.cpp.CppExporterUtils;
import hu.elte.txtuml.export.cpp.statemachine.StateMachineExporter;
import hu.elte.txtuml.export.cpp.statemachine.SubStateMachineExporter;
import hu.elte.txtuml.export.cpp.templates.GenerationNames;
import hu.elte.txtuml.export.cpp.templates.GenerationTemplates;
import hu.elte.txtuml.export.cpp.templates.PrivateFunctionalTemplates;
import hu.elte.txtuml.export.cpp.templates.RuntimeTemplates;
import hu.elte.txtuml.export.cpp.templates.statemachine.EventTemplates;
import hu.elte.txtuml.export.cpp.templates.structual.ConstructorTemplates;
import hu.elte.txtuml.export.cpp.templates.structual.FunctionTemplates;
import hu.elte.txtuml.export.cpp.templates.structual.HeaderTemplates;
import hu.elte.txtuml.export.cpp.templates.structual.LinkTemplates;
import hu.elte.txtuml.utils.Pair;

public class ClassExporter extends StructuredElementExporter<Class> {

	private List<String> additionalSourcesNames;
	
	private AssociationExporter associationExporter;
	private ConstructorExporter constructorExporter;

	private StateMachineExporter stateMachineExporter;
	private PortExporter portExporter;
	private SubStateMachineExporter subStateMachineExporter;

	private int poolId;
	private List<Usage> usages;
	
	public List<String> getAdditionalSources() {
		return additionalSourcesNames;
	}

	@Override
	public void exportStructuredElement(Class structuredElement, String sourceDestination)
			throws FileNotFoundException, UnsupportedEncodingException {
		super.init();
		super.setStructuredElement(structuredElement);
		constructorExporter = new ConstructorExporter(structuredElement.getOwnedOperations());
		associationExporter = new AssociationExporter();
		additionalSourcesNames = new ArrayList<String>();
		portExporter = new PortExporter(usages,structuredElement.getOwnedPorts());

		Optional<StateMachine> classOptionalSM = CppExporterUtils.getStateMachine(structuredElement);
		if (classOptionalSM.isPresent()) {
			stateMachineExporter = new StateMachineExporter(classOptionalSM.get());
			stateMachineExporter.setName(name);
			stateMachineExporter.setStateMachineThreadPoolId(poolId);
		}

		createSource(sourceDestination);

	}

	public void setPoolId(int poolId) {
		this.poolId = poolId;
	}
	
	public void setUsages(List<Usage> usages) {
		this.usages = usages;
	}

	public List<String> getSubmachines() {
		if(CppExporterUtils.isStateMachineOwner(structuredElement)) {
			return stateMachineExporter.getSubMachineNameList();
		} else {
			return null;
		}
	}

	private void createSource(String dest) throws FileNotFoundException, UnsupportedEncodingException {
		String source;
		associationExporter.exportAssociations(structuredElement.getOwnedAttributes());
		if (CppExporterUtils.isStateMachineOwner(structuredElement)) {
			stateMachineExporter.createStateMachineRegion(structuredElement);
			stateMachineExporter.createMachine();
			List<String> subMachines = new LinkedList<String>();
			for (Map.Entry<String, Pair<String, Region>> entry : stateMachineExporter.getSubMachineMap().entrySet()) {
				subStateMachineExporter = new SubStateMachineExporter();
				subStateMachineExporter.setRegion(entry.getValue().getSecond());
				subStateMachineExporter.setName(entry.getValue().getFirst());
				subStateMachineExporter.setParentClass(name);
				subStateMachineExporter.createSubSmSource(dest);
				subMachines.addAll(subStateMachineExporter.getSubMachineNameList());
			}
		}

		source = createClassHeaderSource();
		//TODO refactoring the dependency to outside
		String externalDeclerations = associationExporter.createLinkFunctionDeclarations(name);
		CppExporterUtils.writeOutSource(dest, GenerationTemplates.headerName(name),
				CppExporterUtils.format(HeaderTemplates.headerGuard(source + externalDeclerations, name)));

		source = createClassCppSource();
		CppExporterUtils.writeOutSource(dest, GenerationTemplates.sourceName(name),
				CppExporterUtils.format(getAllDependencies(false) + GenerationTemplates.putNamespace(source, GenerationNames.Namespaces.ModelNamespace)));
	}

	private String createClassHeaderSource() {
		String source = "";
		StringBuilder privateParts = new StringBuilder(
				super.createPrivateAttrbutes() + super.createPrivateOperationsDeclarations());
		StringBuilder protectedParts = new StringBuilder(
				super.createProtectedAttributes() + super.createProtectedOperationsDeclarations());
		StringBuilder publicParts = new StringBuilder(
				super.createPublicAttributes() + super.createPublicOperationDeclarations());
		

		publicParts.append(constructorExporter.exportConstructorDeclarations(name));
		publicParts.append(ConstructorTemplates.destructorDecl(name));

		publicParts.append("\n" + associationExporter.createAssociationMemberDeclarationsCode());

		publicParts.append(LinkTemplates.templateLinkFunctionGeneralDef(LinkTemplates.LinkFunctionType.Link));
		publicParts.append(LinkTemplates.templateLinkFunctionGeneralDef(LinkTemplates.LinkFunctionType.Unlink));
		
		publicParts.append(portExporter.crearePortRelatedCodes());
		if (CppExporterUtils.isStateMachineOwner(structuredElement)) {

			publicParts.append(stateMachineExporter.createStateEnumCode());
			privateParts.append(stateMachineExporter.createStateMachineRelatedHeadedDeclarationCodes());

			if (!stateMachineExporter.ownSubMachine()) {
				source = HeaderTemplates
						.simpleStateMachineClassHeader(getAllDependencies(true), name, getBaseClass(), null,
								publicParts.toString(), protectedParts.toString(), privateParts.toString(), true)
						.toString();
			} else {
				source = HeaderTemplates.hierarchicalStateMachineClassHeader(getAllDependencies(true), name,
						getBaseClass(), getSubmachines(), publicParts.toString(), protectedParts.toString(),
						privateParts.toString(), true);
			}
		} else {
			source = HeaderTemplates.classHeader(getAllDependencies(true), name, getBaseClass(), publicParts.toString(),
					protectedParts.toString(), privateParts.toString(), true);
		}
		return source;
	}

	private String createClassCppSource() {
		StringBuilder source = new StringBuilder("");
		List<StateMachine> smList = new ArrayList<StateMachine>();
		CppExporterUtils.getTypedElements(smList, UMLPackage.Literals.STATE_MACHINE,
				structuredElement.allOwnedElements());
		if (CppExporterUtils.isStateMachineOwner(structuredElement)) {
			source.append(stateMachineExporter.createStateMachineRelatedCppSourceCodes());

		}

		source.append(super.createOperationDefinitions());
		source.append(constructorExporter.exportConstructorsDefinitions(name, CppExporterUtils.isStateMachineOwner(structuredElement)));
		source.append(CppExporterUtils.isStateMachineOwner(structuredElement) ? ConstructorTemplates.destructorDef(name, true)
				: ConstructorTemplates.destructorDef(name, false));
		source.append(FunctionTemplates.functionDef(name, GenerationNames.InitiliazetFixFunctionNames.InitPorts, portExporter.createInitPortsCode()));

		return source.toString();
	}

	private String getAllDependencies(Boolean isHeader) {
		StringBuilder source = new StringBuilder("");
		dependencyExporter.addDependencies(associationExporter.getAssociatedPropertyTypes());

		if (CppExporterUtils.isStateMachineOwner(structuredElement)) {
			for (Map.Entry<String, Pair<String, Region>> entry : stateMachineExporter.getSubMachineMap().entrySet()) {
				dependencyExporter.addDependency(entry.getValue().getFirst());
			}

		}

		if (getBaseClass() != null) {
			source.append(PrivateFunctionalTemplates.include(getBaseClass()));
		}

		if (!isHeader) {
			source.append(dependencyExporter.createDependencyCppIncludeCode(name));
			if (CppExporterUtils.isStateMachineOwner(structuredElement)) {
				source.append(PrivateFunctionalTemplates.include(GenerationTemplates.DeploymentHeader));
				source.append(GenerationTemplates.debugOnlyCodeBlock(GenerationTemplates.StandardIOinclude));
			}
			source.append(PrivateFunctionalTemplates.include(EventTemplates.EventHeaderName));
			if (associationExporter.ownAssociation()) {
				source.append(PrivateFunctionalTemplates.include(LinkTemplates.AssociationsStructuresHreaderName));

			}
			// TODO analyze what dependency is necessary..
			source.append(PrivateFunctionalTemplates.include(GenerationNames.FileNames.ActionPath));
			source.append(PrivateFunctionalTemplates.include(GenerationNames.FileNames.StringUtilsPath));
			source.append(PrivateFunctionalTemplates.include(GenerationNames.FileNames.CollectionUtilsPath));
			source.append(PrivateFunctionalTemplates.include(RuntimeTemplates.RTPath + GenerationTemplates.TimerInterfaceHeader));
			source.append(PrivateFunctionalTemplates.include(RuntimeTemplates.RTPath + GenerationTemplates.TimerHeader));
			source.append(PrivateFunctionalTemplates.include(GenerationNames.FileNames.PortUtilsPath));
		} else {
			for (String interfaceDependency : portExporter.getUsedInterfaces()) {
				source.append(PrivateFunctionalTemplates.include(interfaceDependency));
			}
			source.append(PrivateFunctionalTemplates.include(GenerationNames.FileNames.TypesFilePath));
			if (associationExporter.ownAssociation()) {
				source.append(PrivateFunctionalTemplates.include(RuntimeTemplates.RTPath + LinkTemplates.AssocationHeader));
			}
			source.append(GenerationTemplates.putNamespace(dependencyExporter.createDependencyHeaderIncludeCode(), GenerationNames.Namespaces.ModelNamespace));


		}

		source.append("\n");
		return source.toString();
	}

	private String getBaseClass() {
		if (!structuredElement.getGeneralizations().isEmpty()) {
			return structuredElement.getGeneralizations().get(0).getGeneral().getName();
		} else {
			return null;
		}
	}

}
