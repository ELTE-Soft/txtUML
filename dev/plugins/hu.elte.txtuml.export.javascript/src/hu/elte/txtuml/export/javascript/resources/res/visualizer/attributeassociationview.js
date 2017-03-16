// A specialized linkview with the ability to snap name and multiplicity labels
visualizer.shapes.AttributeAssociationView = joint.dia.LinkView.extend({
		update: function (model, attributes, opt) {
			joint.dia.LinkView.prototype.update.call(this, model, attributes, opt)
			//after markers arranged labels can be snapped
			this.snapLabels();
		},

		// snaps the name and multiplicity labels to their expected locations (runs after every change of the link)
		snapLabels: function () {
			var snap = this.model.attr('snapLabels');
			var origin = this.paper.options.origin;
			// check if should snap
			if (typeof snap !== 'undefined' && snap === true) {

				//check if labels are present (used correctly)
				if (this._labelCache.length < 4) {
					throw new Error('Useage of snapLabel flag requires at least 4 labels to present.');
				}
				//label positions are based on arrowheads
				var arrowheads = [this._V.sourceArrowhead, this._V.targetArrowhead];
				_.each(arrowheads, function (arrowhead, idx) {
					var angle = 0;

					//determining arrowheads rotation
					var transformationString = $(arrowhead.node).attr('transform');
					var rotateNeedle = transformationString.indexOf('rotate');
					if (rotateNeedle > -1) {
						var rotateString = transformationString.slice(rotateNeedle);
						angle = Number(rotateString.split(/ *\( *| *\)/)[1]);
					}

					//arrowhead dimensions
					var originalbbox = V(arrowhead.node).bbox();
					var bbox = {
						width: originalbbox.width,
						height: originalbbox.height,
						x: originalbbox.x - origin.x,
						y: originalbbox.y - origin.y
					}

					//setting name label target size
					var labelbox1 = this._labelCache[idx * 2].bbox();
					labelbox1.width += 10;
					labelbox1.height += 5;

					//setting multiplicity label target size
					var labelbox2 = this._labelCache[idx * 2 + 1].bbox();
					labelbox2.width += 10;
					labelbox2.height += 5;

					//translating labels based on arrowheads rotation and dimensions
					if (angle === -90) {
						this._labelCache[idx * 2].attr('transform', 'translate(' + (bbox.x - labelbox1.width / 2) + ', ' + (bbox.y + bbox.height - labelbox1.height / 2) + ')');
						this._labelCache[idx * 2 + 1].attr('transform', 'translate(' + (bbox.x + bbox.width + labelbox2.width / 2) + ', ' + (bbox.y + bbox.height - labelbox2.height / 2) + ')');
					} else if (angle === -180) {
						this._labelCache[idx * 2].attr('transform', 'translate(' + (bbox.x + bbox.width - labelbox1.width / 2) + ', ' + (bbox.y + bbox.height + labelbox1.height / 2) + ')');
						this._labelCache[idx * 2 + 1].attr('transform', 'translate(' + (bbox.x + bbox.width - labelbox2.width / 2) + ', ' + (bbox.y - labelbox2.height / 2) + ')');
					} else if (angle === -270 || angle === 90) {
						this._labelCache[idx * 2].attr('transform', 'translate(' + (bbox.x + bbox.width + labelbox1.width / 2) + ', ' + (bbox.y + labelbox1.height / 2 + 3) + ')');
						this._labelCache[idx * 2 + 1].attr('transform', 'translate(' + (bbox.x - labelbox2.width / 2) + ', ' + (bbox.y + labelbox2.height / 2 + 3) + ')');
					} else if (angle === 0) {
						this._labelCache[idx * 2].attr('transform', 'translate(' + (bbox.x + labelbox1.width / 2) + ', ' + (bbox.y - labelbox1.height / 2) + ')');
						this._labelCache[idx * 2 + 1].attr('transform', 'translate(' + (bbox.x + labelbox2.width / 2) + ', ' + (bbox.y + bbox.height + labelbox2.height / 2) + ')');
					}

				}, this);
			}
			return this;
		}

	});
