visualizer.shapes = {};
visualizer.shapes.Class = joint.shapes.uml.Class.extend({
		defaults: joint.util.deepSupplement({

			type: 'visualizer.shapes.Class',

			attrs: {
				rect: { 'width': 200 },
				'.uml-class-name-text': {
					'font-family': '"Lucida Console", Monaco, monospace'
				},
				'.uml-class-attrs-text': {
					'font-family': '"Lucida Console", Monaco, monospace'
				},
				'.uml-class-methods-text': {
					'font-family': '"Lucida Console", Monaco, monospace'
				}
			}

		}, joint.shapes.uml.Class.prototype.defaults),
		
		updateRectangles: function() {
			// if init is false, then we are already have a size and need to provide approximate width and height for the rectangles
			// to fit the new size and avoid scaling issues in IE (or other browser not supporting svg non-scaling-stroke attribute)
			
			var init = typeof this._init === 'undefined' ? true : this._init;
			if (!init){
				var size = this.get('size');
			}
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
				rectHeight = lines.length * 20 + 20;
				

				_.each(lines, function(line){
					maxChars = Math.max(maxChars, line.length);
				})
				
				attrs['.uml-class-' + rect.type + '-text'].text = lines.join('\n');
				attrs['.uml-class-' + rect.type + '-rect'].height = rectHeight;
				attrs['.uml-class-' + rect.type + '-rect'].transform = 'translate(0,' + offsetY + ')';
				
				offsetY += rectHeight;
			},this);
			
			
			// autosizing TODO: less font-relied solution
			if (init){
				var minSize = {};
				minSize.height = offsetY * 0.5 + 20; 
				minSize.width = maxChars * 7.2 + 12;
				this.set('size',minSize);
				this._init = false;
			}else{
				this.attr('.uml-class-methods-rect/height', (size.height - 20) * 2 - (offsetY - rectHeight));  
				this.attr('rect/width', size.width);
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
	
visualizer.shapes.Interface = visualizer.shapes.Class.extend({
		type: 'visualizer.shapes.Interface',
		defaults: joint.util.deepSupplement({
			type: 'uml.Interface',
			attrs: {
				'.uml-class-name-rect': { fill : '#f1c40f' },
				'.uml-class-attrs-rect': { fill : '#f39c12' },
				'.uml-class-methods-rect': { fill : '#f39c12' }
			}
		}, visualizer.shapes.Class.prototype.defaults),

		getClassName: function() {
			return ['<<Interface>>', this.get('name')];
		}
});

visualizer.shapes.AttributeAssociation = joint.shapes.uml.Association.extend({
	defaults: joint.util.deepSupplement({

        type: 'visualizer.shapes.AttributeAssociation'
	}, joint.shapes.uml.Association.prototype.defaults)
});

visualizer.shapes.State = joint.shapes.uml.State.extend({		
		defaults: joint.util.deepSupplement({
			type: 'visualizer.shapes.State',
			attrs: {
				'.uml-state-name': {
					'fill': '#000000', 'font-family': '"Lucida Console", Monaco, monospace', 'font-size': 14
				},
				'.uml-state-events': {
					'fill': '#000000', 'font-family': '"Lucida Console", Monaco, monospace', 'font-size': 14
				}
			}
		},joint.shapes.uml.State.prototype.defaults),
		initialize: function() {

			joint.shapes.uml.State.prototype.initialize.apply(this, arguments);
			this.autoSize();
		},
		autoSize: function() {
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
				rectHeight = lines.length * 20 + 20;
				

				_.each(lines, function(line){
					maxChars = Math.max(maxChars, line.length);
				})
				
				offsetY += rectHeight;
			});
			// autosizing TODO: less font-relied solution
			var minSize = {
				'width' : maxChars * 7.2 + 20,
				'height' : offsetY * 0.5 + 20
			}
			this.set('size',minSize);
		},
		strechToNewSize: function(){
			this.attr('rect',this.get('size'));
		}
});

visualizer.shapes.StartState = joint.shapes.uml.StartState.extend({
	markup: '<g class="rotatable"><g class="scalable"><circle class="uml-startstate-circle"/></g><rect class="uml-startstate-name-bg" /><text class="uml-startstate-name"/></g>',
    defaults: joint.util.deepSupplement({

        type: 'visualizer.shapes.StartState',
		attrs:{
			'text':{
                'ref': '.uml-startstate-circle', 'ref-x': .5, 'ref-y': .5, 'text-anchor': 'middle',
                'fill': '#000000', 'font-family': '"Lucida Console", Monaco, monospace', 'font-size': 12,
				'text' : ''
			},
			'.uml-startstate-name-bg':{
				'ref': 'text', 'ref-x':-1, 'ref-y':-1,
				'fill':'white',
				'width':10,
				'height':14
			},
		}
        //attrs: { 'circle': { 'fill': '#34495e', 'stroke': '#2c3e50', 'stroke-width': 2, 'rx': 1 }},
		
		

    }, joint.shapes.uml.StartState.prototype.defaults),
	initialize: function(){
		this.on({
			'change:text/text': this.updateName
		});
		//this.attr('text',{});
		joint.shapes.uml.StartState.prototype.initialize.apply(this, arguments);
		this.updateName();
	},
	updateName: function(){
		var str = this.attr('text/text');
		
		this.attr('.uml-startstate-name-bg/width', str.length * 7.2 + 2);
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
                'ref': '.uml-choice-body', 'ref-x': .5, 'ref-y': .5,  'text-anchor': 'middle',
                'fill': '#000000', 'font-family': '"Lucida Console", Monaco, monospace', 'font-size': 12,
				'text' : ''
			},
			'.uml-choice-name-bg':{
				'ref': 'text', 'ref-x':-1, 'ref-y':-1,
				'fill':'white',
				'width':1,
				'height':14
			},
		},
		/*size:{
			'width': 100,
			'height': 100
		}*/
        //attrs: { 'circle': { 'fill': '#34495e', 'stroke': '#2c3e50', 'stroke-width': 2, 'rx': 1 }},
		
		

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
		
		this.attr('.uml-choice-name-bg/width', str.length * 7.2 + 2);
		var size = {
			'width': str.length * 7.2 + 2,
			'height': str.length * 7.2 + 2
		}
		this.set('size',size);
	}

});
