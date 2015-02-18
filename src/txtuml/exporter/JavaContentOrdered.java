package txtuml.exporter;

import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.NamedElement;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class JavaContentOrdered extends JavaContentCustom {
	public JavaContentOrdered(Model model, ArrayList<String> multipleNames) {
		super(model, multipleNames);
	}

	public String parse(String orderFileName) throws ConvertException {
		return getContent(model, orderFileName);
	}

	private String getContent(Model model, String orderFileName)
			throws ConvertException {
		NodeList nodeList = getXmlContent(orderFileName);

		return getSubContent(model, nodeList);
	}

	private NodeList getXmlContent(String orderFileName)
			throws ConvertException {
		Document doc = null;

		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(new File(orderFileName));
		} catch (Exception e) {
			throw new ConvertException(".xml file not found!");
		}

		return doc.getDocumentElement().getChildNodes();
	}

	public String getSubContent(Element element, NodeList nodeList) {
		String content = "";
		++tabs;

		CodeRecognizer cr = new CodeRecognizer();
		ArrayList<Element> ownedElements = cr.getOwnedElements(element);

		for (int i = 0; i < nodeList.getLength(); ++i) {
			content = getContentFromNodeList(content, nodeList, i,
					ownedElements);
		}

		content += getRemainingElementsFromModel(ownedElements);

		--tabs;
		return content;
	}

	private String getContentFromNodeList(String content, NodeList nodeList,
			int i, ArrayList<Element> ownedElements) {
		org.w3c.dom.Element eElement = null;
		int ownedElementIndex = -1;

		try {
			ownedElementIndex = locateElement(nodeList, i, eElement,
					ownedElements);

			content += getElementsFromModel(ownedElements, ownedElementIndex,
					nodeList, i);
		} catch (Exception e) {
			// current element is not located in uml file. No problem.
		}

		return content;
	}

	private int locateElement(NodeList list, int i,
			org.w3c.dom.Element eElement, ArrayList<Element> ownedElements)
			throws Exception {
		eElement = (org.w3c.dom.Element) list.item(i);

		for (int j = 0; j < ownedElements.size(); ++j) {
			if (StringCalculator.isMatch(eElement,
					((NamedElement) ownedElements.get(j)))) {
				return j;
			}
		}

		throw new Exception();
	}

	private String getElementsFromModel(ArrayList<Element> ownedElements,
			int ownedElementIndex, NodeList list, int i) {
		String result = "";

		if (list.item(i).getNodeType() == Node.ELEMENT_NODE) {
			result += new CodeRecognizer().generate(
					ownedElements.get(ownedElementIndex), tabs, this) + '\n';
		}
		if (list.item(i).hasChildNodes()) {
			result += getSubContent(ownedElements.get(ownedElementIndex), list
					.item(i).getChildNodes());
		}

		if (!"field".equals(list.item(i).getNodeName())) {
			for (int j = 0; j < 4 * tabs; ++j) {
				result += " ";
			}
			result += "}\n\n";
		}

		ownedElements.remove(ownedElementIndex);

		return result;
	}

	private String getRemainingElementsFromModel(
			ArrayList<Element> ownedElements) {
		String result = "";

		for (Element e : ownedElements) {
			result += new CodeRecognizer().generate(e, tabs, this) + '\n';
			result += getContent(e);

			if (!LiteralTypes.Property.equals(e.eClass())) {
				for (int j = 0; j < 4 * tabs; ++j) {
					result += " ";
				}
				result += "}\n\n";
			}
		}

		return result;
	}
}
