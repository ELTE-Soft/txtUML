visualizer.shapes = {};
visualizer.shapes.Class = joint.shapes.uml.Class.extend({
		defaults: joint.util.deepSupplement({
			type: 'visualizer.shapes.Class',
			attrs: {				
				'.uml-class-name-text': {
					'font-family': visualizer.Fonts.default.getFamily(),
					'font-size': visualizer.Fonts.default.getSize()
				},
				'.uml-class-attrs-text': {
					'font-family': visualizer.Fonts.default.getFamily(),
					'font-size': visualizer.Fonts.default.getSize()
				},
				'.uml-class-methods-text': {
					'font-family': visualizer.Fonts.default.getFamily(),
					'font-size': visualizer.Fonts.default.getSize()
				}
			}

		}, joint.shapes.uml.Class.prototype.defaults),
		
		initialize: function() {

			joint.shapes.uml.Class.prototype.initialize.apply(this, arguments);
			this.on({
				'change:size': this.updateRectangles
			});
		},
		
		updateRectangles: function() {
			// if init is false, then we are already have a size and need to provide approximate width and height for the rectangles
			// to fit the new size and avoid scaling issues in IE (or other browser not supporting svg non-scaling-stroke attribute)
			var init = typeof this._init === 'undefined' ? true : this._init;
			if (!init){
				var size = this.get('size');
			}
			
			var font = visualizer.Fonts.default;
			
			var attrs = this.get('attrs');
			

			var rects = [
				{ type: 'name', text: this.getClassName() },
				{ type: 'attrs', text: this.get('attributes') },
				{ type: 'methods', text: this.get('methods') }
			];

			var offsetY = 0;
			var rectHeight = 0;
			var maxChars = 0;
			_.each(rects, function(rect) {
				

				var lines = _.isArray(rect.text) ? rect.text : [rect.text];
				rectHeight = lines.length * font.getContainerHeight(0) + (rect.type == 'name' ? 15 : 10) ;
				rectHeight = Math.max(rectHeight * 2, 30);
				

				_.each(lines, function(line){
					maxChars = Math.max(maxChars, line.length);
				})
				
				attrs['.uml-class-' + rect.type + '-text'].text = lines.join('\n');
				attrs['.uml-class-' + rect.type + '-rect'].height = rectHeight;
				attrs['.uml-class-' + rect.type + '-rect'].transform = 'translate(0,' + offsetY + ')';
				
				offsetY += rectHeight;
			},this);
			
			if (init){
				var minSize = {}; 
				minSize.height = offsetY * 0.5 + 20;  
				minSize.width = maxChars * font.getContainerWidth(0) + 5;
				this.set('size',minSize);
				this.attr('rect/width', minSize.width * 2);
				this._init = false;
			}else{
				this.attr('.uml-class-methods-rect/height', (size.height - 20) * 2 - (offsetY - rectHeight));  
				this.attr('rect/width', size.width * 2);
			}

			
		}
	});
visualizer.shapes.ClassView = joint.shapes.uml.ClassView;
	
visualizer.shapes.Abstract = visualizer.shapes.Class.extend({
		defaults: joint.util.deepSupplement({
			type: 'visualizer.shapes.Abstract',
			attrs: {
				'.uml-class-name-rect': { fill : '#e74c3c' },
				'.uml-class-attrs-rect': { fill : '#c0392b' },
				'.uml-class-methods-rect': { fill : '#c0392b' }
			}
		}, visualizer.shapes.Class.prototype.defaults),

		getClassName: function() {
			return ['<<Abstract>>', this.get('name')];
		}
	});
visualizer.shapes.AbstractView = visualizer.shapes.ClassView;
	
visualizer.shapes.AttributeAssociation = joint.shapes.uml.Association.extend({
	'defaults': joint.util.deepSupplement({
		
		'type': 'visualizer.shapes.AttributeAssociation',
		'attrs':{
			'snapLabels': true
		},
		'sourceName': 'sourceName',
		'sourceNum': 'sourceNum',
		'targetName': 'targetName',
		'targetNum': 'targetNum',
		'name': 'name'
		
	}, joint.shapes.uml.Association.prototype.defaults),
	
	'initialize': function(options){
		var labelTypes = ['sourceName', 'sourceNum', 'targetName', 'targetNum', 'name'];
		var offsets = [-20, 20, 20, -20, 10];
		var distances = [0, 0, 1, 1, 0.5];

		_.each(labelTypes, function(labelType,key){
			this.label(key, { 
				'position':{  
					'distance': distances[key],
					'offset': offsets[key]
				},
				'attrs':{  
					'text':{  
						'font-family': visualizer.Fonts.links.getFamily(),
						'font-size': visualizer.Fonts.links.getSize(),
						'text': this.attributes[labelType]
					}
				}
			});
		},this);
		
		joint.shapes.uml.Association.prototype.initialize.apply(this, options);
	}
});

visualizer.shapes.Generalization = joint.shapes.uml.Generalization.extend({
	'defaults': joint.util.deepSupplement({
		
		'type': 'visualizer.shapes.AttributeAssociation',
		'attrs': { 
			'.marker-source': { d:'M 15 0 L 0 7.5 L 15 15 z', fill: 'white'},
			'.marker-target': { d:'', fill: 'none'}
			
		}
	}, joint.shapes.uml.Generalization.prototype.defaults)
});


