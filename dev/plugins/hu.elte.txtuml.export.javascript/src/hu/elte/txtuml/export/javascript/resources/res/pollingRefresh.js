//include after animation.js
var isPolling = false;
var alertDisplayed = false;
var port = "";

/**
 * Queries the server for diagnostics information.
 * If successful, updates the diagram with `setActiveElements` from `animation.js`
 * If not, clears active elements, and alerts the user.
 */
function refreshElements(){
	$.ajax({
		url: 'http://localhost:' + port + '/' + DIAGNOSTICS_PATH,
	    type: 'GET',
	    dataType: 'json'
	}).complete(function(response){
        if(response.status == 200 && isPolling){
            setActiveElements((JSON.parse(response.responseText)).map(e => _visualizer.getShapeIdByElementName(e.element)));
            hideError();
        }
        else{
            setActiveElements([]);
            if(isPolling){
            	showError();
            }else{
            	hideError();
            }
        }
	});
}

function pollingRefresh(){
    refreshElements();
    if(isPolling){
    	setTimeout(pollingRefresh, REFRESH_INTERVAL_IN_MILISECONDS);
    }
}

//add event listener to the checkbox
$("#toggle-debug-checkbox").on("change", function(){
	if(!this.checked){
		//on
		isPolling = true;
		pollingRefresh();
	}
	else{
		//off
		isPolling = false;
		hideError();
		clearCurrentActiveElements();
	}
});

var errorLabelElement = $('#debug-port-error');
var debugContainer = $('#debug-toggle-container');

function showError(){
	errorLabelElement.innerHeight('8em');
	debugContainer.innerHeight('18em');
}

function hideError(){
	errorLabelElement.height('0');
	debugContainer.innerHeight('10.5em');
}

var typingTimer;
var DONE_TYPING_TIMEOUT_IN_MILISECONDS = 500;

function doneTyping(){
	port = $('#debug-port-input').val();
}

//add event listeners to the debug input
$('#debug-port-input').on('keyup change input',function(){
    clearTimeout(typingTimer);
    if ($('#debug-port-input').val()) {
        typingTimer = setTimeout(doneTyping, DONE_TYPING_TIMEOUT_IN_MILISECONDS);
    }
});

