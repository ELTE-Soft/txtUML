visualizer.shapes.AttributeAssociationView = joint.dia.LinkView.extend({
	update: function(model, attributes, opt) {
		joint.dia.LinkView.prototype.update.call(this, model, attributes, opt)
		this.snapLabels();
    },
	
	snapLabels: function() {
		var snap = this.model.attr('snapLabels');
		if (typeof snap !== 'undefined' && snap === true){
			if  (this._labelCache.length < 5){
				throw new Error('Useage of snapLabel flag requires at least 5 labels to present.');
			}
			var arrowheads = [this._V.sourceArrowhead, this._V.targetArrowhead];
			_.each(arrowheads, function(arrowhead, idx){
				var angle = 0;
				var transformationString = $(arrowhead.node).attr('transform');
				var rotateNeedle = transformationString.indexOf('rotate');
				if (rotateNeedle > -1){
					var rotateString = transformationString.slice(rotateNeedle);
					angle = Number(rotateString.split(/ *\( *| *\)/)[1]);
				}
				var bbox = V(arrowhead.node).bbox();

				var labelbox1 = this._labelCache[idx * 2].bbox();
				labelbox1.width += 10;
				labelbox1.height += 5;
				var labelbox2 = this._labelCache[idx * 2 + 1].bbox();
				labelbox2.width += 10;
				labelbox2.height += 5;
				if (angle === -90){
					this._labelCache[idx * 2].attr('transform', 'translate(' + (bbox.x - labelbox1.width / 2) + ', ' + (bbox.y + bbox.height - labelbox1.height / 2) + ')');
					this._labelCache[idx * 2 + 1].attr('transform', 'translate(' + (bbox.x + bbox.width + labelbox2.width / 2) + ', ' + (bbox.y + bbox.height - labelbox2.height / 2) + ')');
				}else if (angle === -180){
					this._labelCache[idx * 2].attr('transform', 'translate(' + (bbox.x + bbox.width - labelbox1.width / 2) + ', ' + (bbox.y + bbox.height + labelbox1.height / 2) + ')');
					this._labelCache[idx * 2 + 1].attr('transform', 'translate(' + (bbox.x + bbox.width - labelbox2.width / 2) + ', ' + (bbox.y - labelbox2.height / 2) + ')');
				}else if (angle === -270 || angle === 90){
					this._labelCache[idx * 2].attr('transform', 'translate(' + (bbox.x + bbox.width + labelbox1.width / 2) + ', ' + (bbox.y + labelbox1.height / 2 + 3) + ')');
					this._labelCache[idx * 2 + 1].attr('transform', 'translate(' + (bbox.x - labelbox2.width / 2) + ', ' + (bbox.y + labelbox2.height / 2 + 3) + ')');
				}else if (angle === 0){
					this._labelCache[idx * 2].attr('transform', 'translate(' + (bbox.x + labelbox1.width / 2) + ', ' + (bbox.y - labelbox1.height / 2) + ')');
					this._labelCache[idx * 2 + 1].attr('transform', 'translate(' + (bbox.x + labelbox2.width / 2) + ', ' + (bbox.y + bbox.height + labelbox2.height / 2) + ')');
				}
			
			}, this);
		}
        return this;
    }

});