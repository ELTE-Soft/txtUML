package hu.elte.txtuml.export.uml2.tests.models.attribute;

import hu.elte.txtuml.api.model.DataType;
import hu.elte.txtuml.api.model.ModelClass;

public class TestClass extends ModelClass {
	
	byte byte_prim;
	Byte byte_boxed;
	
	short short_prim;
	Short short_boxed;
	
	int int_prim;
	Integer int_boxed;
	
	long long_prim;
	Long long_boxed;
	
	boolean bool_prim;
	Boolean bool_boxed;
	
	String string;
	
	Reals dt;
}

class Reals extends DataType {

	double double_prim;
	Double double_boxed;
	
	float float_prim;
	Float float_boxed;
}