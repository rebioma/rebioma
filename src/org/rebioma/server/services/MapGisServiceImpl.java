package org.rebioma.server.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.Type;
import org.rebioma.client.bean.KmlDbRow;
import org.rebioma.client.bean.ShapeFileInfo;
import org.rebioma.client.services.MapGisService;
import org.rebioma.server.util.HibernateUtil;
import org.rebioma.server.util.KmlUtil;

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

	@Override
	public List<ShapeFileInfo> getShapeFileItems(ShapeFileInfo shapeFile) {
		List<ShapeFileInfo> infos = new ArrayList<ShapeFileInfo>();
		if(shapeFile == null){
			//recuperer la liste des fichier shapes
			ShapeFileInfo info = new ShapeFileInfo();
			info.setLibelle("Limite region Serveur");
			infos.add(info);
		}else{
			//recuperer les lignes d'un fichier shape
			for(int i=1;i<= 22;i++){
				ShapeFileInfo info = new ShapeFileInfo();
				info.setGid(i);
				info.setLibelle("region  Serveur " + i);
				infos.add(info);
			}
		}
		return infos;
	}
	
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws IOException{
		double gisSimplificationTolerance = 0.01d;
		List<Integer> gids = new ArrayList<Integer>();
		gids.add(8);
		gids.add(2);
		gids.add(9);
		gids.add(20);
		StringBuilder fileNameSuffix = new StringBuilder();
		for(Integer gid: gids){
			fileNameSuffix.append(gid);
		}
		Session sess = HibernateUtil.getSessionFactory().openSession();
		//SQLQuery sqlQuery = sess.createSQLQuery("SELECT gid, nom_region, ST_AsKML(ST_Simplify(geom,0.01)) FROM lim_region_aout06_4326");
		SQLQuery sqlQuery = sess.createSQLQuery("SELECT gid, nom_region as name, ST_AsKML(ST_Simplify(geom, :tolerance)) as gisAsKmlResult FROM lim_region_aout06_4326 WHERE gid IN (:gids)");
		sqlQuery.setParameterList("gids", gids);
		sqlQuery.setDouble("tolerance", gisSimplificationTolerance);
		sqlQuery.addScalar("gid");
		sqlQuery.addScalar("name");
		sqlQuery.addScalar("gisAsKmlResult");
		sqlQuery.setResultTransformer(Transformers.aliasToBean(KmlDbRow.class));
		List<KmlDbRow> kmlDbRows = sqlQuery.list();
		String kml = KmlUtil.getKMLString(kmlDbRows);
		KmlUtil.writeKmlFile(kml, "D:/NainaZo/workspace/Rebioma/war/temp", "limite_region_"+ fileNameSuffix.toString());
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
