package hu.elte.txtuml.xtxtuml.ui.labeling;

import com.google.inject.Inject
import hu.elte.txtuml.xtxtuml.xtxtUML.TUAssociation
import hu.elte.txtuml.xtxtuml.xtxtUML.TUAssociationEnd
import hu.elte.txtuml.xtxtuml.xtxtUML.TUAttribute
import hu.elte.txtuml.xtxtuml.xtxtUML.TUClass
import hu.elte.txtuml.xtxtuml.xtxtUML.TUComposition
import hu.elte.txtuml.xtxtuml.xtxtUML.TUConnector
import hu.elte.txtuml.xtxtuml.xtxtUML.TUConnectorEnd
import hu.elte.txtuml.xtxtuml.xtxtUML.TUConstructor
import hu.elte.txtuml.xtxtuml.xtxtUML.TUEntryOrExitActivity
import hu.elte.txtuml.xtxtuml.xtxtUML.TUExecution
import hu.elte.txtuml.xtxtuml.xtxtUML.TUFile
import hu.elte.txtuml.xtxtuml.xtxtUML.TUInterface
import hu.elte.txtuml.xtxtuml.xtxtUML.TUModelDeclaration
import hu.elte.txtuml.xtxtuml.xtxtUML.TUOperation
import hu.elte.txtuml.xtxtuml.xtxtUML.TUPort
import hu.elte.txtuml.xtxtuml.xtxtUML.TUPortMember
import hu.elte.txtuml.xtxtuml.xtxtUML.TUReception
import hu.elte.txtuml.xtxtuml.xtxtUML.TUSignal
import hu.elte.txtuml.xtxtuml.xtxtUML.TUSignalAttribute
import hu.elte.txtuml.xtxtuml.xtxtUML.TUState
import hu.elte.txtuml.xtxtuml.xtxtUML.TUTransition
import hu.elte.txtuml.xtxtuml.xtxtUML.TUTransitionEffect
import hu.elte.txtuml.xtxtuml.xtxtUML.TUTransitionGuard
import hu.elte.txtuml.xtxtuml.xtxtUML.TUTransitionPort
import hu.elte.txtuml.xtxtuml.xtxtUML.TUTransitionTrigger
import hu.elte.txtuml.xtxtuml.xtxtUML.TUTransitionVertex
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider
import org.eclipse.jface.viewers.StyledString
import org.eclipse.xtext.common.types.JvmGenericType
import org.eclipse.xtext.xbase.ui.labeling.XbaseLabelProvider

/**
 * Provides labels for EObjects.
 */
class XtxtUMLLabelProvider extends XbaseLabelProvider {

	@Inject
	new(AdapterFactoryLabelProvider delegate) {
		super(delegate);
	}

	/**
	 * @returns <code>null</null>
	 * (customized icon is used instead, see {@link #image(JvmGenericType)})
	 */
	override protected dispatch imageDescriptor(JvmGenericType genericType) {
		null
	}

	def image(JvmGenericType it) {
		if (interface) {
			"java_iface.png"
		} else {
			"java_class.png"
		}
	}

	def image(TUFile it) {
		"uml2/Package.gif"
	}

	def image(TUModelDeclaration it) {
		"uml2/Model.gif"
	}

	def image(TUExecution it) {
		"execution.gif"
	}

	def image(TUClass it) {
		"uml2/Class.gif"
	}

	def image(TUSignal it) {
		"uml2/Signal.gif"
	}

	def image(TUSignalAttribute it) {
		"uml2/Property.gif"
	}

	def text(TUSignalAttribute it) {
		createStyledOutlineText(name, type.simpleName)
	}

	def image(TUInterface it) {
		"uml2/Interface.gif"
	}

	def image(TUReception it) {
		"uml2/Reception.gif"
	}

	def text(TUReception it) {
		createStyledOutlineText("reception", signal.name)
	}

	def image(TUAssociation it) {
		if (it instanceof TUComposition) {
			"uml2/Association_composite.gif"
		} else {
			"uml2/Association.gif"
		}
	}

	def image(TUAssociationEnd it) {
		"uml2/Property.gif"
	}

	def text(TUAssociationEnd it) {
		createStyledOutlineText(name, multiplicityAsString + " " + endClass.name + propertiesAsString)
	}

