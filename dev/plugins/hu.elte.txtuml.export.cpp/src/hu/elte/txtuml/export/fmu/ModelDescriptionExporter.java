package hu.elte.txtuml.export.fmu;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.SimpleTimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

import hu.elte.txtuml.export.cpp.CppExporterUtils;
import hu.elte.txtuml.export.cpp.Uml2ToCppExporter;

public class ModelDescriptionExporter {

	public void export(Path projectLoc, FMUConfig fmuConfig) throws IOException, URISyntaxException {
		Bundle bundle = Platform.getBundle(Uml2ToCppExporter.PROJECT_NAME);
		URL fileURL = bundle.getEntry("fmuResources" + IPath.SEPARATOR + "modelDescriptionTemplate.xml");
		String template = new String(Files.readAllBytes(Paths.get(FileLocator.toFileURL(fileURL).toURI())));
		StringBuffer sb = new StringBuffer();
		Matcher matcher = Pattern.compile("\\$[a-zA-Z]+").matcher(template);
		while (matcher.find()) {
			if (matcher.group().equals("$guid")) {
				matcher.appendReplacement(sb, fmuConfig.guid);
			} else if (matcher.group().equals("$generateDateAndTime")) {
				Date date = new Date();
				SimpleDateFormat dateFormatter = new SimpleDateFormat("YYYY-MM-DD'T'hh:mm:ss'Z'");
				dateFormatter.setTimeZone(new SimpleTimeZone(SimpleTimeZone.UTC_TIME, "UTC"));
				String formattedDate = dateFormatter.format(date);
				matcher.appendReplacement(sb, formattedDate);
			} else if (matcher.group().equals("$modelName")) {
				matcher.appendReplacement(sb, fmuConfig.umlClassName);
			} else if (matcher.group().equals("$modelId")) {
				matcher.appendReplacement(sb, CppExporterUtils.qualifiedNameToSimpleName(fmuConfig.umlClassName));
			} else if (matcher.group().equals("$variables")) {
				matcher.appendReplacement(sb, generateVariables(fmuConfig.inputVariables, fmuConfig.outputVariables,
						fmuConfig.initialValues));
			} else if (matcher.group().equals("$outputs")) {
				matcher.appendReplacement(sb, generateOutputs(fmuConfig.inputVariables, fmuConfig.outputVariables));
			}
		}
		matcher.appendTail(sb);
		Files.write(projectLoc.resolve("modelDescription.xml"), sb.toString().getBytes());
	}

	private String generateVariables(List<VariableDefinition> inputVariables, List<VariableDefinition> outputVariables,
			Map<String, Object> initialValues) {

		StringBuilder sb = new StringBuilder();
		for (VariableDefinition variableDefinition : inputVariables) {
			sb.append(generateVariable(variableDefinition, initialValues, false));
		}
		for (VariableDefinition variableDefinition : outputVariables) {
			sb.append(generateVariable(variableDefinition, initialValues, true));
		}
		return sb.toString();

	}

	private int variableIndex = 0;

	private String generateVariable(VariableDefinition variableDefinition, Map<String, Object> initVal,
			boolean output) {
		StringBuilder sb = new StringBuilder();
		sb.append("<ScalarVariable name=\"");
		sb.append(variableDefinition.name);
		sb.append("\"  valueReference=\"");
		sb.append(variableIndex++);
		sb.append("\" variability=\"discrete\" causality=");
		sb.append(output ? "\"output\"" : "\"input\"");
		if (output) {
			sb.append(" initial=\"approx\"");
		}
		sb.append("><");
		sb.append(variableDefinition.type.getName() + " ");
		if (initVal.containsKey(variableDefinition.name)) {
			sb.append("start=\"" + initVal.get(variableDefinition.name) + "\"");
		}
		sb.append(" />");
		sb.append("</ScalarVariable>\n");
		return sb.toString();
	}

	private String generateOutputs(List<VariableDefinition> inputVariables, List<VariableDefinition> outputVariables) {
		int outputVarInd = inputVariables.size() + 1;
		StringBuilder sb = new StringBuilder();
		for (VariableDefinition variableDefinition : outputVariables) {
			sb.append(generateOutput(outputVarInd++, variableDefinition, inputVariables.size()));
		}
		return sb.toString();
	}

	private String generateOutput(int index, VariableDefinition variableDefinition, int numInputs) {
		StringBuilder sb = new StringBuilder();
		sb.append("<Unknown index=\"");
		sb.append(index);
		sb.append("\"  dependencies=\"");
		if (numInputs > 0) {
			sb.append(1);
		}
		for (int i = 1; i < numInputs; i++) {
			sb.append(" " + (i + 1));
		}
		sb.append("\" />\n");
		return sb.toString();
	}

}
