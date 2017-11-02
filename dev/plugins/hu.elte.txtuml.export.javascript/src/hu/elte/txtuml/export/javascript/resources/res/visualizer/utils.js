//creates visualizer namespace
visualizer = {}

//collection of helper functions and maps
visualizer.Utils = {}

visualizer.Utils.MAPS = {
	//maps diagram type to collection names (as they appear in input)
	DIAGRAMTYPE_TO_COLLECTION_PROPERTY: {
		'class': 'classDiagrams',
		'state': 'stateMachines'
	},
	//textual representations of visibilities
	VISIBILITY_MAP: {
		'public': '+',
		'package': '~',
		'protected': '#',
		'private': '-'
	}
}

// returns a space-separated list of modifier CSS classes for the given class member
visualizer.Utils.getModifierClasses = function(classMember) {
	var modifierList = [];

	if (classMember.isAbstract) {
		modifierList.push('abstract');
	}
	if (classMember.isStatic) {
		modifierList.push('static');
	}

	return modifierList.join(' ');
}
