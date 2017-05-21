package hu.elte.txtuml.export.cpp.structural;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.eclipse.uml2.uml.Port;

import hu.elte.txtuml.export.cpp.CppExporterUtils;
import hu.elte.txtuml.export.cpp.templates.GenerationNames.TypeDelcreationKeywords;
import hu.elte.txtuml.export.cpp.templates.structual.PortTemplates;

public class PortExporter {

	public void createPortSource(String outputDirectory) throws FileNotFoundException, UnsupportedEncodingException {
		CppExporterUtils.writeOutSource(outputDirectory, (PortTemplates.PORT_HEADER),
				CppExporterUtils.format(PortTemplates.portHeaderGuard("")));
	}

	public String createPortEnumCode(List<Port> ports) {
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
}
