if (typeof visualizer === 'undefined'){
	visualizer = {}
}
visualizer.Utils = function(){
	
};

visualizer.Utils.arrayhasIndex = function(array, index){
	return typeof array[index] !== 'undefined';
}

visualizer.Utils.arrayContains = function(array, value){
	return $.inArray(value, array) > -1;
}

visualizer.Utils.reverseMap = function(map){
	var reverseMap = {};
	$.each(map, function(key, value){
		reverseMap.value = key;
	})
	return reverseMap;
}

visualizer.Utils.MAPS = {
	DIAGRAMTYPE_TO_COLLECTION_PROPERTY : {
		'class' : 'classDiagrams',
		'state' : 'stateChartDiagrams'
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

visualizer.Utils.INT_MAX = 2147483646;
visualizer.Utils.INT_MIN = -2147483647;
