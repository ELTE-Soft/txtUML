visualizer.Grid = function(nodes, links, padding, spacing){
	var context = this;
	if (nodes.length === 0){
		this._left = 0;
		this._right = 0;
		this._top = 0;
		this._bottom = 0;
		this._padding = padding;
		this._totalPixelWidth = padding * 2;
		this._totalPixelHeight = padding * 2;
	}else{
		this._left = null;
		this._right = null;
		this._top = null;
		this._bottom = null;
		this._padding = padding;
		_.each(nodes,function(value){
			
			var position = value.getGridPosition();
			var size = value.getGridSize();
			if (this._left === null){
				this._left = position.x;
				this._top = position.y;
				this._right = position.x + size.width;	
				this._bottom = position.y - size.height;			
			}else{
				this._left = Math.min(this._left,position.x);
				this._top = Math.max(this._top,position.y);
				this._right = Math.max(this._right,position.x + size.width);
				this._bottom = Math.min(this._bottom,position.y - size.height);
			}
		},this);
		
		_.each(links,function(link){
			var route = link.getRoute();
			_.each(route,function(position){
				this._left = Math.min(this._left,position.x - 1);
				this._top = Math.max(this._top,position.y + 1);
				this._right = Math.max(this._right,position.x + 1);
				this._bottom = Math.min(this._bottom,position.y - 1);			
			},this);
		},this);
		
		--this._left;
		++this._right;
		++this._top;
		--this._bottom;
		this._columns = {}
		this._rows = {}
		
		
		var i;
		for (i = this._left; i < this._right; ++i){
			this._columns[i] = {
				'x':padding,
				'width':this._GLOBAL_MIN_WIDTH
			}
		}
		for (i = this._top; i > this._bottom; --i){
			this._rows[i] = {
				'y':padding,
				'height':this._GLOBAL_MIN_HEIGHT
			}
		}
		_.each(links, function(link){
			var route = link.getRoute();
			_.each(route,function( position){
				this._columns[position.x].width = this._TURNING_MIN_WIDTH;
				this._rows[position.y].height = this._TURNING_MIN_HEIGHT;
			},this);
		},this);
		_.each(nodes,function(node){
			var position = node.getGridPosition();
			var size = node.getGridSize();
			var pixelSize = node.getPixelSize();
			
			var widthRatio = Math.ceil(pixelSize.width / size.width);
			var heightRatio = Math.ceil(pixelSize.height / size.height);
			var i;
			var right = position.x + size.width;
			var bottom = position.y - size.height;
			
			var changeIndexes = [];
			var subsize = 0;
			var totalsize = 0;
			
			for (i = position.x; i < right; ++i ){
				if (this._columns[i].width < widthRatio){
					changeIndexes.push(i);
				}else{
					subsize += this._columns[i].width;
				}
				totalsize += this._columns[i].width;
			}
			
			if (totalsize < pixelSize.width){
				var correctedSizeRatio = (pixelSize.width - subsize) / changeIndexes.length;
				_.each(changeIndexes, function(index){
					this._columns[index].width = correctedSizeRatio;
				},this);
			}

			subsize = 0;
			changeIndexes = [];
			totalsize = 0;
			for (i = position.y; i > bottom; --i ){
				
				if (this._rows[i].height < heightRatio){
					changeIndexes.push(i);
				}else{
					subsize += this._rows[i].height;
				}
				totalsize += this._rows[i].height;
			}
			
			if (totalsize < pixelSize.height){
				var correctedSizeRatio = (pixelSize.height - subsize) / changeIndexes.length;
				_.each(changeIndexes, function(index){
					this._rows[index].height = correctedSizeRatio;
				},this);
			}
			
			this._rows[position.y + 1].height = Math.max(this._SPACING_HEIGHT * spacing, this._rows[position.y + 1].height);
			this._rows[bottom].height = Math.max(this._SPACING_HEIGHT * spacing, this._rows[bottom].height);
			this._columns[position.x - 1].width = Math.max(this._SPACING_WIDTH * spacing, this._columns[position.x - 1].width);
			this._columns[right].width = Math.max(this._SPACING_WIDTH * spacing, this._columns[right].width);
			
			
		},this);
		
		for (i = this._left; i < this._right; ++i){		
			if (i < this._right - 1){
				this._columns[i+1].x = this._columns[i].x + this._columns[i].width;
			}
		}
		
		for (i = this._top; i > this._bottom; --i){
			if (i > this._bottom + 1){
				this._rows[i-1].y = this._rows[i].y + this._rows[i].height;
			}
		}
		
		var lastCol = this._columns[this._right - 1];
		var lastRow = this._rows[this._bottom + 1];
		this._totalPixelWidth = lastCol.x + lastCol.width + padding * 2;
		this._totalPixelHeight = lastRow.y + lastRow.height + padding * 2;
	}
	
}

visualizer.Grid.prototype._SPACING_WIDTH = 50;
visualizer.Grid.prototype._SPACING_HEIGHT = 50;
visualizer.Grid.prototype._GLOBAL_MIN_WIDTH = 1;
visualizer.Grid.prototype._GLOBAL_MIN_HEIGHT = 1;
visualizer.Grid.prototype._TURNING_MIN_WIDTH = 100;
visualizer.Grid.prototype._TURNING_MIN_HEIGHT = 100;

visualizer.Grid.prototype.getTotalPixelSize = function(){
	return {
		'width' : this._totalPixelWidth,
		'height' : this._totalPixelHeight
	};
}

visualizer.Grid.prototype.getPixelBounds = function(position, size){
	var x = this._columns[position.x].x;
	var y = this._rows[position.y].y;
	var bounds = {
		'position' : {
			'x' : x,
			'y' : y
		},
		'size' : {
			'width' : (this._columns[position.x + size.width - 1].x + this._columns[position.x + size.width - 1].width) - x,
			'height' : (this._rows[position.y - size.height + 1].y + this._rows[position.y - size.height + 1].height) - y
		}
	};
	return bounds;

}

visualizer.Grid.prototype.translateRoute = function(gridRoute){
	var pixelRoute = [];
	_.each(gridRoute, function(point){
		pixelRoute.push({
			'x': Math.ceil(this._columns[point.x].x + this._columns[point.x].width / 2),
			'y': Math.ceil(this._rows[point.y].y + this._rows[point.y].height / 2)
		});
	},this);
	return pixelRoute;
}
