package hu.elte.txtuml.xtxtuml.scoping

import org.eclipse.xtext.scoping.impl.MultimapBasedSelectable;
import org.eclipse.xtext.resource.IEObjectDescription
import org.eclipse.xtext.naming.QualifiedName
import org.eclipse.emf.ecore.EClass
import java.util.ArrayList

class XtxtUMLMultimapBasedSelectable extends MultimapBasedSelectable {
	
	new(Iterable<IEObjectDescription> allDescriptions) {
		super(allDescriptions);
	}
	
	override getExportedObjects(EClass type, QualifiedName name, boolean ignoreCase) {
		var result = super.getExportedObjects(type, name, ignoreCase);
		
		if (type.name == "JvmType") {
			var currentName = name;
			while (currentName.segmentCount > 1) {
				val currentSegments = currentName.segments;
				
				val newSegments = new ArrayList<String>();
				newSegments.addAll(currentSegments.take(currentName.segmentCount - 2));
				newSegments.add(currentSegments.drop(currentName.segmentCount - 2).join("$"));
				currentName = QualifiedName::create(newSegments);
				
				val newResult = new ArrayList<IEObjectDescription>();
				newResult.addAll(result);
				newResult.addAll(super.getExportedObjects(type, currentName, ignoreCase));
				
				result = newResult;
			}
		}
		
		return result;
	}
	
}