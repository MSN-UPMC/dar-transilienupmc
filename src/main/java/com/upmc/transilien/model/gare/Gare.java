package com.upmc.transilien.model.gare;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
public class Gare {
	@Id
	/* @Id sur Long => si null un identifiant unique sera auto généré lors d'une insertion */
	private Long id;
	private String nom;
	private String codeUIC;
	private Integer longitude, lattitude;

	// nécessaire pour la conversion automatique JSON
	public Gare() {
	}

	public Gare(String nom, String codeUIC, int longitude, int lattitude) {
		super();
		this.nom = nom;
		this.codeUIC = codeUIC;
		this.longitude = longitude;
		this.lattitude = lattitude;
	}

	public String getNom() {
		return nom;
	}

	public String getCodeUIC() {
		return codeUIC;
	}

	public int getLongitude() {
		return longitude;
	}

	public int getLattitude() {
		return lattitude;
	}

	@Override
	public String toString() {
		return "Gare [nom=" + nom + ", codeUIC=" + codeUIC + ", longitude=" + longitude + ", lattitude=" + lattitude + "]";
	}

}
