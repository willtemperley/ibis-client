//Define the namespace
var openLayersLibrary = openLayersLibrary || {};

openLayersLibrary.OpenLayersMap = function(element) {
	this.element = element;
	this.element.innerHTML = "<div></div>";
	
    map = new OpenLayers.Map({
        div: element,
        allOverlays: true
    });

    var osm = new OpenLayers.Layer.OSM();
//    var gmap = new OpenLayers.Layer.Google("Google Streets", {visibility: false});

    // note that first layer must be visible
    map.addLayers([osm]);

    map.addControl(new OpenLayers.Control.LayerSwitcher());
    map.zoomToMaxExtent();

};
