package hu.elte.txtuml.export.papyrus.utils;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.uml2.uml.Element;

/**
 * Supplies functionalities for finding EditParts
 * @author András Dobreff
 */
public class EditPartFinder {
	/**
	 * Gets the right {@link EditPart} from a specified collection which handles the given {@link Element}
	 * @param editParts - Collection of EditParts
	 * @param element - The Element that is searched for
	 * @return The EditPart or null if not found
	 */
	public static EditPart getEditPartOfModelElement(Collection<? extends EditPart> editParts, Element element) {
		if(element == null) return null;
		for (EditPart ep : editParts) {
			Element actual = (Element) ((View) ep.getModel()).getElement();
			EObjectComparator comperator = new EObjectComparator();
			int c = comperator.compare(element, actual);
			if(c == 0)
				return ep; 
		}
		return null;
	}
	

	/**
	 * Gets the right {@link EditPart} that handles the given {@link Element} from a root EditPart.
	 * It compares the Element of the root and all of its children. Returns the found EditPart or null
	 * @param root - The root of the EditParts that can hold the Element  
	 * @param element - The Element that is searched for
	 * @return The EditPart  or null
	 */
	public static EditPart getEditPartofElement(EditPart root, Element element){
		List<EditPart> list = Arrays.asList(root);
		return recursive(list, element);
	}
	
	private static EditPart recursive(List<EditPart> list, Element element){
		EditPart found = getEditPartOfModelElement(list, element); 
		if(found != null){
			return found;
		}else{
			int i = 0;
			while(found == null && i < list.size()){
				@SuppressWarnings("unchecked")
				List<EditPart> children = list.get(i).getChildren();
				found = recursive(children, element);
				i++;
			}
			return found;
		}
	}
	
}
