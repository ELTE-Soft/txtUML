visualizer.linkholders = {}
//This is an abstract base class for wrapper classes around JointJS Link models, and abstract layout informations
visualizer.linkholders.Link = function (link) {
	if (this.constructor === visualizer.linkholders.Link) {
		throw new Error("Can't instantiate abstract class!");
	}
	this._name = link.id;
	this._link = null;
}

// returns the fully qualified name of the enclosed link element
visualizer.linkholders.Link.prototype.getElementName = function () {
	return this._name;
};

// returns the id of the enclosed link shape
visualizer.linkholders.Link.prototype.getShapeId = function () {
	return this._link.id;
};

//returns the JointJS model
visualizer.linkholders.Link.prototype.getLink = function () {
	return this._link;
};

//A linkholder for associations and compositions
visualizer.linkholders.ClassAttributeLink = function (link) {
	visualizer.linkholders.Link.call(this, link);

	//markers are constructed while we process linkends
	var markers = {
		'from': {
			'offset': 0,
			'markers': []
		},
		'to': {
			'offset': 0,
			'markers': []
		}
	};

	//map to the other end of the link so we can peek if it's a part of a composition
	var flipMap = {
		'from': 'to',
		'to': 'from'
	}

	_.each(markers, function (value, key) {
		//first we push the composition marker if present
		if (link[flipMap[key]].composition) {
			value.markers.push('M 30 0 L 15 7.5 L 0 0 L 15 -7.5 z');
			value.offset += 30;
		}

		//then the navigibility marker
		if (link[key].navigable) {
			value.markers.push(this._generateNavigabilityMarker(value.offset));
		}
	}, this);

	//JointJS model data
	var linkData = {
		'source': {
			'id': link.fromID
		},
		'target': {
			'id': link.toID
		},
		'attrs': {
			'.marker-source': {
				'd': markers.from.markers.join(' '),
				'fill': 'black'
			},
			'.marker-target': {
				'd': markers.to.markers.join(' '),
				'fill': 'black'
			}
		},
		'vertices': link.route,
		'sourceName': visualizer.Utils.MAPS.VISIBILITY_MAP[link.from.visibility] + ' ' + link.from.name,
		'sourceNum': link.from.multiplicity,
		'targetName': visualizer.Utils.MAPS.VISIBILITY_MAP[link.to.visibility] + ' ' + link.to.name,
		'targetNum': link.to.multiplicity,
		'name': link.name
	}
	this._link = new visualizer.shapes.AttributeAssociation(linkData);

}
//prototype chaining
visualizer.linkholders.ClassAttributeLink.prototype = Object.create(visualizer.linkholders.Link.prototype);
visualizer.linkholders.ClassAttributeLink.prototype.constructor = visualizer.linkholders.ClassAttributeLink;

//returns a path string for a navigibility marker translated by offset
visualizer.linkholders.ClassAttributeLink.prototype._generateNavigabilityMarker = function (offset) {
	return ['M', offset + 0, 0, 'L', offset + 15, 0, 'M', offset + 0, 0, 'L', offset + 15, -7.5, 'M', offset + 0, 0, 'L', offset + 15, 7.5].join(' ');
}

//A linkholder for generalizations
visualizer.linkholders.ClassNonAttributeLink = function (link) {
	visualizer.linkholders.Link.call(this, link);
	var linkData = {
		'source': {
			'id': link.fromID
		},
		'target': {
			'id': link.toID
		},
		'vertices': link.route
	};

	switch (link.type) {
	case 'generalization':
		this._link = new visualizer.shapes.Generalization(linkData);
		break;
	default:
		throw new Error('Unexpected link type: ' + link.type);
		break;
	}
}
//prototype chaining
visualizer.linkholders.ClassNonAttributeLink.prototype = Object.create(visualizer.linkholders.Link.prototype);
visualizer.linkholders.ClassNonAttributeLink.prototype.constructor = visualizer.linkholders.ClassNonAttributeLink;

visualizer.linkholders.TransitionLink = function (link) {
	visualizer.linkholders.Link.call(this, link);
	var linkData = {
		'source': {
			'id': link.fromID
		},
		'target': {
			'id': link.toID
		},
		'vertices': link.route
	};
	if (_.has(link, 'trigger') && link.trigger.length > 0) {
		linkData.trigger = link.trigger;
	}
	if(linkData.source.id == linkData.target.id){
		this._link = new visualizer.shapes.TransitionReflexive(linkData);
	}
	else {
		this._link = new visualizer.shapes.Transition(linkData);
	}
}
//prototype chaining
visualizer.linkholders.TransitionLink.prototype = Object.create(visualizer.linkholders.Link.prototype);
visualizer.linkholders.TransitionLink.prototype.constructor = visualizer.linkholders.TransitionLink;
