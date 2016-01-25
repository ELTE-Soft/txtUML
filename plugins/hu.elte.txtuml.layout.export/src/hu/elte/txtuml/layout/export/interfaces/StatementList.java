package hu.elte.txtuml.layout.export.interfaces;

import java.util.List;

import hu.elte.txtuml.layout.export.impl.StatementListImpl;
import hu.elte.txtuml.layout.visualizer.statements.Statement;
import hu.elte.txtuml.layout.visualizer.statements.StatementType;

/**
 * 
 * @author Gabor Ferenc Kovacs
 *
 */
public interface StatementList extends List<Statement> {

	static StatementList create() {
		return new StatementListImpl();
	}
	
	void addNew(StatementType type, String... params);

}
