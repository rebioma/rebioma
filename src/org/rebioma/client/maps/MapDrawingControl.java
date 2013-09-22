package org.rebioma.client.maps;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.maps.client.LoadApi;
import com.google.gwt.maps.client.LoadApi.LoadLibrary;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.base.LatLng;
import com.google.gwt.maps.client.controls.ControlPosition;
import com.google.gwt.maps.client.drawinglib.DrawingControlOptions;
import com.google.gwt.maps.client.drawinglib.DrawingManager;
import com.google.gwt.maps.client.drawinglib.DrawingManagerOptions;
import com.google.gwt.maps.client.drawinglib.OverlayType;
import com.google.gwt.maps.client.events.overlaycomplete.OverlayCompleteMapEvent;
import com.google.gwt.maps.client.events.overlaycomplete.OverlayCompleteMapHandler;
import com.google.gwt.maps.client.events.overlaycomplete.circle.CircleCompleteMapEvent;
import com.google.gwt.maps.client.events.overlaycomplete.circle.CircleCompleteMapHandler;
import com.google.gwt.maps.client.events.overlaycomplete.marker.MarkerCompleteMapEvent;
import com.google.gwt.maps.client.events.overlaycomplete.marker.MarkerCompleteMapHandler;
import com.google.gwt.maps.client.events.overlaycomplete.polygon.PolygonCompleteMapEvent;
import com.google.gwt.maps.client.events.overlaycomplete.polygon.PolygonCompleteMapHandler;
import com.google.gwt.maps.client.events.overlaycomplete.polyline.PolylineCompleteMapEvent;
import com.google.gwt.maps.client.events.overlaycomplete.polyline.PolylineCompleteMapHandler;
import com.google.gwt.maps.client.events.overlaycomplete.rectangle.RectangleCompleteMapEvent;
import com.google.gwt.maps.client.events.overlaycomplete.rectangle.RectangleCompleteMapHandler;
import com.google.gwt.maps.client.mvc.MVCArray;
import com.google.gwt.maps.client.overlays.Circle;
import com.google.gwt.maps.client.overlays.CircleOptions;
import com.google.gwt.maps.client.overlays.Marker;
import com.google.gwt.maps.client.overlays.Polygon;
import com.google.gwt.maps.client.overlays.PolygonOptions;
import com.google.gwt.maps.client.overlays.Polyline;
import com.google.gwt.maps.client.overlays.Rectangle;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.sencha.gxt.widget.core.client.box.MessageBox;

public class MapDrawingControl extends Composite{
	
	
	public MapDrawingControl(MapWidget map, ControlPosition position) {
		//ArrayList<LoadLibrary> loadLibraries = new ArrayList<LoadApi.LoadLibrary>();
	    draw(map, position);
	  }

	private void draw(MapWidget map, ControlPosition position){
		//http://code.google.com/p/gwt-maps-api/source/browse/trunk/Apis_Maps_Test/src/com/gonevertical/maps/testing/client/maps/DrawingMapWidget.java
		DrawingControlOptions drawingControlOptions = DrawingControlOptions.newInstance();
	    drawingControlOptions.setPosition(position);
	    //drawingControlOptions.setDrawingModes(OverlayType.values());
	    drawingControlOptions.setDrawingModes(OverlayType.POLYGON);
//	    CircleOptions circleOptions = CircleOptions.newInstance();
	    //circleOptions.setFillColor("FF6633");

	    DrawingManagerOptions options = DrawingManagerOptions.newInstance();
	    options.setMap(map);
	    /*options.setDrawingMode(OverlayType.CIRCLE);
	    options.setCircleOptions(circleOptions);*/

	    options.setDrawingControlOptions(drawingControlOptions);

	    DrawingManager o = DrawingManager.newInstance(options);

//	    o.addCircleCompleteHandler(new CircleCompleteMapHandler() {
//	      public void onEvent(CircleCompleteMapEvent event) {
//	        Circle circle = event.getCircle();
//	        GWT.log("circle completed radius=" + circle.getRadius());
//	      }
//	    });
//
//	    o.addMarkerCompleteHandler(new MarkerCompleteMapHandler() {
//	      public void onEvent(MarkerCompleteMapEvent event) {
//	        Marker marker = event.getMarker();
//	        GWT.log("marker completed position=" + marker.getPosition());
//	      }
//	    });
//
//	    o.addOverlayCompleteHandler(new OverlayCompleteMapHandler() {
//	      public void onEvent(OverlayCompleteMapEvent event) {
//	        OverlayType ot = event.getOverlayType();
//	        GWT.log("marker completed OverlayType=" + ot.toString());
//
//	        if (ot == OverlayType.CIRCLE) {
//	          Circle circle = event.getCircle();
//	          GWT.log("radius=" + circle.getRadius());
//	        }
//
//	        if (ot == OverlayType.MARKER) {
//	          Marker marker = event.getMarker();
//	          GWT.log("position=" + marker.getPosition());
//	        }
//
//	        if (ot == OverlayType.POLYGON) {
//	          Polygon polygon = event.getPolygon();
//	          GWT.log("paths=" + polygon.getPaths().toString());
//	        }
//
//	        if (ot == OverlayType.POLYLINE) {
//	          Polyline polyline = event.getPolyline();
//	          GWT.log("paths=" + polyline.getPath().toString());
//	        }
//
//	        if (ot == OverlayType.RECTANGLE) {
//	          Rectangle rectangle = event.getRectangle();
//	          GWT.log("bounds=" + rectangle.getBounds());
//	        }
//	        GWT.log("marker completed OverlayType=" + ot.toString());
//	      }
//	    });

	    o.addPolygonCompleteHandler(new PolygonCompleteMapHandler() {
	      public void onEvent(PolygonCompleteMapEvent event) {
	        Polygon polygon = event.getPolygon();
	        String kml = KmlGenerator.polygon2Kml(polygon);
	        GWT.log("Polygon completed paths=" + kml);
	        Window.alert(" Representation en kml du polygon\n" + kml);
	      }
	    });

//	    o.addPolylineCompleteHandler(new PolylineCompleteMapHandler() {
//	      public void onEvent(PolylineCompleteMapEvent event) {
//	        Polyline polyline = event.getPolyline();
//	        GWT.log("Polyline completed paths=" + polyline.getPath().toString());
//	      }
//	    });
//
//	    o.addRectangleCompleteHandler(new RectangleCompleteMapHandler() {
//	      public void onEvent(RectangleCompleteMapEvent event) {
//	        Rectangle rectangle = event.getRectangle();
//	        GWT.log("Rectangle completed bounds=" + rectangle.getBounds().getToString());
//	      }
//	    });


	}

}
