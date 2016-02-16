package hu.elte.txtuml.importing.uml2;

import org.eclipse.uml2.uml.Model;

public class JavaGenerator {
	private final String javaFileDir;
	private final String xmlFileName;
	private final String packageName;
	private final Model model;

	public JavaGenerator(String javaFileDir, String xmlFileName, String packageName, Model model) {
		this.javaFileDir = javaFileDir;
		this.xmlFileName = xmlFileName;
		this.packageName = packageName;
		this.model = model;
	}

	void generate() throws ConvertException {
		FileWriter.setFile(javaFileDir, packageName, getClassName());

		writePackage();
		writeImports();

		writeClass();

		FileWriter.endFile();
	}

	private void writePackage() {
		FileWriter.write("package " + packageName + ";\n\n");
	}

	// TODO: refactor: external interfaces...
	private void writeImports() {
		FileWriter.write("import txtuml.api.*;\n\n");
	}

	private void writeClass() throws ConvertException {
		FileWriter.write("public class " + getClassName() + " {\n");
		FileWriter.write(new JavaContent(xmlFileName, model).parse());
		FileWriter.write("}");
	}

	private String getClassName() {
		return model.getName();
	}
}
