package hu.elte.txtuml.examples.validation.model;

import hu.elte.txtuml.api.model.DataType;
import hu.elte.txtuml.examples.validation.model.helpers.A;
import hu.elte.txtuml.examples.validation.model.helpers.MyDataType;

public class DataTypeInvalidFieldType extends DataType {

	final int a = 0;

	final String b = "";

	final MyDataType c = new MyDataType();

	final Object x = null;

	final A y = null;

}
