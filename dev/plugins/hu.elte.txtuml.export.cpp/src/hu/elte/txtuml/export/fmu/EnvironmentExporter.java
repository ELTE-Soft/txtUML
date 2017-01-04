package hu.elte.txtuml.export.fmu;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

import hu.elte.txtuml.export.cpp.Uml2ToCppExporter;

public class EnvironmentExporter {

	public void export(Path projectLoc, FMUConfig fmuConfig) throws IOException, URISyntaxException {
		Bundle bundle = Platform.getBundle(Uml2ToCppExporter.PROJECT_NAME);
		URL fileURL = bundle.getEntry("fmuResources" + IPath.SEPARATOR + "FMUEnvironment.cpp");
		String template = new String(Files.readAllBytes(Paths.get(FileLocator.toFileURL(fileURL).toURI())));
		StringBuffer sb = new StringBuffer();
		Matcher matcher = Pattern.compile("\\$[a-zA-Z]+").matcher(template);

		List<VariableDefinition> allVars = new LinkedList<>();
		allVars.addAll(fmuConfig.inputVariables);
		allVars.addAll(fmuConfig.outputVariables);
		
		while (matcher.find()) {
			if (matcher.group().equals("$fmuclass")) {
				matcher.appendReplacement(sb, unqualify(fmuConfig.umlClassName));
			} else if (matcher.group().equals("$controlevent")) {
				matcher.appendReplacement(sb, unqualify(fmuConfig.outputSignalConfig.get()));
			} else if (matcher.group().equals("$cyclesignal")) {
				matcher.appendReplacement(sb, unqualify(fmuConfig.inputSignalConfig.get()));
			} else if (matcher.group().equals("$valueattributes")) {
				String replacement = generateVariableAttributes(allVars);
				matcher.appendReplacement(sb, replacement);
			} else if (matcher.group().equals("$cyclesignalmembers")) {
				String replacement = generateCycleSignalMembers(fmuConfig.inputVariables);
				matcher.appendReplacement(sb, replacement);
			} else if (matcher.group().equals("$setoutputvariables")) {
				String replacement = generateSetOutputVariables(fmuConfig.outputVariables);
				matcher.appendReplacement(sb, replacement);
			} else if (matcher.group().equals("$outputssize")) {
				matcher.appendReplacement(sb, Integer.toString(fmuConfig.outputVariables.size()));
			} else if (matcher.group().equals("$variableoffsets")) {
				String replacement = generateVariableOffsets(allVars);
				matcher.appendReplacement(sb, replacement);
			}
		}
		matcher.appendTail(sb);
		Files.write(projectLoc.resolve("fmu/FMUEnvironment.cpp"), sb.toString().getBytes());
	}
	
	public void exportHeader(Path projectLoc, FMUConfig fmuConfig) throws IOException, URISyntaxException {
		Bundle bundle = Platform.getBundle(Uml2ToCppExporter.PROJECT_NAME);
		URL fileURL = bundle.getEntry("fmuResources" + IPath.SEPARATOR + "FMUEnvironment.hpp");
		String template = new String(Files.readAllBytes(Paths.get(FileLocator.toFileURL(fileURL).toURI())));
		StringBuffer sb = new StringBuffer();
		Matcher matcher = Pattern.compile("\\$[a-zA-Z]+").matcher(template);

		List<VariableDefinition> allVars = new LinkedList<>();
		allVars.addAll(fmuConfig.inputVariables);
		allVars.addAll(fmuConfig.outputVariables);
		
		while (matcher.find()) {
			if (matcher.group().equals("$fmuclass")) {
				matcher.appendReplacement(sb, unqualify(fmuConfig.umlClassName));
			}
		}
		matcher.appendTail(sb);
		Files.write(projectLoc.resolve("fmu/FMUEnvironment.hpp"), sb.toString().getBytes());
	}
	
	private String unqualify(String name) {
		int lastIndexOf = name.lastIndexOf('.');
		if (lastIndexOf != -1) {
			return name.substring(lastIndexOf + 1, name.length());
		} else {
			return name;
		}
	}

	private String generateVariableAttributes(List<VariableDefinition> variables) {
		StringBuilder sb = new StringBuilder();
		for (VariableDefinition var : variables) {
			sb.append(getVarTypeName(var.type)).append(" ").append(var.name).append(";\n");
		}
		return sb.toString();
	}

	private String getVarTypeName(VariableType type) {
		switch (type) {
		case BOOLEAN:
			return "fmi2Boolean";
		case INTEGER:
			return "fmi2Integer";
		case REAL:
			return "fmi2Real";
		case STRING:
			return "fmi2String";
		}
		return "";
	}

	private String generateCycleSignalMembers(List<VariableDefinition> inputVariables) {
		StringBuilder sb = new StringBuilder();
		if (inputVariables.size() > 0) {
			sb.append("fmu->fmu_env." + inputVariables.get(0).name);
		}
		for (int i = 1; i < inputVariables.size(); i++) {
			sb.append(", ").append("fmu->fmu_env.").append(inputVariables.get(i).name);
		}
		return sb.toString();
	}

	private String generateSetOutputVariables(List<VariableDefinition> variables) {
		StringBuilder sb = new StringBuilder();
		for (VariableDefinition var : variables) {
			sb.append("vars->").append(var.name).append(" = myevent->").append(var.name).append(";\n");
		}
		return sb.toString();
	}

	private String generateVariableOffsets(List<VariableDefinition> variables) {
		StringBuilder sb = new StringBuilder();
		if (variables.size() > 0) {
			sb.append("offsetof(fmu_variables,").append(variables.get(0).name).append(")");
		}
		for (int i = 1; i < variables.size(); i++) {
			sb.append(", ").append("offsetof(fmu_variables,").append(variables.get(i).name).append(")");
		}
		return sb.toString();
	}

}
