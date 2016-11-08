visualizer.linkholders = {}
visualizer.linkholders.Link = function (link){
	if (this.constructor === visualizer.linkholders.Link) {
      throw new Error("Can't instantiate abstract class!");
    }
	var clazz = this;
	clazz._gridRoute = link.route;
	/*var turningPoints = []
	for (var i = 1; i < clazz._gridRoute.length - 1; ++i){
		var before = clazz._gridRoute[i - 1];
		var current = clazz._gridRoute[i];
		var after = clazz._gridRoute[i];
		if (before.x !== after.x && before.y !== after.y ){
			turningPoints.push(clazz._gridRoute[i]);
		}
	}
	if (turningPoints.length = 0){
		turningPoints.push(clazz._gridRoute[clazz._gridRoute.length / 2]);
	}
	clazz._gridRoute = turningPoints;*/
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
	//console.log(clazz);
	//clazz._sideFrom = clazz._getDirectionFromAToB(route[0],route[1]);
	//clazz._sideTo = clazz._getDirectionFromAToB(route[route.length -  1],route[route.length - 2]);
	clazz._gridRoute = route.slice(1,-1);
}

visualizer.linkholders.OrthogonalLink.prototype = Object.create(visualizer.linkholders.Link.prototype);
visualizer.linkholders.OrthogonalLink.prototype.constructor = visualizer.linkholders.OrthogonalLink;

visualizer.linkholders.OrthogonalLink.prototype.SIDES = {
	LEFT : 0,
	TOP : 1,
	RIGHT : 2,
	BOTTOM : 3
};
visualizer.linkholders.OrthogonalLink.prototype._getDirectionFromAToB = function(a, b){
	var direction = undefined;
	if (a.x > b.x){
		direction = this.SIDES.RIGHT;
	}else if (a.x < b.x){
		direction = this.SIDES.LEFT;
	}else if (a.y < b.y){
		direction = this.SIDES.TOP;
	}else if (a.y > b.y){
		direction = this.SIDES.BOTTOM;
	}else{
		throw new Error('Invalid direction');
	}
	return direction;
}


visualizer.linkholders.ClassAttributeLink = function (link){
	clazz = this;
	visualizer.linkholders.OrthogonalLink.call(clazz, link);
	var sourceMarker = [];
	var targetMarker = [];
	var sourceOffset = 0;
	var targetOffset = 0;
	if (link.type === 'composition'){
		sourceMarker.push('M 30 0 L 15 7.5 L 0 0 L 15 -7.5 z');
		sourceOffset += 30;
	}
	if (link.from.navigable){
			sourceMarker.push(clazz._generateNavigabilityMarker(sourceOffset));
	}
	if (link.to.navigable){
			targetMarker.push(clazz._generateNavigabilityMarker(targetOffset));
	}
	var linkData = {  
		'source':{  
			'id':link.fromID
		},
		'target':{  
			'id':link.toID
		},
		'attrs': { 
			'.marker-source': {
				'd':targetMarker.join(' '),
				'fill': 'black'
			},
			'.marker-target': {
				'd':sourceMarker.join(' '),
				'fill': 'black'
			}
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
						'text':visualizer.Utils.MAPS.VISIBILITY_MAP[link.to.visibility] + ' ' + link.to.name
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
						'text':link.to.multiplicity
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
						'text':visualizer.Utils.MAPS.VISIBILITY_MAP[link.from.visibility] + ' ' + link.from.name
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
						'text':link.from.multiplicity
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
