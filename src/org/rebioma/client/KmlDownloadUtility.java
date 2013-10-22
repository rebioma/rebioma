package org.rebioma.client;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Hidden;

/**
 * 
 * @author Mikajy
 *
 */
public class KmlDownloadUtility{
	
	public static final String GIDS_ITEM_SEPARATOR = "|";
	public static final String TABLE_PARAM_PREFIX = "table";
	public static final String TABLE_GID_PARAM_PREFIX = "gidtable";
	
	private FormPanel form;

	public KmlDownloadUtility() {
		super();
		form = new FormPanel();
		form.setAction(GWT.getModuleBaseURL() + "kmlfile");
	    form.setEncoding(FormPanel.ENCODING_MULTIPART);
	    form.setMethod(FormPanel.METHOD_POST);
	}
	
	public void submit(Map<String, List<Integer>> tableGidsMap){
		StringBuilder gidsParTables = new StringBuilder();
		StringBuilder tables = new StringBuilder();
		int tableIndex = 0;
		form.clear();//on efface tous les elements du formulaire avant d'en ajouter
		Hidden nbTable = new Hidden("nbTable", tableGidsMap.size()+"");
		form.add(nbTable);
		for(Entry<String, List<Integer>> entry: tableGidsMap.entrySet()){
			String tableParamName = TABLE_PARAM_PREFIX+tableIndex;
			String tableParamValue = entry.getKey();
			Hidden tableField = new Hidden(tableParamName, tableParamValue);
			form.add(tableField);//une paramètre par table
			//gids
			int gidIndex = 0;
			gidsParTables.setLength(0);
			for(Integer gid: entry.getValue()){
				if(gidIndex > 0){
					gidsParTables.append(GIDS_ITEM_SEPARATOR);
				}
				gidsParTables.append(gid);
				gidIndex++;
			}
			String gidTableParamName = TABLE_GID_PARAM_PREFIX+tableIndex;
			Hidden tableGidsField = new Hidden(gidTableParamName, gidsParTables.toString());
			form.add(tableGidsField);
			tableIndex++;
		}
		form.submit();
	}
}
