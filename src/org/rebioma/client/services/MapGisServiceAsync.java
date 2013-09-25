package org.rebioma.client.services;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface MapGisServiceAsync {

	void findOccurrenceIdByGeom(String kml,
			AsyncCallback<List<Integer>> callback);


}
