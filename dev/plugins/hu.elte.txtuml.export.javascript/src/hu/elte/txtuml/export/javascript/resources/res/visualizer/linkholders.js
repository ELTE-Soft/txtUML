visualizer.linkholders = {}
visualizer.linkholders.Link = function (link){
	if (this.constructor === visualizer.linkholders.Link) {
      throw new Error("Can't instantiate abstract class!");
    }
	var clazz = this;
	clazz._gridRoute = link.route;
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
	clazz = this;
	visualizer.linkholders.Link.call(clazz, link);
	var route = clazz._gridRoute;
}

visualizer.linkholders.OrthogonalLink.prototype = Object.create(visualizer.linkholders.Link.prototype);
visualizer.linkholders.OrthogonalLink.prototype.constructor = visualizer.linkholders.OrthogonalLink;

visualizer.linkholders.OrthogonalLink.prototype.SIDES = {
	LEFT : 0,
	TOP : 1,
	RIGHT : 2,
	BOTTOM : 3
};

visualizer.linkholders.ClassAttributeLink = function (link){
	clazz = this;
	visualizer.linkholders.OrthogonalLink.call(clazz, link);
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
			value.markers.push(clazz._generateNavigabilityMarker(value.offset));
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
			},
			'snapLabels' : true
		},
		'labels':[  
			{  
				'position':{  
					'distance':0,
					'offset':-20
				},
				'attrs':{  
					'text':{  
						'font-family': '"Lucida Console", Monaco, monospace',
						'text':visualizer.Utils.MAPS.VISIBILITY_MAP[link.from.visibility] + ' ' + link.from.name
					}
					
			
				}
			},
			{  
				'position':{  
					'distance':0,
					'offset':20
				},
				'attrs':{  
					'text':{  
						'font-family': '"Lucida Console", Monaco, monospace',
						'text':link.from.multiplicity
					}
				}
			},
			{  
				'position':{  
					'distance':1,
					'offset':20
				},
				'attrs':{  
					'text':{  
						'font-family': '"Lucida Console", Monaco, monospace',
						'text':visualizer.Utils.MAPS.VISIBILITY_MAP[link.to.visibility] + ' ' + link.to.name
					}
				}
			},
			{  
				'position':{  
					'distance':1,
					'offset':-20
				},
				'attrs':{  
					'text':{  
						'font-family': '"Lucida Console", Monaco, monospace',
						'text':link.to.multiplicity
					}
				}
			},
			{  
				'position':{  
					'distance':0.5,
					'offset':10
				},
				'attrs':{  
					'text':{  
						'font-family': '"Lucida Console", Monaco, monospace',
						'text':link.name,
					}
				}
			}
		]
	};
	clazz._link = new visualizer.shapes.AttributeAssociation(linkData);
	
	
	
	
}

visualizer.linkholders.ClassAttributeLink.prototype = Object.create(visualizer.linkholders.OrthogonalLink.prototype);
visualizer.linkholders.ClassAttributeLink.prototype.constructor = visualizer.linkholders.ClassAttributeLink;

visualizer.linkholders.ClassAttributeLink.prototype._generateNavigabilityMarker = function(offset){
	return ['M', offset + 0, 0, 'L', offset + 15, 0, 'M', offset + 0, 0,'L', offset + 15, -7.5, 'M',offset + 0, 0, 'L', offset + 15, 7.5].join(' ');
}

visualizer.linkholders.ClassNonAttributeLink = function (link){
	clazz = this;
	visualizer.linkholders.OrthogonalLink.call(clazz, link);
	var linkData = {  	
		'source':{  
			'id':link.fromID 
		},
		'target':{  
			'id':link.toID
		},
		'attrs': { 
			'.marker-source': { d:'M 15 0 L 0 7.5 L 15 15 z', fill: 'white'},
			'.marker-target': { d:'', fill: 'none'}
			
		}
	
	};
	
	switch (link.type){
		case 'generalization': clazz._link = new joint.shapes.uml.Generalization(linkData); break;
		default: throw new Error('Unexpected link type: ' + link.type); break;
	}	
}
visualizer.linkholders.ClassNonAttributeLink.prototype = Object.create(visualizer.linkholders.OrthogonalLink.prototype);
visualizer.linkholders.ClassNonAttributeLink.prototype.constructor = visualizer.linkholders.ClassNonAttributeLink;

visualizer.linkholders.TransitionLink = function (link){
	clazz = this;
	visualizer.linkholders.OrthogonalLink.call(clazz, link);
	var linkData = {  	
		'source':{  
			'id':link.fromID 
		},
		'target':{  
			'id':link.toID
		},
		'labels':[  
			{  
				'position':{  
					'distance':0.5,
					'offset':-20
				},
				'attrs':{  
					'text':{  
						'font-family': '"Lucida Console", Monaco, monospace',
						'text':link.name
					}
					
			
				}
			}
		]
	
	};
	
	clazz._link = new joint.shapes.uml.Transition(linkData);
}
visualizer.linkholders.TransitionLink.prototype = Object.create(visualizer.linkholders.OrthogonalLink.prototype);
visualizer.linkholders.TransitionLink.prototype.constructor = visualizer.linkholders.TransitionLink;

