package hu.elte.txtuml.xtxtuml.common

import hu.elte.txtuml.xtxtuml.xtxtUML.TUClass
import hu.elte.txtuml.xtxtuml.xtxtUML.TUDataType
import hu.elte.txtuml.xtxtuml.xtxtUML.TUSignal

class XtxtUMLClassDataTypeSignalHelper {
	
	def dispatch name(TUClass it) {
		it.name;
	}
	
	def dispatch name(TUDataType it) {
		it.name;
	}
	
	def dispatch name(TUSignal it) {
		it.name;
	}
}

