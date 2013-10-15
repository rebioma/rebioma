package org.rebioma.client.bean;

import java.io.Serializable;

public class ShapeFileInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6181794001289843474L;
	
	private int gid;//identifiant de l'enregistrement dans la table "this.tableName"
	
	private String libelle;//le label à afficher sur le FO
	
	private String tableName;//le nom de la table(shape) qui contient l'enregitrement

	public int getGid() {
		return gid;
	}

	public void setGid(int gid) {
		this.gid = gid;
	}

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
}
