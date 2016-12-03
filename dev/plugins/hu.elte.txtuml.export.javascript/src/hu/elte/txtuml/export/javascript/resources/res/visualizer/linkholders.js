visualizer.linkholders = {}
visualizer.linkholders.Link = function (link){
	if (this.constructor === visualizer.linkholders.Link) {
      throw new Error("Can't instantiate abstract class!");
    }
	this._gridRoute = link.route;
	this._link = null;
}

visualizer.linkholders.Link.prototype.getLink =  function(){
	return this._link;
};

visualizer.linkholders.Link.prototype.getRoute = function(route){
	return this._gridRoute;
}

visualizer.linkholders.Link.prototype.setPixelRoute = function(route){
	this._link.set('vertices', route);
}

visualizer.linkholders.OrthogonalLink = function (link){
	visualizer.linkholders.Link.call(this, link);
	var route = this._gridRoute;
}

visualizer.linkholders.OrthogonalLink.prototype = Object.create(visualizer.linkholders.Link.prototype);
visualizer.linkholders.OrthogonalLink.prototype.constructor = visualizer.linkholders.OrthogonalLink;

visualizer.linkholders.ClassAttributeLink = function (link){
	visualizer.linkholders.OrthogonalLink.call(this, link);
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
	
	var flipMap = {
		'from' : 'to',
		'to' : 'from'
	}
		
	_.each(markers, function(value, key){
		if (link[key].composition){
			value.markers.push('M 30 0 L 15 7.5 L 0 0 L 15 -7.5 z');
			value.offset += 30;
		}
		if (link[flipMap[key]].navigable){
			value.markers.push(this._generateNavigabilityMarker(value.offset));
		}
	}, this);
	
	var linkData = {  
		'source':{  
			'id':link.fromID
		},
		'target':{  
			'id':link.toID
		},
		'attrs': { 
			'.marker-source': {
				'd':markers.to.markers.join(' '),
				'fill': 'black'
			},
			'.marker-target': {
				'd':markers.from.markers.join(' '),
				'fill': 'black'
			}
		},
		'sourceName':visualizer.Utils.MAPS.VISIBILITY_MAP[link.from.visibility] + ' ' + link.from.name,
		'sourceNum':link.from.multiplicity,
		'targetName':visualizer.Utils.MAPS.VISIBILITY_MAP[link.to.visibility] + ' ' + link.to.name,
		'targetNum':link.to.multiplicity,
		'name':link.name
	}
	this._link = new visualizer.shapes.AttributeAssociation(linkData);
	
	
	
	
}

visualizer.linkholders.ClassAttributeLink.prototype = Object.create(visualizer.linkholders.OrthogonalLink.prototype);
visualizer.linkholders.ClassAttributeLink.prototype.constructor = visualizer.linkholders.ClassAttributeLink;

visualizer.linkholders.ClassAttributeLink.prototype._generateNavigabilityMarker = function(offset){
	return ['M', offset + 0, 0, 'L', offset + 15, 0, 'M', offset + 0, 0,'L', offset + 15, -7.5, 'M',offset + 0, 0, 'L', offset + 15, 7.5].join(' ');
}

visualizer.linkholders.ClassNonAttributeLink = function (link){
	visualizer.linkholders.OrthogonalLink.call(this, link);
	var linkData = {  	
		'source':{  
			'id':link.fromID 
		},
		'target':{  
			'id':link.toID
		}	
	};
	
	switch (link.type){
		case 'generalization': this._link = new visualizer.shapes.Generalization(linkData); break;
		default: throw new Error('Unexpected link type: ' + link.type); break;
	}	
}
visualizer.linkholders.ClassNonAttributeLink.prototype = Object.create(visualizer.linkholders.OrthogonalLink.prototype);
visualizer.linkholders.ClassNonAttributeLink.prototype.constructor = visualizer.linkholders.ClassNonAttributeLink;

visualizer.linkholders.TransitionLink = function (link){
	visualizer.linkholders.OrthogonalLink.call(this, link);
	var linkData = {  	
		'source':{  
			'id':link.fromID 
		},
		'target':{  
			'id':link.toID
		}
	};
	if (_.has(link,'trigger') && link.trigger.length > 0){
		linkData.trigger = link.trigger;
	}
	
	this._link = new visualizer.shapes.Transition(linkData);
}
visualizer.linkholders.TransitionLink.prototype = Object.create(visualizer.linkholders.OrthogonalLink.prototype);
visualizer.linkholders.TransitionLink.prototype.constructor = visualizer.linkholders.TransitionLink;

