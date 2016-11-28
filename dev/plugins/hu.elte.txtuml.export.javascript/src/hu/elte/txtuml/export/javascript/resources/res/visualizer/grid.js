visualizer.Grid = function(nodes, links, padding){
	var clazz = this;
	if (nodes.length === 0){
		clazz._left = 0;
		clazz._right = 0;
		clazz._top = 0;
		clazz._bottom = 0;
		clazz._padding = padding;
		clazz._totalPixelWidth = padding * 2;
		clazz._totalPixelHeight = padding * 2;
	}else{
		var clazz = this;
		clazz._left = null;
		clazz._right = null;
		clazz._top = null;
		clazz._bottom = null;
		clazz._padding = padding;
		
		clazz._colWidth = clazz._GLOBAL_MIN_WIDTH;
		clazz._rowHeight = clazz._GLOBAL_MIN_HEIGHT;
		$.each(nodes,function(key,value){
			
			var position = value.getGridPosition();
			var size = value.getGridSize();
			if (clazz._left === null){
				clazz._left = position.x;
				clazz._top = position.y;
				clazz._right = position.x + size.width;	
				clazz._bottom = position.y - size.height;			
			}else{
				clazz._left = Math.min(clazz._left,position.x);
				clazz._top = Math.max(clazz._top,position.y);
				clazz._right = Math.max(clazz._right,position.x + size.width);
				clazz._bottom = Math.min(clazz._bottom,position.y - size.height);
			}
		});
		
		$.each(links,function(key,link){
			var route = link.getRoute();
			$.each(route,function(key, position){
				clazz._left = Math.min(clazz._left,position.x - 1);
				clazz._top = Math.max(clazz._top,position.y + 1);
				clazz._right = Math.max(clazz._right,position.x + 1);
				clazz._bottom = Math.min(clazz._bottom,position.y - 1);			
			});
		});
		// Uniform
		/*var i;
		$.each(nodes,function(key,node){
			var size = node.getGridSize();
			var pixelSize = node.getPixelSize();
			
			var widthRatio = Math.ceil(pixelSize.width / size.width);
			var heightRatio = Math.ceil(pixelSize.height / size.height);
			
			clazz._colWidth = Math.max(widthRatio, clazz._colWidth);
			clazz._rowHeight = Math.max(heightRatio, clazz._rowHeight);
		});
		
		clazz._totalPixelWidth = clazz._padding * 2 + clazz._colWidth * (clazz._right - clazz._left);
		clazz._totalPixelHeight = clazz._padding * 2 + clazz._rowHeight * (clazz._top - clazz._bottom);*/
		clazz._columns = {}
		clazz._rows = {}
		
		
		var i;
		for (i = clazz._left; i < clazz._right; ++i){
			clazz._columns[i] = {
				'x':padding,
				'width':clazz._GLOBAL_MIN_WIDTH
			}
		}
		for (i = clazz._top; i > clazz._bottom; --i){
			clazz._rows[i] = {
				'y':padding,
				'height':clazz._GLOBAL_MIN_HEIGHT
			}
		}
		$.each(links, function(key, link){
			var route = link.getRoute();
			$.each(route,function(poskey, position){
				clazz._columns[position.x].width = clazz._TURNING_MIN_WIDTH;
				clazz._rows[position.y].height = clazz._TURNING_MIN_HEIGHT;
			});
		});
		$.each(nodes,function(key,node){
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
				if (clazz._columns[i].width < widthRatio){
					changeIndexes.push(i);
				}else{
					subsize += clazz._columns[i].width;
				}
				totalsize += clazz._columns[i].width;
			}
			
			if (totalsize < pixelSize.width){
				var correctedSizeRatio = (pixelSize.width - subsize) / changeIndexes.length;
				$.each(changeIndexes, function(key,index){
					clazz._columns[index].width = correctedSizeRatio;
				});
			}

			subsize = 0;
			changeIndexes = [];
			totalsize = 0;
			for (i = position.y; i > bottom; --i ){
				
				if (clazz._rows[i].height < heightRatio){
					changeIndexes.push(i);
				}else{
					subsize += clazz._rows[i].height;
				}
				totalsize += clazz._rows[i].height;
			}
			
			if (totalsize < pixelSize.height){
				var correctedSizeRatio = (pixelSize.height - subsize) / changeIndexes.length;
				$.each(changeIndexes, function(key,index){
					clazz._rows[index].height = correctedSizeRatio;
				});
			}
			
			
		});
		
		for (i = clazz._left; i < clazz._right; ++i){		
			if (i < clazz._right - 1){
				clazz._columns[i+1].x = clazz._columns[i].x + clazz._columns[i].width;
			}
		}
		
		for (i = clazz._top; i > clazz._bottom; --i){
			if (i > clazz._bottom + 1){
				clazz._rows[i-1].y = clazz._rows[i].y + clazz._rows[i].height;
			}
		}
		
		var lastCol = clazz._columns[clazz._right - 1];
		var lastRow = clazz._rows[clazz._bottom + 1];
		clazz._totalPixelWidth = lastCol.x + lastCol.width + padding * 2;
		clazz._totalPixelHeight = lastRow.y + lastRow.height + padding * 2;
	}
	
}

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
	var clazz = this;
	var pixelRoute = [];
	$.each(gridRoute, function(key, point){
		pixelRoute.push({
			'x': Math.ceil(clazz._columns[point.x].x + clazz._columns[point.x].width / 2),
			'y': Math.ceil(clazz._rows[point.y].y + clazz._rows[point.y].height / 2)
		});
	});
	return pixelRoute;
}
