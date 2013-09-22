package org.rebioma.client.maps;

import com.google.gwt.maps.client.base.LatLng;
import com.google.gwt.maps.client.mvc.MVCArray;
import com.google.gwt.maps.client.mvc.MVCArrayCallback;
import com.google.gwt.maps.client.overlays.Polygon;

public abstract class KmlGenerator {
	
	/**
	 * generation d'une representation en kml d'un polygon
	 * @param polygon
	 * @return
	 */
	public static String polygon2Kml(Polygon polygon){
		final StringBuilder kmlBuilder = new StringBuilder();
		if(polygon != null){
			kmlBuilder.append("<Polygon><outerBoundaryIs><LinearRing><coordinates>");
			
	        MVCArray<LatLng> coords = polygon.getPath();
	        LatLng debutLatLng = coords.get(0);
	        coords.forEach(new MVCArrayCallback<LatLng>() {
				@Override
				public void forEach(LatLng latLng, int arg1) {
					kmlBuilder.append(latLng.getLongitude()).append(",")
					.append(latLng.getLatitude()).append(",0.0 ");
				}
			});
	        //on ferme le "LinearRing" en ajoutant les coordonnées de depart à la fin
	        kmlBuilder.append(debutLatLng.getLongitude()).append(",")
			.append(debutLatLng.getLatitude()).append(",0.0 ");
	        
			kmlBuilder.append("</coordinates></LinearRing></outerBoundaryIs></Polygon>");
		}
		return kmlBuilder.toString();
	}
}
