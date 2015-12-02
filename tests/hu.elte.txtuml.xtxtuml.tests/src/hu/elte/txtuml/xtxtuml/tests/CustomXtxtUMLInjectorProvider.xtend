package hu.elte.txtuml.xtxtuml.tests

import com.google.inject.Guice
import com.google.inject.Injector
import com.google.inject.Provider
import hu.elte.txtuml.xtxtuml.XtxtUMLInjectorProvider
import hu.elte.txtuml.xtxtuml.XtxtUMLRuntimeModule
import hu.elte.txtuml.xtxtuml.XtxtUMLStandaloneSetup
import hu.elte.txtuml.xtxtuml.naming.IPackageNameCalculator
import org.eclipse.emf.ecore.EObject
import org.eclipse.xtext.naming.QualifiedName
import org.eclipse.xtext.service.AbstractGenericModule
import org.eclipse.xtext.util.Modules2

import static hu.elte.txtuml.xtxtuml.tests.PackageNameCalculatorStubProvider.*

class CustomXtxtUMLInjectorProvider extends XtxtUMLInjectorProvider {

	override Injector internalCreateInjector() {
		return new XtxtUMLStandaloneSetup() {

			override Injector createInjector() {
				return Guice.createInjector(Modules2.mixin(new XtxtUMLRuntimeModule(), new AbstractGenericModule() {

					def Provider<IPackageNameCalculator> provideIPackageNameCalculator() {
						PackageNameCalculatorStubProvider.INSTANCE
					}
				}));
			}
		}.createInjectorAndDoEMFRegistration();
	}
}

class PackageNameCalculatorStubProvider implements Provider<IPackageNameCalculator> {

	public static val INSTANCE = new PackageNameCalculatorStubProvider

	private static var QualifiedName packageName

	static def setPackageName(String ... packageNameParts) {
		packageName = QualifiedName.create(packageNameParts)
	}

	override get() {
		new IPackageNameCalculator() {
			override getExpectedPackageName(EObject astNode) {
				packageName
			}
		}
	}
}
