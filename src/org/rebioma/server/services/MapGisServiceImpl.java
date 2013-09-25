package org.rebioma.server.services;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.rebioma.client.services.MapGisService;
import org.rebioma.server.util.HibernateUtil;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class MapGisServiceImpl extends RemoteServiceServlet implements
		MapGisService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1166479109921202488L;

	@Override
	public List<Integer> findOccurrenceIdByGeom(String kml) {
		List<Integer> occurrenceIds = new ArrayList<Integer>();
		Session sess = HibernateUtil.getSessionFactory().openSession();
		sess = HibernateUtil.getSessionFactory().openSession();
		SQLQuery sqlQuery = sess
				.createSQLQuery("SELECT ST_IsValid(ST_GeomFromKML(:kml)) as isValid ");
		sqlQuery.setParameter("kml", kml);
		Boolean isValide = (Boolean) sqlQuery.uniqueResult();
		if (isValide) {
			sqlQuery = sess
					.createSQLQuery("SELECT id FROM occurrence WHERE ST_Within(geom, ST_GeomFromKML(:kml))='t'");
			sqlQuery.setParameter("kml", kml);
			occurrenceIds = sqlQuery.list();
		} else {
			throw new RuntimeException("Le kml généré n'est pas valide \n ["
					+ kml + "]");
		}
		return occurrenceIds;
	}

	// @Override
	// public List<Integer> findOccurrenceIdByGeom(/*OverlayType overlayType,*/
	// List<LatLng> geomCoordonnees) {
	// List<Integer> occurrenceIds = new ArrayList<Integer>();
	// String kml = KmlGenerator.kmlFromCoords(/*overlayType,
	// */geomCoordonnees);
	// Session sess = HibernateUtil.getSessionFactory().openSession();
	// sess=HibernateUtil.getSessionFactory().openSession();
	// SQLQuery sqlQuery =
	// sess.createSQLQuery("SELECT ST_IsValid(ST_GeomFromKML(:kml)) as isValid ");
	// sqlQuery.setParameter("kml", kml);
	// sqlQuery.addEntity(Boolean.class);
	// Boolean isValide = (Boolean)sqlQuery.uniqueResult();
	// if(isValide){
	// sqlQuery =
	// sess.createSQLQuery("SELECT id FROM occurrence WHERE ST_Within(geom, ST_GeomFromKML(:kml))='t'");
	// sqlQuery.addEntity(Integer.class);
	// sqlQuery.setParameter("kml", kml);
	// occurrenceIds = sqlQuery.list();
	// }else{
	// throw new RuntimeException("Le kml généré n'est pas valide \n ["+ kml
	// +"]");
	// }
	// return occurrenceIds;
	// }

}
