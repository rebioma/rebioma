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
import org.rebioma.client.KmlDownloadUtility;
import org.rebioma.client.bean.KmlDbRow;
import org.rebioma.server.services.DBFactory;
import org.rebioma.server.services.OccurrenceDbImpl;
import org.rebioma.server.util.HibernateUtil;
import org.rebioma.server.util.KmlUtil;

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
		String nbTableParm = req.getParameter("nbTable");
		int nbTable = Integer.parseInt(nbTableParm);
		//on recupère les autres paramètres
		Map<String, List<Integer>> tableGidsMap = new HashMap<String, List<Integer>>();
		final String gidsSplitRegex = "\\" + KmlDownloadUtility.GIDS_ITEM_SEPARATOR;
		for(int i=0;i< nbTable;i++){
			String tableParamName = KmlDownloadUtility.TABLE_PARAM_PREFIX+i;
			String tableGidsParamName = KmlDownloadUtility.TABLE_GID_PARAM_PREFIX+i;
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
			}
		}

		String kmlFileName = createKmlFile(tableGidsMap);
		resp.setContentType("application/zip");
       resp.setHeader("Content-Disposition",
          "attachment; filename=" + kmlFileName +".zip");
		File kmlFile = new File(getTempPath(), kmlFileName);
		DBFactory.getFileValidationService().zipFiles(
		          new File[] { kmlFile},
		          resp.getOutputStream());
		//kmlFile.delete();
	}
	
	private String createKmlFile(Map<String, List<Integer>> tableGidsMap) throws IOException{
		double gisSimplificationTolerance = 0.01d;//à dynamiser
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
		String fileName = "limite_region_"+ fileNameSuffix.toString();
		KmlUtil.writeKmlFile(kml, getTempPath(), fileName);
		return fileName + ".kml";
	}
	
	private String getTempPath(){
		String rootPath = getServletContext().getRealPath("/");
		String tempPath = rootPath + "temp";
		return tempPath;
	}
	
}
