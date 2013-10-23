package org.rebioma.server;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.rebioma.client.KmlUtil;
import org.rebioma.client.bean.KmlDbRow;
import org.rebioma.server.services.DBFactory;
import org.rebioma.server.util.HibernateUtil;


public class KmlFileServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6949654865922950754L;
	
	 private final Logger log = Logger.getLogger(KmlFileServlet.class);

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		final double gisTableSimplificationTolerance = 0.01d;//à dynamiser
		//on recupère les autres paramètres
		Map<String, List<Integer>> tableGidsMap = new HashMap<String, List<Integer>>();
		final String gidsSplitRegex = "\\" + KmlUtil.GIDS_ITEM_SEPARATOR;
		String tableParamName = KmlUtil.TABLE_PARAM_NAME;
		String tableGidsParamName = KmlUtil.GIDS_PARAM_NAME;
		String tableName = req.getParameter(tableParamName);
		String tableGidsParam = req.getParameter(tableGidsParamName);
		if(StringUtils.isNotBlank(tableName)){
			//on va decouper les gids et les transformer en integer
			List<Integer> gids = new ArrayList<Integer>();
			if(StringUtils.isNotBlank(tableGidsParam)){
				String[] gidsStrs = tableGidsParam.split(gidsSplitRegex);
				for(String gidsStr: gidsStrs){
					try{
						gids.add(Integer.parseInt(gidsStr));
					}catch(NumberFormatException nfe){
						//log ?
					}
				}
			}
			if(tableGidsMap.containsKey(tableName)){
				tableGidsMap.get(tableName).addAll(gids);
			}else{
				tableGidsMap.put(tableName, gids);
			}
			//
			java.util.Collections.sort(gids);
			String kmlFileName = getKmlFileName(tableName, gids, gisTableSimplificationTolerance);
			resp.setContentType("application/vnd.google-earth.kmz"); //'application/vnd.google-earth.kml+xml' pour un fichier kml
	        resp.setHeader("Content-Disposition", "attachment; filename=" + tableName +".kmz");
			File kmlFile = new File(getTempPath(), kmlFileName);
			DBFactory.getFileValidationService().zipFiles(new File[] {kmlFile}, resp.getOutputStream());
		}
		//kmlFile.delete();
	}
	
	private String getKmlFileName(String tableName, List<Integer> gids, double gisSimplificationTolerance) throws IOException{
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ").append(KmlUtil.KML_GID_NAME).append(",").append(KmlUtil.KML_LABEL_NAME).append(", ST_AsKML(ST_Simplify(geom, :tolerance)) as gisAsKmlResult ");
		sql.append(" FROM ").append(tableName).append(" WHERE ");
		StringBuilder fileNameSuffix = new StringBuilder();
		int idx = 0;
		for(Integer gid: gids){//on prefère utilise des OR plutot que un IN
			if(idx > 0){
				sql.append(" OR ");
			}
			sql.append("gid=").append(gid);
			fileNameSuffix.append("-").append(gid);
			idx++;
		}
		String fileName = tableName + fileNameSuffix.toString();
		File file = new File(getTempPath() + "/" + fileName + ".kml");
		if(!file.exists()){
			Session sess = HibernateUtil.getSessionFactory().openSession();
			SQLQuery sqlQuery = sess.createSQLQuery(sql.toString());
			sqlQuery.setDouble("tolerance", gisSimplificationTolerance);
			sqlQuery.addScalar(KmlUtil.KML_GID_NAME);
			sqlQuery.addScalar(KmlUtil.KML_LABEL_NAME);
			sqlQuery.addScalar("gisAsKmlResult");
			sqlQuery.setResultTransformer(Transformers.aliasToBean(KmlDbRow.class));
			List<KmlDbRow> kmlDbRows = sqlQuery.list();
			String kml = KmlUtil.getKMLString(kmlDbRows);
			KmlUtil.writeKmlFile(kml, getTempPath(), fileName);
		}
		return fileName + ".kml";
	}
	
	private String getTempPath(){
		String rootPath = getServletContext().getRealPath("/");
		String tempPath = rootPath + "temp";
		return tempPath;
	}
	
}
