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
       if (!this._V.labels) return this;

        // This method assumes all the label nodes are stored in the `this._labelCache` hash table
        // by their indexes in the `this.get('labels')` array. This is done in the `renderLabels()` method.

        var labels = this.model.get('labels') || [];
        if (!labels.length) return this;

        var connectionElement = this._V.connection.node;
        var connectionLength = connectionElement.getTotalLength();

        // Firefox returns connectionLength=NaN in odd cases (for bezier curves).
        // In that case we won't update labels at all.
        if (!_.isNaN(connectionLength)) {

            var samples;

            _.each(labels, function(label, idx) {

                var position = label.position;
                var distance = _.isObject(position) ? position.distance : position;
                var offset = _.isObject(position) ? position.offset : { x: 0, y: 0 };

                distance = (distance > connectionLength) ? connectionLength : distance; // sanity check
                distance = (distance < 0) ? connectionLength + distance : distance;
                distance = (distance > 1) ? distance : connectionLength * distance;

                var labelCoordinates = connectionElement.getPointAtLength(distance);

                if (_.isObject(offset)) {

                    // Just offset the label by the x,y provided in the offset object.
                    labelCoordinates = g.point(labelCoordinates).offset(offset.x, offset.y);

                } else if (_.isNumber(offset)) {

                    if (!samples) {
                        samples = this._samples || this._V.connection.sample(this.options.sampleInterval);
                    }

                    // Offset the label by the amount provided in `offset` to an either
                    // side of the link.

                    // 1. Find the closest sample & its left and right neighbours.
                    var minSqDistance = Infinity;
                    var closestSample;
                    var closestSampleIndex;
                    var p;
                    var sqDistance;
                    for (var i = 0, len = samples.length; i < len; i++) {
                        p = samples[i];
                        sqDistance = g.line(p, labelCoordinates).squaredLength();
                        if (sqDistance < minSqDistance) {
                            minSqDistance = sqDistance;
                            closestSample = p;
                            closestSampleIndex = i;
                        }
                    }
                    var prevSample = samples[closestSampleIndex - 1];
                    var nextSample = samples[closestSampleIndex + 1];

                    // 2. Offset the label on the perpendicular line between
                    // the current label coordinate ("at `distance`") and
                    // the next sample.
                    var angle = 0;
                    if (nextSample) {
                        angle = g.point(labelCoordinates).theta(nextSample);
                    } else if (prevSample) {
                        angle = g.point(prevSample).theta(labelCoordinates);
                    }
                    labelCoordinates = g.point(labelCoordinates).offset(offset).rotate(labelCoordinates, angle - 90);
                }

                this._labelCache[idx].attr('transform', 'translate(' + labelCoordinates.x + ', ' + labelCoordinates.y + ')');

            }, this);
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
			// TODO:  better but still cross-browser angle determination
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
		//console.log('target' + (bbox.x + bbox.width + labelbox.width / 2));
		//this._labelCache[0].attr('transform', 'translate(' + (bbox.x + bbox.width + labelbox.width / 2) + ', ' + bbox.y + ')');

        return this;
    }

});