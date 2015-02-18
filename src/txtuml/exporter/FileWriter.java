package txtuml.exporter;

import java.io.PrintWriter;

public class FileWriter {
	private static PrintWriter pw;

	public static void setFile(String javaFileDir, String packageName,
			String className) throws ConvertException {
		try {
			pw = new PrintWriter(javaFileDir + className + ".java");
		} catch (Exception e) {
			throw new ConvertException("Cannot create file.");
		}
	}

	public static void write(String text) {
		pw.write(text);
	}

	public static void endFile() {
		pw.close();
	}
}
