package hu.elte.txtuml.xtxtuml.naming;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.naming.QualifiedName;

public interface IPackageNameCalculator {

	public QualifiedName getExpectedPackageName(EObject astNode);
}
