// This class is responsible for translating the abstract grid to a pixel grid based on the abstract layout informations
// and actual sizes of the nodes. Also considers spacing and an overall padding
visualizer.Grid = function(nodes, links, padding, spacing){
	
	//if no nodes present taht means there are also no links so only the total size should be set
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
		
		//get the dimensions of the abstract grid based on the nodes
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
		
		//take account of the links' turning points positions
		_.each(links,function(link){
			var route = link.getRoute();
			_.each(route,function(position){
				this._left = Math.min(this._left,position.x - 1);
				this._top = Math.max(this._top,position.y + 1);
				this._right = Math.max(this._right,position.x + 1);
				this._bottom = Math.min(this._bottom,position.y - 1);			
			},this);
		},this);
		
		//finally incrase the abstract grid dimensions for potentional padding on the edge
		--this._left;
		++this._right;
		++this._top;
		--this._bottom;
		
		
		this._columns = {} //map for columns
		this._rows = {} //map for rows
		
		//generate map for columns with the abstract grid's coordinate as keys
		var i;
		for (i = this._left; i < this._right; ++i){
			this._columns[i] = {
				'x':padding,
				'width':this._GLOBAL_MIN_WIDTH //empty column width
			}
		}
		
		//generate map for rows with the abstract grid's coordinate as keys
		for (i = this._top; i > this._bottom; --i){
			this._rows[i] = {
				'y':padding,
				'height':this._GLOBAL_MIN_HEIGHT //empty row height
			}
		}
		
		//set turning points row and column size
		_.each(links, function(link){
			var route = link.getRoute();
			_.each(route,function( position){
				this._columns[position.x].width = this._TURNING_MIN_WIDTH;
				this._rows[position.y].height = this._TURNING_MIN_HEIGHT;
			},this);
		},this);
		
		//set column and row sizes based on the node's size and abstract layout informations
		_.each(nodes,function(node){
			
			var position = node.getGridPosition(); //abstract position
			var size = node.getGridSize(); //abstract size
			var pixelSize = node.getPixelSize(); //model estimated size
			
			//target pixel sizes for the rows / columns containing the node
			var widthRatio = Math.ceil(pixelSize.width / size.width);
			var heightRatio = Math.ceil(pixelSize.height / size.height);
			
			var i;
			
			//bottom-right corner
			var right = position.x + size.width; 
			var bottom = position.y - size.height;
			
			var changeIndexes = []; //indexes for rows/columns whose size might increased 
			var subsize = 0; //sum of the size of the rows/columns whose size is bigger then the target ratio
			var totalsize = 0; //sum of the size off all the rows/columns containing the node
			
			for (i = position.x; i < right; ++i ){
				if (this._columns[i].width < widthRatio){
					changeIndexes.push(i); 
				}else{
					subsize += this._columns[i].width;
				}
				totalsize += this._columns[i].width; 
			}
			
			//if the node can't fit
			if (totalsize < pixelSize.width){
				//calculate the corrected pixel size taking account of the already bigger nodes/columns
				var correctedSizeRatio = (pixelSize.width - subsize) / changeIndexes.length;
				_.each(changeIndexes, function(index){
					//Math.max is unnecessary corrected ratio can't be more than the original which is bigger then the
					//size of the ones whose index is in the changeIndexes
					this._columns[index].width = correctedSizeRatio;
				},this);
			}

			subsize = 0; //indexes for rows/columns whose size might increased 
			changeIndexes = []; //sum of the size of the rows/columns whose size is bigger then the target ratio
			totalsize = 0; //sum of the size off all the rows/columns containing the node
			
			for (i = position.y; i > bottom; --i ){
				
				if (this._rows[i].height < heightRatio){
					changeIndexes.push(i);
				}else{
					subsize += this._rows[i].height;
				}
				totalsize += this._rows[i].height;
			}
			
			//if the node can't fit
			if (totalsize < pixelSize.height){
				//calculate the corrected pixel size taking account of the already bigger nodes/columns
				var correctedSizeRatio = (pixelSize.height - subsize) / changeIndexes.length;
				_.each(changeIndexes, function(index){
					//Math.max is unnecessary corrected ratio can't be more than the original which is bigger then the
					//size of the ones whose index is in the changeIndexes
					this._rows[index].height = correctedSizeRatio;
				},this);
			}
			
			//increasing the rows / columns size neighbouring the node (scaled by spacing)
			this._rows[position.y + 1].height = Math.max(this._SPACING_HEIGHT * spacing, this._rows[position.y + 1].height);
			this._rows[bottom].height = Math.max(this._SPACING_HEIGHT * spacing, this._rows[bottom].height);
			this._columns[position.x - 1].width = Math.max(this._SPACING_WIDTH * spacing, this._columns[position.x - 1].width);
			this._columns[right].width = Math.max(this._SPACING_WIDTH * spacing, this._columns[right].width);
			
			
		},this);
		
		//setting offsets for columns for further use
		for (i = this._left; i < this._right; ++i){		
			if (i < this._right - 1){
				this._columns[i+1].x = this._columns[i].x + this._columns[i].width;
			}
		}
		
		//setting offsets for rows for further use
		for (i = this._top; i > this._bottom; --i){
			if (i > this._bottom + 1){
				this._rows[i-1].y = this._rows[i].y + this._rows[i].height;
			}
		}
		
		//setting total size 
		var lastCol = this._columns[this._right - 1];
		var lastRow = this._rows[this._bottom + 1];
		this._totalPixelWidth = lastCol.x + lastCol.width + padding * 2;
		this._totalPixelHeight = lastRow.y + lastRow.height + padding * 2;
	}
	
}

//base spacing
visualizer.Grid.prototype._SPACING_WIDTH = 50;
visualizer.Grid.prototype._SPACING_HEIGHT = 50;

//empty column's/row's width/height
visualizer.Grid.prototype._GLOBAL_MIN_WIDTH = 1;
visualizer.Grid.prototype._GLOBAL_MIN_HEIGHT = 1;

//turning point padding
visualizer.Grid.prototype._TURNING_MIN_WIDTH = 100;
visualizer.Grid.prototype._TURNING_MIN_HEIGHT = 100;

//returns the grid's total size
visualizer.Grid.prototype.getTotalPixelSize = function(){
	return {
		'width' : this._totalPixelWidth,
		'height' : this._totalPixelHeight
	};
}

//returns the position and size of a pixel rectangle based on the abstract position and size provided
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

//returns the series of pixel coordinates based on the series of abstract coordinates provided
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
