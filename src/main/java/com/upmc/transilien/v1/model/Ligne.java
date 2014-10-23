package com.upmc.transilien.v1.model;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
/**
 * Objet ligne
 * @author Kevin Coquart &amp; Mag-Stellon Nadarajah
 *
 */
public class Ligne {
	@Id
	/* @Id sur Long => si null un identifiant unique sera auto généré lors d'une insertion */
	private Long id;
	@Index
	private String nom;
	private List<Integer> gares;

	/**
	 * nécessaire pour la conversion automatique JSON
	 */
	public Ligne() {
	}

	/**
	 * Construit une ligne vide
	 * 
	 * @param nom
	 *            son nom
	 */
	public Ligne(String nom) {
		super();
		this.nom = nom;
		gares = new ArrayList<Integer>();
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
	 * @return les codeUIC des gares qui composent la ligne
	 */
	public List<Integer> getGares() {
		return gares;
	}

	/**
	 * ajoute une gare à la ligne
	 * 
	 * @param gare
	 *            le codeUIC de la gare
	 */
	public void addGares(int gare) {
		if (!gares.contains(gare))
			gares.add(gare);
	}

	/**
	 * modifie les gares de la ligne
	 * 
	 * @param newGares
	 *            la nouvelle liste de gares
	 */
	public void setGares(List<Integer> newGares) {
		gares = newGares;
	}

	@Override
	public String toString() {
		return "Ligne [nom=" + nom + ", gares=" + gares + "]\n";
	}
}
