package hu.elte.txtuml.xtxtuml.common

import hu.elte.txtuml.xtxtuml.xtxtUML.TUClass
import hu.elte.txtuml.xtxtuml.xtxtUML.TUDataType
import hu.elte.txtuml.xtxtuml.xtxtUML.TUSignal
import hu.elte.txtuml.xtxtuml.xtxtUML.TUClassOrDataTypeOrSignal
import static hu.elte.txtuml.xtxtuml.xtxtUML.XtxtUMLPackage.Literals.*
import static hu.elte.txtuml.xtxtuml.validation.XtxtUMLIssueCodes.*

class XtxtUMLClassDataTypeSignalHelper {
	
	def dispatch name(TUClass it) {
		name;
	}
	
	def dispatch name(TUDataType it) {
		name;
	}
	
	def dispatch name(TUSignal it) {
		name;
	}
	
	def dispatch TUClassOrDataTypeOrSignal superType(TUSignal it){
		superSignal
	}
	
	def dispatch TUClassOrDataTypeOrSignal superType(TUDataType it){
		superDataType
	}
	
	def dispatch TUClassOrDataTypeOrSignal superType(TUClass it){
		superClass
	}
	
	def dispatch members(TUClass it) {
		members;
	}
	
	def dispatch members(TUDataType it) {
		members;
	}
	
	def dispatch members(TUSignal it) {
		null;
	}
	
	def dispatch typeString(TUClass it) {
		"class";
	}
	
	def dispatch typeString(TUDataType it) {
		"datatype";
	}
	
	def dispatch typeString(TUSignal it) {
		"signal";
	}
	
	def dispatch errorFeature(TUSignal it){
		TU_SIGNAL__SUPER_SIGNAL
	}
	
	def dispatch errorFeature(TUClass it){
		TU_CLASS__SUPER_CLASS
	}
	
	def dispatch errorFeature(TUDataType it){
		TU_DATA_TYPE__SUPER_DATA_TYPE
	}
	
	def dispatch errorCode(TUSignal it){
		SIGNAL_HIERARCHY_CYCLE
	}
	
	def dispatch errorCode(TUClass it){
		CLASS_HIERARCHY_CYCLE
	}
	
	def dispatch errorCode(TUDataType it){
		DATATYPE_HIERARCHY_CYCLE
	}
}

