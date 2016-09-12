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

	private StateMachineExporter stateMachineExporter;
	private SubStateMachineExporter subStateMachineExporter;

	public ClassExporter() {
	}

	public ClassExporter(Class cls, String name, int poolId) {
		super(cls, name);
		init(cls, name, poolId);
	}

	public void init(Class cls, String name, int poolId) {
		super.init(cls, name);
		associationExporter = new AssociationExporter();
		stateMachineExporter = new StateMachineExporter(name, poolId,cls);
		constructorExporter = new ConstructorExporter(structuredElement.getOwnedOperations());

		additionalSourcesNames = new ArrayList<String>();
		subSubMachines = new LinkedList<String>();
	}

	public List<String> getAdditionalSources() {
		return additionalSourcesNames;
	}

	public void createSource(String dest) throws FileNotFoundException, UnsupportedEncodingException {
		String source;
		StringBuilder externalDeclerations = new StringBuilder("");
		associationExporter.exportAssocations(structuredElement.getOwnedAttributes());
		List<StateMachine> smList = new ArrayList<StateMachine>();
		Shared.getTypedElements(smList, structuredElement.allOwnedElements(), UMLPackage.Literals.STATE_MACHINE);
		if (stateMachineExporter.isOwnStateMachine()) {

			for (Map.Entry<String, Pair<String, Region>> entry : stateMachineExporter.getSubMachineMap().entrySet()) {
				subStateMachineExporter = new SubStateMachineExporter(entry.getValue().getSecond(), name,
						entry.getValue().getFirst(), dest);
				subStateMachineExporter.createSubSmSource();
				subSubMachines.addAll(subStateMachineExporter.getSubmachines());
			}
		}

		source = createClassHeaderSource();
		externalDeclerations.append(associationExporter.createLinkFunctionDeclerations(name));
		Shared.writeOutSource(dest, GenerationTemplates.headerName(name),
				GenerationTemplates.headerGuard(source + externalDeclerations.toString(), name));

		source = createClassCppSource();
		Shared.writeOutSource(dest, GenerationTemplates.sourceName(name),
				GenerationTemplates.cppInclude(name) + getAllDependency(false) + source);
	}

	public List<String> getSubmachines() {
		return stateMachineExporter.getSubmachines();
	}

	public void setRealName(String realClassName) {
		this.name = realClassName;

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

		if (associationExporter.ownAssociation()) {
			publicParts.append(
					GenerationTemplates.templateLinkFunctionGeneralDef(GenerationTemplates.LinkFunctionType.Link));
			publicParts.append(
					GenerationTemplates.templateLinkFunctionGeneralDef(GenerationTemplates.LinkFunctionType.Unlink));
		}

		if (stateMachineExporter.isOwnStateMachine()) {

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
		Shared.getTypedElements(smList, structuredElement.allOwnedElements(), UMLPackage.Literals.STATE_MACHINE);

		if (stateMachineExporter.isOwnStateMachine()) {
			source.append(stateMachineExporter.createStateMachineRelatedCppSourceCodes());

		}

		source.append(super.createOperationDefinitions());
		source.append(
				constructorExporter.exportConstructorsDefinitions(name, stateMachineExporter.isOwnStateMachine()));
		source.append(stateMachineExporter.isOwnStateMachine() ? GenerationTemplates.destructorDef(name, true)
				: GenerationTemplates.destructorDef(name, false));

		return source.toString();
	}

	private String getAllDependency(Boolean isHeader) {
		StringBuilder source = new StringBuilder("");
		dependencyExporter.addDependecies(associationExporter.getAssociatedPropertyTypes());

		if (stateMachineExporter.isOwnStateMachine()) {
			for (Map.Entry<String, Pair<String, Region>> entry : stateMachineExporter.getSubMachineMap().entrySet()) {
				dependencyExporter.addDependecy(entry.getValue().getFirst());
			}

		}

		source.append(dependencyExporter.createDependencyIncudesCode(isHeader));

		if (getBaseClass() != null) {
			source.append(GenerationTemplates.cppInclude(getBaseClass()));
		}

		if (!isHeader) {
			if (stateMachineExporter.isOwnStateMachine()) {
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
			if(associationExporter.ownAssociation()) {
				source.append(GenerationTemplates.
						cppInclude(GenerationTemplates.RuntimePath + GenerationTemplates.AssocationHeader));
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
