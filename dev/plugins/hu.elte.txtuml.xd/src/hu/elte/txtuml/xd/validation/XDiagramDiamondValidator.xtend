package hu.elte.txtuml.xd.validation

import org.eclipse.xtext.validation.Check
import hu.elte.txtuml.xd.xDiagramDefinition.XDDiamondInstruction

class XDiagramDiamondValidator extends XDiagramGroupValidator {
	final val DIAMOND_ARG_NAMES = #["top", "right", "bottom", "left"];
	
	@Check
	def checkDiamondArgumentNamesConsistent(XDDiamondInstruction diamond){
		val hasEmptyArgsName = diamond.args.wrapped.expressions.exists[x|x.argName == null];
		val hasGivenArgsName = diamond.args.wrapped.expressions.exists[x|x.argName != null];
		
		if (hasEmptyArgsName && hasGivenArgsName){
			error("The diamond operator can have arguments with names or exactly 4 arguments without names.", diamond, null);
		}
	}
	
	@Check
	def checkDiamondArgumentNamesValid(XDDiamondInstruction diamond){
		diamond.args.wrapped.expressions
			.filter[x | x.argName != null]
			.filter[x | !DIAMOND_ARG_NAMES.exists[y | y.equals(x.argName)]]
			.forEach[x | 
				error("The argument name '" + x.argName + "' is invalid for the diamond operator. "+
				      "Valid argument names are 'top', 'right', 'bottom', 'left'", x, null)
			];
	}
	
	@Check
	def checkDiamondArgumentNamesUnique(XDDiamondInstruction diamond){
		if (!diamond.args.wrapped.expressions.exists[x|x.argName != null]) return;
		
		val list = newArrayList();
		diamond.args.wrapped.expressions.forEach[x |
			if (list.contains(x.argName)){
				error("The argument '" + x.argName + "' was defined earlier for this diamond operator.", x, null)
			}
			list.add(x.argName);
		]
	}
	
	@Check
	def checkDiamondArgumentNamesCount(XDDiamondInstruction diamond){
		if (!diamond.args.wrapped.expressions.exists[x|x.argName == null]) return;
		
		if (diamond.args.wrapped.expressions.size() != 4) {
			error("diamond should have exactly 4 anonymous arguments", diamond, null);
		}
	}
}
