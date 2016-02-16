package hu.elte.txtuml.examples.validation;

import hu.elte.txtuml.api.model.DataType;
import hu.elte.txtuml.examples.validation.helpers.A;
import hu.elte.txtuml.examples.validation.helpers.MyDataType;

public class DataTypeInvalidMethod extends DataType {

	final int a;
	
	public DataTypeInvalidMethod() {
		a = 43;
	}
	
	public void f() {
		
	}

}
