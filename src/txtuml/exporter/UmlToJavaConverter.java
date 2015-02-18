package txtuml.exporter;

import org.eclipse.uml2.uml.Model;

public class UmlToJavaConverter {
	private static UmlToJavaConverter instance = null;

	public static UmlToJavaConverter getInstance() {
		if (instance == null) {
			synchronized (UmlToJavaConverter.class) {
				if (instance == null) {
					instance = new UmlToJavaConverter();
				}
			}
		}

		return instance;
	}

	private UmlToJavaConverter() {
	}

	public void convert(String javaFileDir, String packageName, String umlFileName,
			String xmlFileName) throws ConvertException {
		Model model;

		try {
			model = new ModelLoader(umlFileName).load();
		} catch (Exception e) {
			throw new ConvertException("Model not found!");
		}

		JavaGenerator generater = new JavaGenerator(javaFileDir, xmlFileName, packageName,
				model);
		generater.generate();
	}

	public static void main(String[] args) throws ConvertException {
		String javaFileDir = "";
		String packageName = "";
		String umlFileName = "";
		String xmlFileName = "";

		if (args.length == 3) {

			javaFileDir = args[0];
			packageName = args[1];
			umlFileName = args[2];

		} else if (args.length == 4) {

			javaFileDir = args[0];
			packageName = args[1];
			umlFileName = args[2];
			xmlFileName = args[3];

		} else {
			throw new ConvertException(
					"It must be 2 or 3 command line parameters!");
		}

		UmlToJavaConverter converter = UmlToJavaConverter.getInstance();

		try {
			converter.convert(javaFileDir, packageName, umlFileName, xmlFileName);
		} catch (ConvertException e) {
			System.out.println(e.getMessage());
		}
	}
}