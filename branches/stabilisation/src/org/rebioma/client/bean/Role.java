package org.rebioma.client.bean;

import org.rebioma.client.i18n.LocaleListBox;

// Generated Feb 23, 2010 8:53:01 PM by Hibernate Tools 3.2.5.Beta

/**
 * Role generated by hbm2java
 */
public class Role implements java.io.Serializable {

  private Integer id;
  private String nameEn;
  private String nameFr;
  private String descriptionEn;
  private String descriptionFr;

  public Role() {
  }

  public Role(String nameEn, String nameFr, String descriptionEn,
      String descriptionFr) {
    this.nameEn = nameEn;
    this.nameFr = nameFr;
    this.descriptionEn = descriptionEn;
    this.descriptionFr = descriptionFr;
  }

  public boolean equals(Object obj) {
    if (obj == null || !(obj instanceof Role)) {
      return false;
    }
    return id != null && id.equals(((Role) obj).getId());
  }

  public String getDescriptionEn() {
    return this.descriptionEn;
  }

  public String getDescriptionFr() {
    return this.descriptionFr;
  }

  public Integer getId() {
    return this.id;
  }

  public String getName(String locale) {
    if (locale.equals(LocaleListBox.ENGLISH)) {
      return nameEn;
    } else {
      return nameFr;
    }
  }

  public String getNameEn() {
    return this.nameEn;
  }

  public String getNameFr() {
    return this.nameFr;
  }

  public int hashCode() {
    return id == null ? 0 : id.hashCode();
  }

  public void setDescriptionEn(String descriptionEn) {
    this.descriptionEn = descriptionEn;
  }

  public void setDescriptionFr(String descriptionFr) {
    this.descriptionFr = descriptionFr;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public void setNameEn(String nameEn) {
    this.nameEn = nameEn;
  }

  public void setNameFr(String nameFr) {
    this.nameFr = nameFr;
  }

  public String toString() {
    return nameEn;
  }
}