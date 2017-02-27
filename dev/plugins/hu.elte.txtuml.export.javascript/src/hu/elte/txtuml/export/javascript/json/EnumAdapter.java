package hu.elte.txtuml.export.javascript.json;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.eclipse.uml2.uml.VisibilityKind;

/***
 * 
 * Adapter for serializing an enum using it's toString() method
 *
 */
public class EnumAdapter extends XmlAdapter<String, Enum<?>> {

	public String marshal(Enum<?> enm) {
		return enm.toString();
	}

	public VisibilityKind unmarshal(String val) {
		throw new UnsupportedOperationException("Can not unmarshal enum value");
	}
}
