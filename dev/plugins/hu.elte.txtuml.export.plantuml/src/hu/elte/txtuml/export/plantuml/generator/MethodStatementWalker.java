package hu.elte.txtuml.export.plantuml.generator;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.*;

public class MethodStatementWalker extends ASTVisitor {

	protected List<MethodDeclaration> fragments;

	public MethodStatementWalker() {
		super();
		fragments = new ArrayList<MethodDeclaration>();
	}

	@Override
	public boolean preVisit2(ASTNode node) {

		if (node.getNodeType() == ASTNode.METHOD_DECLARATION) {
			MethodDeclaration decl = (MethodDeclaration) node;
			if (!decl.getName().toString().equals("run") && !decl.getName().toString().equals("initialize")) {
				fragments.add(decl);
			}
		}

		return false;
	};
}
