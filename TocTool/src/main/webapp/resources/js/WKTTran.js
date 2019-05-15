/**
 * wkt string to arcgis Point object
 * 
 * @param wkt
 * @returns {Point}
 * @constructor
 */
function WktToPoint(wkt, spatialreference) {
	var wktUtil = new WKTUtil();
	var pt = wktUtil.read(wkt);
	var json = {
		x : pt[0],
		y : pt[1],
		spatialReference : spatialreference
	};
	var point = new esri.geometry.Point(json);
	return point;
}

/**
 * wkt string to arcgis Multipoint object
 * 
 * @param wkt
 * @returns {Multipoint}
 * @constructor
 */
function WktToMultipoint(wkt, spatialreference) {
	var wktUtil = new WKTUtil();
	var pt = wktUtil.read(wkt);
	var mpJson ={
		"points": pt,
		"spatialReference": spatialreference
	};
	var point = new esri.geometry.Multipoint(mpJson);
	return point;
}

/**
 * wkt string to arcgis Polyline object
 * 
 * @param wkt
 * @returns {Polyline}
 * @constructor
 */
function WktToPolyline(wkt, spatialreference) {
	var wktUtil = new WKTUtil();
	var points = wktUtil.read(wkt);
	var json = {
		paths : [ points ],
		spatialReference : spatialreference
	};
	var polyline = new esri.geometry.Polyline(json);
	return polyline;
}

/**
 * wkt string to arcgis Polyline object
 * 
 * @param wkt
 * @returns {MultiPolyline}
 * @constructor
 */
function WktToMultiPolyline(wkt, spatialreference) {
	var wktUtil = new WKTUtil();
	var pt = wktUtil.read(wkt);
	var array = new Array();
	for (var i = 0; i < pt.length; i++) {
		var jsonObject = pt[i];
		var json = {
			paths : jsonObject,
			spatialReference : spatialreference
		};
		var Polyline = new esri.geometry.Polyline(json);
		array.push(Polyline);
	}
	return array;
}

/**
 * wkt string to arcgis Polygon object
 * 
 * @param wkt
 * @returns {Polygon}
 * @constructor
 */
function WktToPolygon(wkt, spatialreference) {
	var wktUtil = new WKTUtil();
	var points = wktUtil.read(wkt);
	var json = {
		rings : points,
		spatialReference : spatialreference
	};
	var polygon = new esri.geometry.Polygon(json);
	return polygon;
}

/**
 * 
 * @param wkt
 * @returns {MultiPolygon}
 * @constructor
 */
function WktToMultiPolygon(wkt, spatialreference) {
	var wktUtil = new WKTUtil();
	var pt = wktUtil.read(wkt);
	var json = {
		rings : pt,
		spatialReference : spatialreference
	};
	var Polygon = new esri.geometry.Polygon(json);
	return Polygon;
}

/**
 * @param geometry
 */
function PointToWKT(geometry) {
	return "POINT (" + geometry.x + " " + geometry.y + ")";
}
/**
 * @param geometry
 */
function PolygonToWKT(geometry) {
	var wkt = [];
	var rings = geometry.rings;
	for ( var i in rings) {
		var ring = rings[i];
		for ( var j in ring) {
			var p = ring[j];
			wkt.push(p.join(" "));
		}
	}
	return "POLYGON ((" + wkt.join(",") + "))";
}

/**
 * @param geometry
 */
function LineToWKT(geometry) {
	var wkt = [];
	var paths = geometry.paths;
	for ( var i in paths) {
		var path = paths[i];
		for ( var j in path) {
			var p = path[j];
			wkt.push(p.join(" "));
		}
	}
	return "LINESTRING (" + wkt.join(",") + ")";
}


