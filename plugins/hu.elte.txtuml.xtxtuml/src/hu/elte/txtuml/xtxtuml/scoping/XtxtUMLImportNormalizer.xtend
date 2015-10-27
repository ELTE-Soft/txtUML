package hu.elte.txtuml.xtxtuml.scoping

import org.eclipse.xtext.xbase.scoping.NestedTypeAwareImportNormalizerWithDotSeparator
import org.eclipse.xtext.naming.QualifiedName
import java.util.ArrayList

class XtxtUMLImportNormalizer extends NestedTypeAwareImportNormalizerWithDotSeparator {

	new(QualifiedName importedNamespace, boolean wildcard, boolean ignoreCase) {
		super(importedNamespace, wildcard, ignoreCase);
	}
	
	override protected resolveWildcard(QualifiedName relativeName) {
		getImportedNamespacePrefix().append(relativeName);	
	}
	
	override deresolve(QualifiedName fullyQualifiedName) {
		var oldSegments = fullyQualifiedName.segments;
		var newSegments = new ArrayList<String>();
		
		for (segment : getImportedNamespacePrefix().segments) {
			if (segment != oldSegments.head) {
				val oldSegmentsSplittedHead = oldSegments.head.split("\\$");
				
				val tempOldSegments = new ArrayList<String>();
				tempOldSegments.add(oldSegmentsSplittedHead.head);
				tempOldSegments.add(oldSegmentsSplittedHead.drop(1).join("$"));
				tempOldSegments.addAll(oldSegments.drop(1));
				
				oldSegments = tempOldSegments;
			}
			
			while (segment != oldSegments.head) {
				val oldSegmentsSplittedSecond = oldSegments.get(1).split("\\$");
				
				val tempOldSegments = new ArrayList<String>();
				tempOldSegments.add(oldSegments.head + "$" + oldSegmentsSplittedSecond.head);
				tempOldSegments.add(oldSegmentsSplittedSecond.drop(1).join("$"));
				tempOldSegments.addAll(oldSegments.drop(2));
				
				oldSegments = tempOldSegments;
			}
			
			newSegments.add(segment);
			oldSegments = oldSegments.drop(1).toList();
		}

		newSegments.addAll(oldSegments);
		super.deresolve(QualifiedName::create(newSegments));
	}
	
}