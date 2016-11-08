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
			});
			// autosizing TODO: less font-relied solution
			this.get("size").height = offsetY * 0.5 + 20; 
			this.get("size").width = maxChars * 7.2 + 12;
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

