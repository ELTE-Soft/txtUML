package txtuml.exporter;

import java.util.ArrayList;
import java.util.Collections;

import org.eclipse.uml2.uml.Model;

public class JavaContent {
	private String orderFileName;
	private Model model;
	private ArrayList<String> multipleNames;

	public JavaContent(String orderFileName, Model model) {
		this.orderFileName = orderFileName;
		this.model = model;

		checkForSameNames();
	}

	private void checkForSameNames() {
		multipleNames = new ArrayList<>();
		ArrayList<String> allList = new CodeRecognizer()
				.getAllOwnedElements(model);

		Collections.sort(allList);
		for (int i = 0; i < allList.size() - 1; ++i) {
			if (allList.get(i).equals(allList.get(i + 1))) {
				multipleNames.add(allList.get(i));
			}
		}
	}

	public String parse() throws ConvertException {
		if ("".equals(orderFileName)) {
			return new JavaContentCustom(model, multipleNames).parse();
		}

		return new JavaContentOrdered(model, multipleNames)
				.parse(orderFileName);
	}
}
