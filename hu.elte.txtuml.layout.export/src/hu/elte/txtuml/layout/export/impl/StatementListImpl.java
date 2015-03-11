package hu.elte.txtuml.layout.export.impl;

import java.util.LinkedList;

import Annotations.Statement;
import Annotations.StatementType;
import hu.elte.txtuml.layout.export.interfaces.StatementList;

@SuppressWarnings("serial")
public class StatementListImpl extends LinkedList<Statement> implements StatementList {

	@Override
	public void addNew(StatementType type, String... params) {
		add(new Statement(type, params));
	}

}
