package hu.elte.txtuml.layout.export.interfaces;

import hu.elte.txtuml.layout.export.impl.StatementListImpl;
import hu.elte.txtuml.layout.visualizer.annotations.Statement;
import hu.elte.txtuml.layout.visualizer.annotations.StatementType;

import java.util.List;


public interface StatementList extends List<Statement> {
	static StatementList create() {
		return new StatementListImpl();
	}
	
	void addNew(StatementType type, String... params);

}
