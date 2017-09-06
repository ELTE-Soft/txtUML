package hu.elte.txtuml.export.fmu;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.TypeLiteral;

import hu.elte.txtuml.api.deployment.fmi.FMU;
import hu.elte.txtuml.api.deployment.fmi.FMUInput;
import hu.elte.txtuml.api.deployment.fmi.FMUOutput;
import hu.elte.txtuml.api.deployment.fmi.InitialBooleanValue;
import hu.elte.txtuml.api.deployment.fmi.InitialIntegerValue;
import hu.elte.txtuml.api.deployment.fmi.InitialRealValue;
import hu.elte.txtuml.utils.eclipse.NotFoundException;
import hu.elte.txtuml.utils.eclipse.ProjectUtils;
import hu.elte.txtuml.utils.jdt.SharedUtils;

public class FMUExportGovernor {

	private static final List<String> INITIAL_ANNOT_NAMES = Arrays.asList(InitialBooleanValue.class.getCanonicalName(),
				InitialIntegerValue.class.getCanonicalName(), InitialRealValue.class.getCanonicalName());

	public FMUConfig extractFMUConfig(String projectName, String fmuDescription) throws NotFoundException, JavaModelException, IOException {
		
		IJavaProject javaProject = ProjectUtils.findJavaProject(projectName);
		FMUConfig config = new FMUConfig();
		
		config.guid = Long.toHexString(new Random().nextLong());
		
		IType descType = javaProject.findType(fmuDescription);
		IPath path = descType.getCompilationUnit().getResource().getLocation();
		CompilationUnit compUnit = SharedUtils.parseJavaSource(path.toFile(), javaProject);
		config.inputSignalConfig = Optional.empty();
		config.outputSignalConfig = Optional.empty();
		config.initialValues = new HashMap<>();
		
		for (Object object : compUnit.types()) {
			TypeDeclaration cls = (TypeDeclaration) object;
			List<?> mods = cls.modifiers();
			for (Object mod : mods) {
				if (mod instanceof NormalAnnotation) {
					NormalAnnotation annotMod = (NormalAnnotation) mod;
					ITypeBinding bind = annotMod.resolveTypeBinding();
					if (bind.getQualifiedName().equals(FMU.class.getCanonicalName())) {
						config.umlClassName = getAnnotValue(annotMod, "fmuClass");
					} else if (bind.getQualifiedName().equals(FMUInput.class.getCanonicalName())) {
						String inputSignalName = getAnnotValue(annotMod, "inputSignal");
						config.inputSignalConfig = Optional.of(inputSignalName);
						config.inputVariables = loadClassMembers(javaProject, inputSignalName);
					} else if (bind.getQualifiedName().equals(FMUOutput.class.getCanonicalName())) {
						config.outputSignalConfig = Optional.of(getAnnotValue(annotMod, "outputSignal"));
						config.outputVariables = loadClassMembers(javaProject, config.outputSignalConfig.get());
					} else if (INITIAL_ANNOT_NAMES.contains(bind.getQualifiedName())) {
						config.initialValues.put(getAnnotValue(annotMod, "variableName"), getAnnotValue(annotMod, "value"));
					}
				}
			}
		}

		return config;
	}
	
	public String getAnnotValue(NormalAnnotation annotMod, String valueName) {
		for (Object annotValue : annotMod.values()) {
			MemberValuePair pair = (MemberValuePair) annotValue;
			if (pair.getName().getIdentifier().equals(valueName)) {
				if (pair.getValue() instanceof TypeLiteral) {
					return ((TypeLiteral) pair.getValue()).getType().resolveBinding().getQualifiedName();
				} else if (pair.getValue() instanceof StringLiteral) {
					return ((StringLiteral) pair.getValue()).getLiteralValue();
				} else {
					return pair.getValue().toString();
				}
			}
		}
		return null;
	}
	

	private List<VariableDefinition> loadClassMembers(IJavaProject javaProject, String typeName) throws JavaModelException {
		IType signalType = javaProject.findType(typeName);
		List<VariableDefinition> ret = new LinkedList<>();
		for (IField field : signalType.getFields()) {
			 ret.add(new VariableDefinition(field.getElementName(), VariableType.fromJavaType(field.getTypeSignature())));
		}
		return ret;
	}
	
}
