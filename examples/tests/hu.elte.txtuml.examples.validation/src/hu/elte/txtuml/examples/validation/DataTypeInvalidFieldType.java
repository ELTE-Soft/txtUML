package hu.elte.txtuml.examples.validation;

import hu.elte.txtuml.api.model.DataType;
import hu.elte.txtuml.examples.validation.helpers.A;
import hu.elte.txtuml.examples.validation.helpers.MyDataType;

public class DataTypeInvalidFieldType extends DataType {

	final int a = 0;
	
	final String b = "";
	
	final MyDataType c = new MyDataType();
	
	final Object x = null;
	
	final A y = null;

}
