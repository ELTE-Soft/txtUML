package hu.elte.txtuml.export.javascript.json.model.cd;

import org.eclipse.persistence.oxm.annotations.XmlAccessMethods;
import org.eclipse.uml2.uml.Property;

public class AssociationEnd {
	@XmlAccessMethods(getMethodName = "getName")
	private String name;
	@XmlAccessMethods(getMethodName = "getMultiplicity")
	private String multiplicity;
	@XmlAccessMethods(getMethodName = "getVisibility")
	private String visibility;
	@XmlAccessMethods(getMethodName = "isNavigable")
	private Boolean navigable;
	@XmlAccessMethods(getMethodName = "isComposition")
	private Boolean composition;

	protected AssociationEnd() {
	};

	public AssociationEnd(String endID, Property end) {
		name = end.getLabel();
		int low = end.lowerBound();
		int high = end.upperBound();

		if (low == high) {
			multiplicity = Integer.toString(low);
		} else {
			multiplicity = low + ".." + (high == -1 ? "*" : high);
		}
		visibility = end.getVisibility().getLiteral();
		navigable = end.isNavigable();
		composition = end.isComposite();
		/*
		 * AggregationKind akind = end.getAggregation(); if (akind.getValue() ==
		 * AggregationKind.NONE){ if (end.isNavigable()){ type = "direction"; }
		 * }else{ type = akind.getLiteral(); }
		 */
	}

	public String getName() {
		return name;
	}

	public String getMultiplicity() {
		return multiplicity;
	}

	public String getVisibility() {
		return visibility;
	}

	public Boolean isNavigable() {
		return navigable;
	}

	public Boolean isComposition() {
		return composition;
	}

}
