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
