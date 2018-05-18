//include after animation.js
var isPolling = false;
var alertDisplayed = false;

const ALERT_STRING = "Could not connect to server. Please make sure an execution is in progress.";

/**
 * Queries the server for diagnostics information.
 * If successful, updates the diagram with `setActiveElements` from `animation.js`
 * If not, clears active elements, and alerts the user.
 */
function refreshElements(){
	$.ajax({
		url: 'http://localhost:' + DIAGNOSTICS_PORT + '/' + DIAGNOSTICS_PATH,
	    type: 'GET',
	    dataType: 'json'
	}).complete(function(response){
        if(response.status == 200 && isPolling){
            setActiveElements((JSON.parse(response.responseText)).map( e => e.element));
        }
        else{
            setActiveElements([]);
            if(!alertDisplayed && isPolling){
            	alert(ALERT_STRING);
            	alertDisplayed = true;
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
		alertDisplayed = false;
		clearCurrentActiveElements();
	}
});