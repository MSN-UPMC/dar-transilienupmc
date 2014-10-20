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
	}

	/**
	 * @return l'id de la gare
	 */
	public Long getId() {
		return id;
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
	 * Calcule la distance entre 2 gares
	 * 
	 * @param o
	 *            la gare cible
	 * @return la distance entre la gare courante et la gare cible
	 */
	public Double calcule(Gare o) {
		double latLongToMeters = 111319.49079327;
		double deltaLat = (latitude - o.latitude) * latLongToMeters, deltaLong = (longitude - o.longitude) * latLongToMeters;
		return Math.sqrt(Math.pow(deltaLat, 2) + Math.pow(deltaLong, 2));
	}

	@Override
	public String toString() {
		String s = "";
		if (codeUIC2 != null) {
			s = "Gare [nom=" + nom + ", codeUIC=[" + codeUIC + ", " + codeUIC2 + "], longitude=" + longitude + ", lattitude=" + latitude + "]\n";
		} else
			s = "Gare [nom=" + nom + ", codeUIC=" + codeUIC + ", longitude=" + longitude + ", lattitude=" + latitude + "]\n";
		return s;
	}

	@Override
	public int compareTo(Gare arg0) {
		return nom.compareTo(arg0.nom);
	}

}
