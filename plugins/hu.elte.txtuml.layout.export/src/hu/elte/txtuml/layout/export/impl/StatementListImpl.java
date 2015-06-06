package hu.elte.txtuml.layout.export.impl;

import java.util.LinkedList;

import hu.elte.txtuml.layout.export.interfaces.StatementList;
import hu.elte.txtuml.layout.visualizer.annotations.Statement;
import hu.elte.txtuml.layout.visualizer.annotations.StatementType;

/**
 * Default implementation for {@link StatementList}.
 * 
 * @author Gábor Ferenc Kovács
 *
 */
@SuppressWarnings("serial")
public class StatementListImpl extends LinkedList<Statement> implements StatementList {

	@Override
	public void addNew(StatementType type, String... params) {
		add(new Statement(type, params));
	}

}
