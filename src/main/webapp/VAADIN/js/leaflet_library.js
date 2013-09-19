//Define the namespace
var leafletLibrary = leafletLibrary || {};

leafletLibrary.LeafletMap = function(element) {
	this.element = element;
	this.element.innerHTML = "<div></div>";
	this.speciesId = null;

	var map = L.map(element, {
		maxZoom : 14
	}).setView([ 0, 0 ], 2);

	this.baseLayer = L.tileLayer(
					'http://server.arcgisonline.com/ArcGIS/rest/services/World_Imagery/MapServer/tile/{z}/{y}/{x}.png',
					{
						attribution : 'Map data &copy; <a href="http://openstreetmap.org">OpenStreetMap</a> contributors, <a href="http://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, Imagery © <a href="http://cloudmade.com">CloudMade</a>'
					}).addTo(map);

	this.islandLayer = L.geoJson().addTo(map);

	// Default implementation of the click handler
	this.click = function() {
		 alert("Error: Must implement click() method");
	};

	this.fitBounds = function(boundsString) {

		if (boundsString != null) {
			var bounds = JSON.parse(boundsString);
			map.fitBounds(bounds);
		}
		
	};
	
	this.setSpecies = function(geoJsonString) {
	};

	/*
	 * Show the locations
	 */
	this.setLocations = function(geoJsonString) {
		
		if (typeof geoJsonString === 'undefined') {
			return;
		}
		
		var geoJson = JSON.parse(geoJsonString);
		this.islandLayer.addData(geoJson);

	};

	// Set up button click
	var component = this; // Can't use this inside the function

	map.onclick = function() {
		component.click();
	};
};
