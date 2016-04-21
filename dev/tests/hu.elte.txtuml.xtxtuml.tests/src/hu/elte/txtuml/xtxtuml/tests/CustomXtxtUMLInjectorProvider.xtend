package hu.elte.txtuml.xtxtuml.tests

import com.google.inject.Guice
import com.google.inject.Injector
import hu.elte.txtuml.xtxtuml.XtxtUMLInjectorProvider
import hu.elte.txtuml.xtxtuml.XtxtUMLRuntimeModule
import hu.elte.txtuml.xtxtuml.XtxtUMLStandaloneSetup

class CustomXtxtUMLInjectorProvider extends XtxtUMLInjectorProvider {

	override Injector internalCreateInjector() {
		return new XtxtUMLStandaloneSetup() {

			override Injector createInjector() {
				return Guice.createInjector(new XtxtUMLRuntimeModule());
			}
		}.createInjectorAndDoEMFRegistration();
	}
}
