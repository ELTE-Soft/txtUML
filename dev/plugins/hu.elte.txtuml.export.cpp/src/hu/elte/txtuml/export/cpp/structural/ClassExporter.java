package hu.elte.txtuml.export.cpp.structural;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Region;
import org.eclipse.uml2.uml.StateMachine;
import org.eclipse.uml2.uml.UMLPackage;

import hu.elte.txtuml.utils.Pair;
import hu.elte.txtuml.export.cpp.Shared;
import hu.elte.txtuml.export.cpp.templates.GenerationTemplates;

public class ClassExporter extends StructuredElementExporter<Class> {

	private List<String> subSubMachines;
	private List<String> additionalSourcesNames;
	private AssociationExporter associationExporter;
	private ConstructorExporter constructorExporter;

	private Shared shared;

	private StateMachineExporter stateMachineExporter;
	private SubStateMachineExporter subStateMachineExporter;

	private int poolId;

	public ClassExporter() {
		shared = new Shared();
	}

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
		stateMachineExporter = new StateMachineExporter();
		additionalSourcesNames = new ArrayList<String>();
		subSubMachines = new LinkedList<String>();

		shared.setModelElements(structuredElement.allOwnedElements());

		stateMachineExporter.setName(name);
		stateMachineExporter.setStateMachineThreadPoolId(poolId);

		createSource(sourceDestination);

	}

	public void setPoolId(int poolId) {
		this.poolId = poolId;
	}

	public List<String> getSubmachines() {
		return stateMachineExporter.getSubmachines();
	}

	private void createSource(String dest) throws FileNotFoundException, UnsupportedEncodingException {
		String source;
		associationExporter.exportAssocations(structuredElement.getOwnedAttributes());
		stateMachineExporter.createStateMachineRegion(structuredElement);
		if (stateMachineExporter.ownStateMachine()) {
			stateMachineExporter.init();
			stateMachineExporter.searchInitialState();
			stateMachineExporter.createMachine();

			for (Map.Entry<String, Pair<String, Region>> entry : stateMachineExporter.getSubMachineMap().entrySet()) {
				subStateMachineExporter = new SubStateMachineExporter();
				subStateMachineExporter.setRegion(entry.getValue().getSecond());
				subStateMachineExporter.setName(entry.getValue().getFirst());
				subStateMachineExporter.setParentClass(name);
				subStateMachineExporter.createSubSmSource(dest);
				subSubMachines.addAll(subStateMachineExporter.getSubmachines());
			}
		}

		source = createClassHeaderSource();
		String externalDeclerations = associationExporter.createLinkFunctionDeclerations(name);
		Shared.writeOutSource(dest, GenerationTemplates.headerName(name),
				Shared.format(GenerationTemplates.headerGuard(source + externalDeclerations, name)));

		source = createClassCppSource();
		Shared.writeOutSource(dest, GenerationTemplates.sourceName(name),
				Shared.format(GenerationTemplates.cppInclude(name) + getAllDependency(false) + source));
	}

	private String createClassHeaderSource() {
		String source = "";
		StringBuilder privateParts = new StringBuilder(
				super.createPrivateAttrbutes() + super.createPrivateOperationsDeclerations());
		StringBuilder protectedParts = new StringBuilder(
				super.createProtectedAttributes() + super.createProtectedOperationsDeclerations());
		StringBuilder publicParts = new StringBuilder(
				super.createPublicAttributes() + super.createPublicOperationDecelerations());

		publicParts.append(constructorExporter.exportConstructorDeclareations(name));
		publicParts.append(GenerationTemplates.destructorDecl(name));

		publicParts.append("\n" + associationExporter.createAssociationMemeberDeclerationsCode());

		publicParts
				.append(GenerationTemplates.templateLinkFunctionGeneralDef(GenerationTemplates.LinkFunctionType.Link));
		publicParts.append(
				GenerationTemplates.templateLinkFunctionGeneralDef(GenerationTemplates.LinkFunctionType.Unlink));

		if (stateMachineExporter.ownStateMachine()) {

			publicParts.append(stateMachineExporter.createStateEnumCode());
			privateParts.append(stateMachineExporter.createStateMachineRelatedHeadedDeclerationCodes());

			if (stateMachineExporter.ownSubMachine()) {
				source = GenerationTemplates
						.simpleStateMachineClassHeader(getAllDependency(true), name, getBaseClass(), null,
								publicParts.toString(), protectedParts.toString(), privateParts.toString(), true)
						.toString();
			} else {
				source = GenerationTemplates.hierarchicalStateMachineClassHeader(getAllDependency(true), name,
						getBaseClass(), getSubmachines(), publicParts.toString(), protectedParts.toString(),
						privateParts.toString(), true).toString();
			}
		} else {
			source = GenerationTemplates.classHeader(getAllDependency(true), name, getBaseClass(),
					publicParts.toString(), protectedParts.toString(), privateParts.toString(), true).toString();
		}
		return source;
	}

	private String createClassCppSource() {
		StringBuilder source = new StringBuilder("");
		List<StateMachine> smList = new ArrayList<StateMachine>();
		shared.getTypedElements(smList, UMLPackage.Literals.STATE_MACHINE);

		if (stateMachineExporter.ownStateMachine()) {
			source.append(stateMachineExporter.createStateMachineRelatedCppSourceCodes());

		}

		source.append(super.createOperationDefinitions());
		source.append(constructorExporter.exportConstructorsDefinitions(name, stateMachineExporter.ownStateMachine()));
		source.append(stateMachineExporter.ownStateMachine() ? GenerationTemplates.destructorDef(name, true)
				: GenerationTemplates.destructorDef(name, false));

		return source.toString();
	}

	private String getAllDependency(Boolean isHeader) {
		StringBuilder source = new StringBuilder("");
		dependencyExporter.addDependecies(associationExporter.getAssociatedPropertyTypes());

		if (stateMachineExporter.ownStateMachine()) {
			for (Map.Entry<String, Pair<String, Region>> entry : stateMachineExporter.getSubMachineMap().entrySet()) {
				dependencyExporter.addDependecy(entry.getValue().getFirst());
			}

		}

		source.append(dependencyExporter.createDependencyIncudesCode(isHeader));

		if (getBaseClass() != null) {
			source.append(GenerationTemplates.cppInclude(getBaseClass()));
		}

		if (!isHeader) {
			if (stateMachineExporter.ownStateMachine()) {
				source.append(GenerationTemplates.cppInclude(GenerationTemplates.DeploymentHeader));
				source.append(GenerationTemplates.debugOnlyCodeBlock(GenerationTemplates.StandardIOinclude));
			}
			if (associationExporter.ownAssociation()) {
				source.append(GenerationTemplates.cppInclude(GenerationTemplates.AssociationsStructuresHreaderName));

			}
			// TODO analyze what dependency is necessary..
			source.append(GenerationTemplates
					.cppInclude(GenerationTemplates.RuntimePath + GenerationTemplates.StandardFunctionsHeader));
			source.append(GenerationTemplates
					.cppInclude(GenerationTemplates.RuntimePath + GenerationTemplates.TimerInterfaceHeader));
			source.append(
					GenerationTemplates.cppInclude(GenerationTemplates.RuntimePath + GenerationTemplates.TimerHeader));
		} else {
			if (associationExporter.ownAssociation()) {
				source.append(GenerationTemplates
						.cppInclude(GenerationTemplates.RuntimePath + GenerationTemplates.AssocationHeader));
			}

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
