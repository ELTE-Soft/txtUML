//Responsible from selecting a diagram from input and generating links for other diagrams
visualizer.Selector = function (input) {
	//getting url anchor
	var params = window.location.hash.substring(1).split('_');

	//check if anchor is valid (diagramType exists and index is in bounds)
	var paramsValid = params.length >= 2 &&
		_.has(visualizer.Utils.MAPS.DIAGRAMTYPE_TO_COLLECTION_PROPERTY, params[0]) && //valid type
		!isNaN(params[1]) && //index is not NaN
		parseInt(Number(params[1])) == params[1] && //index is integer
		Number(params[1]) >= 0 &&
		input[visualizer.Utils.MAPS.DIAGRAMTYPE_TO_COLLECTION_PROPERTY[params[0]]].length > Number(params[1]);
	//index is in bounds

	this._diagramMap = {}; //stores the names of diagrams of the input grouped by diagram types
	this._instances = {};
	this._selected = null;

	//if params are valid set the selected diagram and type
	if (paramsValid) {
		var collectionName = visualizer.Utils.MAPS.DIAGRAMTYPE_TO_COLLECTION_PROPERTY[params[0]];
		this._selected = input[collectionName][Number(params[1])];
		this._selected.type = params[0];
		this._selected.inst = params[2];
	}

	//iterate diagram types
	_.each(visualizer.Utils.MAPS.DIAGRAMTYPE_TO_COLLECTION_PROPERTY, function (diagramCollectionName, type) {

		//if diagram is not yet selected and there is at least one diagram of this type then set the first
		if (this._selected === null && input[diagramCollectionName].length > 0) {
			this._selected = input[diagramCollectionName][0];
			this._selected.type = type;
		}

		//store all diagram names of this type
		this._diagramMap[type] = [];
		_.each(input[diagramCollectionName], function (diagram) {
			this._diagramMap[type].push(diagram.name);
		}, this);

	}, this);

	//if there wasn't any diagram then throw an exception
	if (this._selected === null) {
		throw new Error('No diagrams provided');
	}
}

//returns the selected diagram
visualizer.Selector.prototype.getSelectedDiagram = function () {
	return this._selected;
}

//sets diagram instances
visualizer.Selector.prototype.setInstances = function (inst) {
	this._instances = inst;
	var timestamp = new Date().getTime(); //this ensures that the URL changes and the page is reloaded
	var self = this;
	_.each(this._diagramMap, function (diagrams, type) {
		if (type != "state") return;
		_.each(diagrams, function (diagramName, key) {
			try {
				if (diagramName === self._selected.name) {
					var machineName = input.stateMachines[key].machineName;
					$('#diagInst' + key).html(self._instances.filter(function (diis) {
						return diis.name.split('@')[0] == machineName;
					}).map(function (diis) {
						return '<li><a href="?refresh=' + timestamp + '#' + type + '_' + key + '_' + diis.name + '">' + diis.id + '</a></li>';
					}).join(""));
				}
			} catch (flu) {
				$('#diagInst' + key).html("");
			}
		});
	});

}

//puts links (pointing to diagrams present in the input) into the given tbody (one row, one column per diagram type present)
visualizer.Selector.prototype.putLinks = function (tbody) {
	var innerHTML = '<tr>';
	var timestamp = new Date().getTime(); //this ensures that the URL changes and the page is reloaded
	_.each(this._diagramMap, function (diagrams, type) {
		innerHTML += '<td><ul>';
		_.each(diagrams, function (diagramName, key) {
			//anchor is in a format of <diagram type>_<diagram_index>
			innerHTML += '<li><a title="' + diagramName + '"href="?refresh=' + timestamp + '#' + type + '_' + key + '">' + diagramName.replace(/^.*\.([^.]*)$/, "$1") + '</a>' + (type == "state" ? '<ul id=diagInst' + key + '></ul>' : '') + '</li>'
		});
		innerHTML += '</ul></td>'
	});
	innerHTML += '</tr>';
	tbody.append(innerHTML);
}
