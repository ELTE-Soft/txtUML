visualizer.Selector  = function(input){
	
	var params = window.location.hash.substring(1).split('_');
	var paramsValid = params.length === 2 &&
		_.has(visualizer.Utils.MAPS.DIAGRAMTYPE_TO_COLLECTION_PROPERTY, params[0]) && 
		input[visualizer.Utils.MAPS.DIAGRAMTYPE_TO_COLLECTION_PROPERTY[params[0]]].length > params[1];
	
	this._diagramMap = {};
	this._selected = null;
	
	if (paramsValid){
		var collectionName = visualizer.Utils.MAPS.DIAGRAMTYPE_TO_COLLECTION_PROPERTY[params[0]];
		this._selected = input[collectionName][params[1]];
		this._selected.type = params[0];

	}
	_.each(visualizer.Utils.MAPS.DIAGRAMTYPE_TO_COLLECTION_PROPERTY, function(diagramCollectionName, type){			
		if (this._selected === null && input[diagramCollectionName].length > 0){
			this._selected = input[diagramCollectionName][0];
			this._selected.type = type;
		}
		this._diagramMap[type] = [];
		_.each(input[diagramCollectionName], function(diagram){
			this._diagramMap[type].push(diagram.name);
		},this);
		
	},this);
	if (this._selected === null){
		throw new Error('No diagrams provided');
	}

}

visualizer.Selector.prototype.getSelectedDiagram = function(){
	return this._selected;
}

visualizer.Selector.prototype.putLinks = function(table_holder){
	var innerHTML = '<tr>';
	var timestamp = new Date().getTime();
	_.each(this._diagramMap, function(diagrams, type){
		innerHTML += '<td><ul>';
		_.each(diagrams, function(diagramName, key){
			innerHTML += '<li><a href="?refresh=' + timestamp + '#'+ type + '_' + key + '">'+ diagramName +'</a></li>';
		});
		innerHTML += '</ul></td>'
	});
	innerHTML += '</tr>';
	table_holder.append(innerHTML);
}