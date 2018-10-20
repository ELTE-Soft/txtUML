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

public class DebuggerExporter {

	public void export(Path projectLoc, FMUConfig fmuConfig) throws IOException, URISyntaxException {
		Bundle bundle = Platform.getBundle(Uml2ToCppExporter.PROJECT_NAME);
		URL fileURL = bundle.getEntry("fmuResources" + IPath.SEPARATOR + "fmudebug.cpp");
		String template = new String(Files.readAllBytes(Paths.get(FileLocator.toFileURL(fileURL).toURI())));
		Matcher matcher = Pattern.compile("\\$[a-zA-Z]+").matcher(template);

		List<VariableDefinition> allVars = new LinkedList<>();
		allVars.addAll(fmuConfig.inputVariables);
		allVars.addAll(fmuConfig.outputVariables);

		StringBuffer sb = new StringBuffer();
		while (matcher.find()) {
			if (matcher.group().equals("$declarebuffers")) {
				// to avoid 'unused' warnings
				matcher.appendReplacement(sb, VariableDefinition.generateDeclareBuffers(allVars));
			} else if (matcher.group().equals("$setinputvariables")) {
				matcher.appendReplacement(sb, generateSetInputVariables(fmuConfig.inputVariables));
			} else if (matcher.group().equals("$getoutputvariables")) {
				matcher.appendReplacement(sb, generateGetOutputVariables(fmuConfig.outputVariables));
			}
		}

		matcher.appendTail(sb);
		Files.createDirectories(projectLoc.resolve("fmu"));
		Files.write(projectLoc.resolve("fmu/fmudebug.cpp"), sb.toString().getBytes());
	}

	private String generateSetInputVariables(List<VariableDefinition> inputVariables) {
		StringBuilder sb = new StringBuilder();
		for (VariableDefinition var : inputVariables) {
			String typeName = var.type.getName();
			String buffer = "temp" + typeName;

			sb.append("iss >> ").append(buffer).append("[0];\n");
			sb.append("fmi2Set").append(typeName).append("(comp, vars, 1, ").append(buffer).append(");\n");
			sb.append("++vars[0];\n");
			sb.append("std::cout << ").append(buffer).append("[0] << \" \";\n\n");
		}

		return sb.toString();
	}

	private String generateGetOutputVariables(List<VariableDefinition> outputVariables) {
		StringBuilder sb = new StringBuilder();
		for (VariableDefinition var : outputVariables) {
			String typeName = var.type.getName();
			String buffer = "temp" + typeName;

			sb.append("fmi2Get").append(typeName).append("(comp, vars, 1, ").append(buffer).append(");\n");
			sb.append("++vars[0];\n");
			sb.append("std::cout << ").append(buffer).append("[0] << \" \";\n\n");
		}

		return sb.toString();
	}

}
