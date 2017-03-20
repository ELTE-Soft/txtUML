package hu.elte.txtuml.xtxtuml.ui;

import org.eclipse.xtext.builder.smap.DerivedResourceMarkerCopier;
import org.eclipse.xtext.service.AbstractGenericModule;

import hu.elte.txtuml.xtxtuml.ui.validation.XtxtUMLDerivedResourceMarkerCopier;

public class BuilderCustomizationModule extends AbstractGenericModule {

	public Class<? extends DerivedResourceMarkerCopier> bindDerivedResourceMarkerCopier() {
		return XtxtUMLDerivedResourceMarkerCopier.class;
	}

}
