visualizer.shapes.AttributeAssociationView = joint.dia.LinkView.extend({
	markup: [
        '<path class="connection" stroke="black" d="M 0 0 0 0"/>',
        '<path class="marker-source" fill="black" stroke="black" d="M 0 0 0 0"/>',
        '<path class="marker-target" fill="black" stroke="black" d="M 0 0 0 0"/>',
        '<path class="connection-wrap" d="M 0 0 0 0"/>',
        '<g class="labels"/>',
        '<g class="marker-vertices"/>',
        '<g class="marker-arrowheads"/>',
        '<g class="link-tools"/>'
    ].join(''),

    labelMarkup: [
        '<g class="label">',
        '<rect />',
        '<text />',
        '</g>'
    ].join(''),
	update: function(model, attributes, opt) {

        opt = opt || {};

        if (!opt.updateConnectionOnly) {
            // update SVG attributes defined by 'attrs/'.
            this.updateAttributes();
        }

        // update the link path, label position etc.
        this.updateConnection(opt);
        this.updateToolsPosition();
		//this.updateLabelPositions();
        this.updateArrowheadMarkers();
		this.updateLabelPositions(); // needs to be called after arrowhead positioning

        // Local perpendicular flag (as opposed to one defined on paper).
        // Could be enabled inside a connector/router. It's valid only
        // during the update execution.
        this.options.perpendicular = null;
        // Mark that postponed update has been already executed.
        this.updatePostponed = false;

        return this;
    },
	updateLabelPositions: function() {
		joint.dia.LinkView.prototype.updateLabelPositions.call(this);
		var snap = this.model.attr('snapLabels');
		if (typeof snap !== 'undefined' && snap === true){
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
				else{
					console.log('Non diagonal link');
				}
			
			}, this);
			//console.log('target' + (bbox.x + bbox.width + labelbox.width / 2));
			//this._labelCache[0].attr('transform', 'translate(' + (bbox.x + bbox.width + labelbox.width / 2) + ', ' + bbox.y + ')');
		}
        return this;
    }

});