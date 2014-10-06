package com.upmc.transilien.v1.model;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class Gare {
	@Id
	/* @Id sur Long => si null un identifiant unique sera auto généré lors d'une insertion */
	private Long id;
	@Index
	private String nom;
	@Index
	private String codeUIC;
	private Double longitude, lattitude;

	// nécessaire pour la conversion automatique JSON
	public Gare() {
	}

	public Gare(String nom, String codeUIC, Double longitude, Double lattitude) {
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

	public Double getLongitude() {
		return longitude;
	}

	public Double getLattitude() {
		return lattitude;
	}

	@Override
	public String toString() {
		return "Gare [nom=" + nom + ", codeUIC=" + codeUIC + ", longitude=" + longitude + ", lattitude=" + lattitude + "]\n";
	}

}
