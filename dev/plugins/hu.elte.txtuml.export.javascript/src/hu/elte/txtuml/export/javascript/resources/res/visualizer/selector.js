visualizer.Selector  = function(input){
	
	var clazz = this;
	
	var params = window.location.hash.substring(1).split('_');
	var paramsValid = params.length === 2 &&
		_.has(visualizer.Utils.MAPS.DIAGRAMTYPE_TO_COLLECTION_PROPERTY, params[0]) && 
		visualizer.Utils.arrayContains(input[params[0]], params[1]);
	
	clazz._diagramMap = {};
	clazz._selected = null;
	
	if (paramsValid){
		var collectionName = visualizer.Utils.MAPS.DIAGRAMTYPE_TO_COLLECTION_PROPERTY[params[0]];
		clazz._selected = input[collectionName][params[1]];
		clazz._selected.type = params[0];
	}
	$.each(visualizer.Utils.MAPS.DIAGRAMTYPE_TO_COLLECTION_PROPERTY, function(type, diagramCollectionName){			
		if (clazz._selected === null && input[diagramCollectionName].length > 0){
			clazz._selected = input[diagramCollectionName][0];
			clazz._selected.type = type;
		}
		clazz._diagramMap[type] = [];
		$.each(input[diagramCollectionName], function(key, diagram){
			clazz._diagramMap[type].push(diagram.name);
		});
		
	});
	if (clazz._selected === null){
		throw new Error('No diagrams loaded')
	}
	
	
	
	
	
}

visualizer.Selector.prototype.getSelectedDiagram = function(){
	return this._selected;
}

visualizer.Selector.prototype.putLinks = function(table_holder){
	var innerHTML = '<tr>';
	var timestamp = new Date().getTime();
	$.each(this._diagramMap, function(type, diagrams){
		innerHTML += '<td><ul>';
		$.each(diagrams, function(key, diagramName){
			innerHTML += '<li><a href="?refresh=' + timestamp + '#'+ type + '_' + key + '">'+ diagramName +'</a></li>';
		});
		innerHTML += '</ul></td>'
	});
	innerHTML += '</tr>';
	table_holder.append(innerHTML);
}