visualizer.shapes.Transition = joint.shapes.uml.Transition.extend({
	'defaults': joint.util.deepSupplement({
		
		'type': 'visualizer.shapes.Transition',
		'trigger': null
		
	}, joint.shapes.uml.Transition.prototype.defaults),
	
	'initialize': function(options){
		
		if (this.attributes.trigger){
			this.label(0, { 
					'position':{  
						'distance': 0.5,
						'offset': 10
					},
					'attrs':{  
						'text':{  
							'font-family': visualizer.Fonts.links.getFamily(),
							'font-size': visualizer.Fonts.links.getSize(),
							'text': this.attributes.trigger
						}
					}
				});
		}
		
		joint.shapes.uml.Transition.prototype.initialize.apply(this, options);
	}
});

visualizer.shapes.State = joint.shapes.uml.State.extend({		
		defaults: joint.util.deepSupplement({
			type: 'visualizer.shapes.State',
			attrs: {
				'.uml-state-name': {
					'fill': '#000000', 'font-family': visualizer.Fonts.default.getFamily(), 'font-size': visualizer.Fonts.default.getSize()
				},
				'.uml-state-events': {
					'fill': '#000000', 'font-family': visualizer.Fonts.default.getFamily(), 'font-size': visualizer.Fonts.default.getSize()
				}
			}
		},joint.shapes.uml.State.prototype.defaults),
		initialize: function() {

			joint.shapes.uml.State.prototype.initialize.apply(this, arguments);
			this.on({
				'change:size': this.fixBorders
			});
			this.autoSize();
		},
		autoSize: function() {
			var font = visualizer.Fonts.default;
			var attrs = this.get('attrs');

			var rects = [
				{ type: 'name', text: this.get('name') },
				{ type: 'events', text: this.get('events') }
			];

			var offsetY = 0;
			var rectHeight = 0;
			var maxChars = 0;
			_.each(rects, function(rect) {
				

				var lines = _.isArray(rect.text) ? rect.text : [rect.text];
				rectHeight = lines.length * font.getContainerHeight(3);
				

				_.each(lines, function(line){
					maxChars = Math.max(maxChars, line.length);
				})
				
				offsetY += rectHeight;
			});
			var minSize = {
				'width' : maxChars * font.getContainerWidth(0) + 35,
				'height' : offsetY + 20
			}
			this.set('size', minSize);
		},
		fixBorders: function(){
			var size = this.get('size');
			this.attr('rect/width',size.width);
			this.attr('rect/height',size.height);
		}
		
});

visualizer.shapes.StartState = joint.shapes.uml.StartState.extend({
	markup: '<g class="rotatable"><g class="scalable"><circle class="uml-startstate-circle"/></g><rect class="uml-startstate-name-bg" /><text class="uml-startstate-name"/></g>',
    defaults: joint.util.deepSupplement({

        type: 'visualizer.shapes.StartState',
		attrs:{
			'text':{
                'ref': '.uml-startstate-circle', 'ref-x': .5, 'ref-y': .5, 'text-anchor': 'middle',
                'fill': '#000000', 'font-family':visualizer.Fonts.pseudostates.getFamily() , 'font-size': visualizer.Fonts.pseudostates.getSize(),
				'text' : ''
			},
			'.uml-startstate-name-bg':{
				'ref': 'text', 'ref-x':-1, 'ref-y':-1,
				'fill':'white',
				'width':1,
				'height':visualizer.Fonts.pseudostates.getContainerHeight(1)
			},
		},
		'size':{
			'width':30,
			'height':30			
		}		

    }, joint.shapes.uml.StartState.prototype.defaults),
	initialize: function(){
		this.on({
			'change:text/text': this.updateName
		});
		joint.shapes.uml.StartState.prototype.initialize.apply(this, arguments);
		this.updateName();
	},
	updateName: function(){
		var font = visualizer.Fonts.pseudostates;
		var str = this.attr('text/text');
		this.attr('.uml-startstate-name-bg/width', font.getContainerBoxSize(str, 1, 0).width);
	}

});
visualizer.shapes.Choice = joint.shapes.basic.Generic.extend({
	markup: '<g class="rotatable"><g class="scalable"><path class="uml-choice-body"/><path class="uml-choice-fill"/></g><rect class="uml-choice-name-bg" /><text class="uml-choice-name"/></g>',
    defaults: joint.util.deepSupplement({

        type: 'visualizer.shapes.Choice',
		attrs:{
			'.uml-choice-body':{
				'd': 'M 0 100 L 100 0 L 200 100 L 100 200 z',
				'fill':'black'
			},
			'.uml-choice-fill':{
				'd': 'M 4 100 L 100 4 L 196 100 L 100 196 z',
				'fill': 'white'
			},
			'text':{
                'ref': '.uml-choice-body', 'ref-x': .5, 'ref-y': .5,  'text-anchor': 'middle', 'y':'0.4em',
                'fill': '#000000', 'font-family': visualizer.Fonts.pseudostates.getFamily(), 'font-size': visualizer.Fonts.pseudostates.getSize(),
				'text' : ''
			},
			'.uml-choice-name-bg':{
				'ref': 'text', 'ref-x':-1, 'ref-y':-1,
				'fill':'white',
				'width':1,
				'height':visualizer.Fonts.pseudostates.getContainerHeight(1)
			},
			
		},
		'size':{
			'width':60,
			'height':60
		}
    }, joint.shapes.basic.Generic.prototype.defaults),
	initialize: function(){
		this.on({
			'change:text/text': this.updateName
		});
		joint.shapes.basic.Generic.prototype.initialize.apply(this, arguments);
		this.updateName();
	},
	updateName: function(){
		var str = this.attr('text/text');
		var font = visualizer.Fonts.pseudostates;
		
		var width = font.getContainerBoxSize(str, 1, 0).width;
		this.attr('.uml-choice-name-bg/width', width);
		var oldSize = this.get('size');
		var size = {
			'width': Math.max(width + 20, oldSize.width),
			'height': oldSize.width
		}
		this.set('size',size);
	}

});
