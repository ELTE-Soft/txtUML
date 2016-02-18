package hu.elte.txtuml.xtxtuml;

import org.eclipse.xtext.builder.smap.DerivedResourceMarkerCopier;
import org.eclipse.xtext.service.AbstractGenericModule;

import hu.elte.txtuml.xtxtuml.validation.XtxtUMLDerivedResourceMarkerCopier;

public class BuilderCustomizationModule extends AbstractGenericModule {

	public Class<? extends DerivedResourceMarkerCopier> bindDerivedResourceMarkerCopier() {
		return XtxtUMLDerivedResourceMarkerCopier.class;
	}
}
