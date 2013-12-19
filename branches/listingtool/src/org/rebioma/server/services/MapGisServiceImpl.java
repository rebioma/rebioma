package org.rebioma.server.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.Type;
import org.rebioma.client.KmlUtil;
import org.rebioma.client.bean.KmlDbRow;
import org.rebioma.client.bean.ShapeFileInfo;
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
			info.setTableName("lim_region_aout06");
			infos.add(info);
		}else{
			Session sess = HibernateUtil.getSessionFactory().openSession();
			StringBuilder sqlBuilder = new StringBuilder("SELECT gid, nom_region as name FROM ");
			sqlBuilder.append(shapeFile.getTableName());
			SQLQuery sqlQuery = sess
					.createSQLQuery(sqlBuilder.toString());
			sqlQuery.addScalar(KmlUtil.KML_GID_NAME);
			sqlQuery.addScalar(KmlUtil.KML_LABEL_NAME);
			sqlQuery.setResultTransformer(Transformers.aliasToBean(KmlDbRow.class));
			List<KmlDbRow> kmlDbRows = sqlQuery.list();
			//recuperer les lignes d'un fichier shape
			for(KmlDbRow row: kmlDbRows){
				ShapeFileInfo info = new ShapeFileInfo();
				info.setGid(row.getGid());
				info.setLibelle(row.getName());
				info.setTableName(shapeFile.getTableName());
				infos.add(info);
			}
		}
		return infos;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Integer> findOccurrenceIdsByShapeFiles(
			Map<String, List<Integer>> tableGidsMap) {
		StringBuilder sqlBuilder = new StringBuilder("SELECT o.id FROM occurrence o ");
		StringBuilder sqlWhereBuilder = new StringBuilder();
		int index = 0;
		Map<String, List<Integer>> gidsParamMap = new HashMap<String, List<Integer>>();
		for(Entry<String, List<Integer>> entry: tableGidsMap.entrySet()){
			//String paramName = "gids" + index;
			String tableName = entry.getKey();
			List<Integer> gids = entry.getValue();
			sqlBuilder.append(" JOIN ").append(tableName).append(" ON ");
			//sqlBuilder.append(tableName).append(".gid IN (:").append(paramName).append(") ");
			sqlBuilder.append("( ");
			int gidIdx = 0;
			for(Integer gid: gids){
				if(gidIdx > 0){
					sqlBuilder.append(" OR ");
				}
				sqlBuilder.append(tableName).append(".gid=").append(gid);
				gidIdx++;
			}
			sqlBuilder.append(") ") ;
			if(sqlWhereBuilder.length() == 0){
				sqlWhereBuilder.append(" WHERE ");
			}else{
				sqlWhereBuilder.append(" OR ");
			}
			sqlWhereBuilder.append(" ST_Within(").append("o.geom,").append(tableName).append(".geom").append(")='t' ");
			//gidsParamMap.put(paramName, gids);
			index++;
		}
		sqlBuilder.append(sqlWhereBuilder.toString());
		List<Integer> occurrenceIds = new ArrayList<Integer>();
		Session sess = HibernateUtil.getSessionFactory().openSession();
		SQLQuery sqlQuery = sess
				.createSQLQuery(sqlBuilder.toString());
//		for(Entry<String, List<Integer>> paramEntry: gidsParamMap.entrySet()){
//			sqlQuery.setParameter(paramEntry.getKey(), paramEntry.getValue());
//		}
		occurrenceIds = sqlQuery.list();
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