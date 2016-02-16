package hu.elte.txtuml.layout.export.impl;

import java.util.ArrayList;

import hu.elte.txtuml.layout.export.interfaces.StatementList;
import hu.elte.txtuml.layout.visualizer.statements.Statement;
import hu.elte.txtuml.layout.visualizer.statements.StatementType;

/**
 * Default implementation for {@link StatementList}.
 */
@SuppressWarnings("serial")
public class StatementListImpl extends ArrayList<Statement> implements StatementList {

	@Override
	public void addNew(StatementType type, String... params) {
		add(new Statement(type, params));
	}

}
