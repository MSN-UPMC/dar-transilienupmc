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
public class Gare {
	@Id
	/* @Id sur Long => si null un identifiant unique sera auto généré lors d'une insertion */
	private Long id;
	@Index
	private String nom;
	@Index
	private int codeUIC;
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
		this.longitude = longitude;
		this.latitude = latitude;
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
	public int getCodeUIC() {
		return codeUIC;
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
		return Math.sqrt(Math.pow(longitude - o.longitude, 2) + Math.pow(latitude - o.latitude, 2));
	}

	@Override
	public String toString() {
		return "Gare [nom=" + nom + ", codeUIC=" + codeUIC + ", longitude=" + longitude + ", lattitude=" + latitude + "]\n";
	}
}
