package hu.elte.txtuml.layout.export;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import hu.elte.txtuml.layout.export.utils.LayoutUtils;
import hu.elte.txtuml.utils.Pair;

/**
 * This class collects {@link DiagramExportationReport}s which belong to the given project and model.
 * This collection then can be queried as a {@link TxtUMLLayoutDescriptor}.
 */
public class TxtUMLExporter {

	private String projectName;
	private String txtUMLModelName;
	private Map<String, String> txtUMLLayout; // <layout name, project name>

	/**
	 * The Constructor
	 * 
	 * @param projectName
	 *            - The txtUMLProject
	 * @param txtUMLModelName
	 *            - The fully qualified name of the txtUML model
	 * @param txtUMLLayout
	 *            - The fully qualified names of the txtUML Diagrams and the
	 *            project names
	 * @param parent
	 *            - the parent ClassLoader
	 */
	public TxtUMLExporter(String projectName, String txtUMLModelName,
			Map<String, String> txtUMLLayout) {

		this.projectName = projectName;
		this.txtUMLModelName = txtUMLModelName;
		this.txtUMLLayout = txtUMLLayout;
	}

	/**
	 * Creates a description structure from a txtUML diagram definition
	 * 
	 * @return
	 * @throws Exception
	 */
	public TxtUMLLayoutDescriptor exportTxtUMLLayout() throws Exception {
		List<Pair<String, DiagramExportationReport>> reports = new ArrayList<>();

		for (Map.Entry<String, String> layout : txtUMLLayout.entrySet()) {
			try {
				DiagramExportationReport report = LayoutUtils.exportTxtUMLLayout(projectName, layout.getKey(),
						layout.getValue());
				if (!report.isSuccessful()) {
					StringBuilder errorMessages = new StringBuilder(
							"Errors occured during layout exportation:" + System.lineSeparator());
					for (Object error : report.getErrors()) {
						errorMessages.append("- " + error + System.lineSeparator());
					}
					errorMessages.append("The exportation was't successfull.");
					throw new LayoutExportException(errorMessages.toString());
				}

				reports.add(new Pair<>(layout.getKey(), report));
			} catch (Exception e) {
				throw e;
			}
		}
		return new TxtUMLLayoutDescriptor(txtUMLModelName, reports);
	}
}