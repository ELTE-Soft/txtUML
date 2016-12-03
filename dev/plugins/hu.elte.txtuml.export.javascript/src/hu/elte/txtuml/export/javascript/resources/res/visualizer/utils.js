if (typeof visualizer === 'undefined'){
	visualizer = {}
}
visualizer.Utils = function(){
	
};

visualizer.Utils.MAPS = {
	DIAGRAMTYPE_TO_COLLECTION_PROPERTY : {
		'class' : 'classDiagrams',
		'state' : 'stateMachines'
	},
	VISIBILITY_MAP : {
		'public' : '+',
		'package' : '~',
		'protected' : '#',
		'private' : '-'
	},
	NON_SCALABLE_PSEUDOSTATE_KINDS : [
		'initial'
	]
}

