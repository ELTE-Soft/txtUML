package hu.elte.txtuml.layout.export.interfaces;

import hu.elte.txtuml.layout.export.impl.StatementListImpl;

import java.util.List;

import Annotations.Statement;
import Annotations.StatementType;

public interface StatementList extends List<Statement> {
	static StatementList create() {
		return new StatementListImpl();
	}
	
	void addNew(StatementType type, String... params);

}
