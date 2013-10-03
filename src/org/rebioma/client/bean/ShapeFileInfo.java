package org.rebioma.client.bean;

import java.io.Serializable;

public class ShapeFileInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6181794001289843474L;
	
	private int gid;
	
	private String libelle;

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
}
