package com.upmc.transilien.v1.model;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
/**
 * Description d'un objet gare
 * @author Kevin Coquart &amp; Mag-Stellon Nadarajah
 *
 */
public class Gare implements Comparable<Gare> {
	@Id
	/*
	 * @Id sur Long => si null un identifiant unique sera auto généré lors d'une insertion
	 */
	private Long id;
	@Index
	private String nom;
	@Index
	private Integer codeUIC;
	@Index
	private Integer codeUIC2;
	private Double longitude;
	private Double latitude;
	private String voisin1;
	private String voisin2;

	/**
	 * nécessaire pour la conversion automatique JSON
	 */
	public Gare() {
	}

	/**
	 * construit une gare
	 * 
	 * @param nom
	 *            son nom
	 * @param codeUIC
	 *            son codeUIC
	 * @param longitude
	 *            sa longitude
	 * @param latitude
	 *            sa latitude
	 */
	public Gare(String nom, int codeUIC, Double longitude, Double latitude) {
		super();
		this.nom = nom;
		this.codeUIC = codeUIC;
		this.codeUIC2 = null;
		this.longitude = longitude;
		this.latitude = latitude;
		this.voisin1 = null;
		this.voisin2 = null;
	}

	/**
	 * @return son nom
	 */
	public String getNom() {
		return nom;
	}

	/**
	 * @return son codeUIC
	 */
	public Integer getCodeUIC() {
		return codeUIC;
	}

	/**
	 * @return son codeUIC2
	 */
	public Integer getCodeUIC2() {
		return codeUIC2;
	}

	/**
	 * @return ses codeUIC
	 */
	public Integer[] getCodesUIC() {
		Integer[] tmp = { codeUIC, codeUIC2 };
		return tmp;
	}

	public void ajouteUIC(Integer codeUIC2, boolean swap) {
		if (swap) {
			this.codeUIC2 = this.codeUIC;
			this.codeUIC = codeUIC2;
		} else {
			this.codeUIC2 = codeUIC2;
		}
	}

	/**
	 * @return sa longitude
	 */
	public Double getLongitude() {
		return longitude;
	}

	/**
	 * @return sa latitude
	 */
	public Double getLatitude() {
		return latitude;
	}

	/**
	 * @return le voisin1
	 */
	public String getVoisin1() {
		return voisin1;
	}

	/**
	 * @return le voisin2
	 */
	public String getVoisin2() {
		return voisin2;
	}

	/**
	 * Calcule la distance entre 2 gares
	 * 
	 * @param o
	 *            la gare cible
	 * @return la distance entre la gare courante et la gare cible
	 */
	public Double calcule(Gare o) {
		return Math.sqrt(Math.pow(longitude - o.longitude, 2) + Math.pow(latitude - o.latitude, 2));
	}

	/**
	 * ajoute un voisin en commencant par le voisin1.
	 * 
	 * @param go
	 *            la gare voisine ajoutée
	 * @return vrai si les 2 gares sont remplis
	 * @throws Exception
	 *             si les 2 gares sont rempli avant l'appel à cette fonction
	 */
	public boolean ajoute(Gare go, boolean recursion) throws Exception {
		boolean result = false;

		if (voisin1 == null)
			voisin1 = go.getNom();
		else if (voisin2 == null) {
			voisin2 = go.getNom();
			result = true;
		} else
			throw new Exception("Les 2 gares sont déjà rempli, qu'est ce que t'as foutu sur l'algo ...");

		if (recursion) {
			result = go.ajoute(this, false);
		}
		return result;
	}

	@Override
	public String toString() {
		String s = "";
		if (codeUIC2 != null) {
			s = "Gare [nom=" + nom + ", codeUIC=[" + codeUIC + ", " + codeUIC2 + "], longitude=" + longitude
					+ ", lattitude=" + latitude + "]\n";
		} else
			s = "Gare [nom=" + nom + ", codeUIC=" + codeUIC + ", longitude=" + longitude + ", lattitude=" + latitude
					+ "]\n";
		return s;
	}

	@Override
	public int compareTo(Gare arg0) {
		return nom.compareTo(arg0.nom);
	}

}
