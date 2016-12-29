package hu.elte.txtuml.export.fmu;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

import hu.elte.txtuml.export.cpp.Uml2ToCppExporter;

public class EnvironmentExporter {

	public void export(IPath projectLoc, FMUConfig fmuConfig) throws IOException, URISyntaxException {
		Bundle bundle = Platform.getBundle(Uml2ToCppExporter.PROJECT_NAME);
		URL fileURL = bundle.getEntry("fmuResources" + IPath.SEPARATOR + "FMUEnvironment.cpp");
		String template = new String(Files.readAllBytes(Paths.get(FileLocator.toFileURL(fileURL).toURI())));
		StringBuffer sb = new StringBuffer();
		Matcher matcher = Pattern.compile("\\$[a-zA-Z]+").matcher(template);
		while (matcher.find()) {
			if (matcher.group().equals("$fmuclass")) {
				matcher.appendReplacement(sb, fmuConfig.umlClassName);
			} else if (matcher.group().equals("$controlevent")) {
				matcher.appendReplacement(sb, fmuConfig.outputSignalConfig.get().outputSignalName);
			} else if (matcher.group().equals("$cyclesignal")) {
				matcher.appendReplacement(sb, fmuConfig.inputSignalConfig.get());
			} else if (matcher.group().equals("$setvariables")) {
				String replacement = generateVariableSetList(fmuConfig.outputVariables);
				matcher.appendReplacement(sb, replacement);
			} else if (matcher.group().equals("$envvariabletypes")) {
				String replacement = generateVariableTypeList(fmuConfig.outputVariables);
				matcher.appendReplacement(sb, replacement);
			} else if (matcher.group().equals("$cyclesignalmembers")) {
				String replacement = generateCycleSignalMembers(fmuConfig.inputVariables.size());
				matcher.appendReplacement(sb, replacement);
			}
		}
		matcher.appendTail(sb);
		Files.write(
				Paths.get(projectLoc.toOSString(), Uml2ToCppExporter.GENERATED_CPP_FOLDER_NAME, "FMUEnvironment.cpp"),
				sb.toString().getBytes());
	}

	private String generateVariableSetList(List<VariableDefinition> variables) {
		StringBuilder sb = new StringBuilder();
		int i = 0;
		for (VariableDefinition var : variables) {
			sb.append("values[").append(i++).append("] = event.").append(var.name).append(";\n");			
		}
		return sb.toString();
	}

	private String generateVariableTypeList(List<VariableDefinition> variables) {
		StringBuilder sb = new StringBuilder();
		if (!variables.isEmpty()) {
			sb.append(getVarTypeName(variables.get(0).type));
		}
		for (int i = 1; i < variables.size(); i++) {
			VariableDefinition var = variables.get(i);
			sb.append(", ").append(getVarTypeName(var.type));
		}
		return sb.toString();
	}

	private String getVarTypeName(VariableType type) {
		switch (type) {
		case BOOLEAN:
			return "fmi2BooleanType";
		case INTEGER:
			return "fmi2IntegerType";
		case REAL:
			return "fmi2RealType";
		case STRING:
			return "fmi2StringType";
		}
		return "";	
	}

	private String generateCycleSignalMembers(int inputVariableNum) {
		StringBuilder sb = new StringBuilder();
		if (inputVariableNum > 0) {
			sb.append("fmu->fmu_env->values[0]");
		}
		for (int i = 0; i < inputVariableNum; i++) {
			sb.append(", ").append("fmu->fmu_env->values[").append(i).append("]");
		}
		return sb.toString();
	}

}
