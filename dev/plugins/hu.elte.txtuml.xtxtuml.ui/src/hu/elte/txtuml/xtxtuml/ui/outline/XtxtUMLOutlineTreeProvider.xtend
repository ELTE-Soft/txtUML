package hu.elte.txtuml.xtxtuml.ui.outline;

import hu.elte.txtuml.xtxtuml.xtxtUML.TUAssociationEnd
import hu.elte.txtuml.xtxtuml.xtxtUML.TUAttribute
import hu.elte.txtuml.xtxtuml.xtxtUML.TUConstructor
import hu.elte.txtuml.xtxtuml.xtxtUML.TUEntryOrExitActivity
import hu.elte.txtuml.xtxtuml.xtxtUML.TUEnumerationLiteral
import hu.elte.txtuml.xtxtuml.xtxtUML.TUExecution
import hu.elte.txtuml.xtxtuml.xtxtUML.TUFile
import hu.elte.txtuml.xtxtuml.xtxtUML.TUOperation
import hu.elte.txtuml.xtxtuml.xtxtUML.TUSignalAttribute
import hu.elte.txtuml.xtxtuml.xtxtUML.TUTransitionEffect
import hu.elte.txtuml.xtxtuml.xtxtUML.TUTransitionGuard
import org.eclipse.xtext.ui.editor.outline.impl.DefaultOutlineTreeProvider
import org.eclipse.xtext.ui.editor.outline.impl.DocumentRootNode

/**
 * Customization of the default outline structure.
 */
class XtxtUMLOutlineTreeProvider extends DefaultOutlineTreeProvider {
	
	def _isLeaf(TUFile op) {
		true
	}
	
	def _isLeaf(TUExecution exec) {
		true
	}

	def _isLeaf(TUEnumerationLiteral eLit) {
		true
	}
		
	def _isLeaf(TUSignalAttribute sAttr) {
		true
	}
	
	def _isLeaf(TUAttribute attr) {
		true
	}
	
	def _isLeaf(TUConstructor ctor) {
		true
	}
	
	def _isLeaf(TUOperation op) {
		true
	}
	
	def _isLeaf(TUEntryOrExitActivity act) {
		true
	}

	def _isLeaf(TUAssociationEnd assocEnd) {
		true
	}
	
	def _isLeaf(TUTransitionEffect effect) {
		true
	}
	
	def _isLeaf(TUTransitionGuard guard) {
		true
	}
	
	def _createChildren(DocumentRootNode rootNode, TUFile file) {
		createNode(rootNode, file);
		for (element : file.elements) {
			createNode(rootNode, element);
		}
	}
	
}
