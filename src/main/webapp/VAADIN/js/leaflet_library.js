//Define the namespace
var leafletLibrary = leafletLibrary || {};

leafletLibrary.LeafletMap = function(element) {
	this.element = element;
	this.element.innerHTML = "<div></div>";

	var map = L.map(element).setView([ 0, 0 ], 2);

	L.tileLayer(
					'http://{s}.tile.osm.org/{z}/{x}/{y}.png',
					{
						attribution : 'Map data &copy; <a href="http://openstreetmap.org">OpenStreetMap</a> contributors, <a href="http://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, Imagery © <a href="http://cloudmade.com">CloudMade</a>',
						maxZoom : 18
					}).addTo(map);

	// Default implementation of the click handler
	this.click = function() {
		// alert("Error: Must implement click() method");
	};

	this.fitBounds = function(boundsString) {
		
//		window.alert(boundsString);
		
		if (boundsString != null) {
			var bounds = JSON.parse(boundsString);
			map.fitBounds(bounds);
		}

//		new L.Marker(bounds[0]).addTo(map);

	};

	// Set up button click
	var component = this; // Can't use this inside the function
	
	map.onclick = function() {
		component.click();
	};
};