	def image(TUConnector it) {
		if (delegation) {
			"uml2/Connector_delegation.gif"
		} else {
			"uml2/Connector_assembly.gif"
		}
	}

	def image(TUConnectorEnd it) {
		"uml2/ConnectorEnd.gif"
	}

	def text(TUConnectorEnd it) {
		createStyledOutlineText(name, role?.name + "->" + port?.name)
	}

	def image(TUAttribute it) {
		"uml2/Property.gif"
	}

	def text(TUAttribute it) {
		createStyledOutlineText(name, prefix.type.simpleName)
	}

	def image(TUConstructor it) {
		"uml2/Operation.gif"
	}

	def text(TUConstructor it) {
		val parameterList = if (parameters.empty) {
				"()"
			} else {
				parameters.join("(", ", ", ")", [parameterType.simpleName])
			}

		return new StyledString(name + parameterList)
	}

	def image(TUOperation it) {
		"uml2/Operation.gif"
	}

	def text(TUOperation it) {
		it.text(false) // `it` cannot be omitted here because of incorrect resolution
	}

	def text(TUOperation it, boolean showNames) {
		val parameterList = if (parameters.empty) {
				"()"
			} else {
				parameters.join("(", ", ", ")", [parameterType.simpleName + if(showNames) " " + name else ""])
			}

		createStyledOutlineText(name + parameterList, prefix.type.simpleName)
	}

	def image(TUPort it) {
		"uml2/Port.gif"
	}

	def text(TUPort it) {
		var text = new StyledString(name);
		if (behavior) {
			text = text.append(new StyledString(
				" (behavior)",
				StyledString::DECORATIONS_STYLER
			))
		}

		return text;
	}

	def image(TUPortMember it) {
		"uml2/Property.gif"
	}

	def text(TUPortMember it) {
		createStyledOutlineText(if(required) "required" else "provided", interface.name)
	}

	def image(TUState it) {
		switch (type) {
			case PLAIN,
			case COMPOSITE: "uml2/State.gif"
			case INITIAL: "uml2/Pseudostate_initial.gif"
			case CHOICE: "uml2/Pseudostate_choice.gif"
		}
	}

	def image(TUEntryOrExitActivity it) {
		"uml2/Activity.gif"
	}

	def text(TUEntryOrExitActivity it) {
		if(entry) "entry" else "exit"
	}

	def image(TUTransition it) {
		"uml2/Transition.gif"
	}

	def image(TUTransitionEffect it) {
		"uml2/Activity.gif"
	}

	def text(TUTransitionEffect it) {
		"effect"
	}

	def image(TUTransitionGuard it) {
		"uml2/Constraint.gif"
	}

	def text(TUTransitionGuard it) {
		"guard"
	}

	def image(TUTransitionVertex it) {
		"uml2/Property.gif"
	}

	def text(TUTransitionVertex it) {
		createStyledOutlineText(if(from) "from" else "to", vertex.name)
	}

	def image(TUTransitionTrigger it) {
		"uml2/Trigger.gif"
	}

	def text(TUTransitionTrigger it) {
		createStyledOutlineText("trigger", trigger.name)
	}

	def image(TUTransitionPort it) {
		"uml2/Port.gif"
	}

	def text(TUTransitionPort it) {
		createStyledOutlineText("port", port.name)
	}

	def private multiplicityAsString(TUAssociationEnd it) {
		if (container) {
			"0..1"
		} else if (multiplicity == null) {
			"1"
		} else if (multiplicity.any) {
			"*"
		} else {
			multiplicity.lower + if (multiplicity.isUpperSet) {
				".." + if(multiplicity.isUpperInf) "*" else multiplicity.upper
			} else {
				""
			}
		}
	}

	def private propertiesAsString(TUAssociationEnd it) {
		if (notNavigable || container) {
			var propString = " (";

			if(notNavigable) propString += "hidden ";
			if(container) propString += "container ";

			return propString.substring(0, propString.length - 1) + ")";
		} else {
			return "";
		}
	}

	/**
	 * Creates a StyledString, concatenating the given name and details
	 * section with a colon, where the latter will be formatted with the
	 * default decorations styler.
	 */
	def private createStyledOutlineText(String name, String details) {
		new StyledString(name).append(new StyledString(
			" : " + details,
			StyledString::DECORATIONS_STYLER
		))
	}

}
