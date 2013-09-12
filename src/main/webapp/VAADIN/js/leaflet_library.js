//Define the namespace
var leafletLibrary = leafletLibrary || {};

leafletLibrary.LeafletMap = function(element) {
	this.element = element;
	this.element.innerHTML = "<div></div>";
	this.speciesId = null;

	var map = L.map(element, {maxZoom: 14}).setView([ 0, 0 ], 2);

	L.tileLayer(
				'http://server.arcgisonline.com/ArcGIS/rest/services/World_Imagery/MapServer/tile/{z}/{y}/{x}.png',
				{
					attribution : 'Map data &copy; <a href="http://openstreetmap.org">OpenStreetMap</a> contributors, <a href="http://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, Imagery © <a href="http://cloudmade.com">CloudMade</a>'
//					maxZoom : 18
				}).addTo(map);

	this.speciesLayer = L.tileLayer.wms(
			"http://lrm-maps.jrc.ec.europa.eu/geoserver/ibis/wms", {
				layers : 'ibis:species',
				format : 'image/png',
				transparent : true,
				attribution : "Weather data © 2012 IEM Nexrad"

			}).addTo(map);
	
	this.islandLayer = L.tileLayer.wms(
			"http://lrm-maps.jrc.ec.europa.eu/geoserver/ibis/wms", {
				layers : 'ibis:gid',
				format : 'image/png',
				transparent : true,
				attribution : "GID"

			}).addTo(map);


	// Default implementation of the click handler
	this.click = function() {
		// alert("Error: Must implement click() method");
	};

	this.fitBounds = function(boundsString) {

		if (boundsString != null) {
			var bounds = JSON.parse(boundsString);
			map.fitBounds(bounds);
		}

	};

	/*
	 * Show the species range
	 */
	this.setSpecies = function(id) {
		if (id != this.speciesId) {
			this.speciesId = id;
			this.speciesLayer.setParams({
				viewparams : 'binomial_id:' + this.speciesId
			}, true);
		}
	};

	// Set up button click
	var component = this; // Can't use this inside the function

	map.onclick = function() {
		component.click();
	};
};
