package com.upmc.transilien.v1.model;

import com.googlecode.objectify.annotation.AlsoLoad;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
/**
 * Un train
 * @author Kevin Coquart &amp; Mag-Stellon Nadarajah
 *
 */
public class Train {
	@Id
	private Long id; // @Id sur Long => si null un identifiant unique sera auto
						// généré lors d'une insertion
	@Index
	private int depart, terminus;
	@Index
	private String numero;
	@Index
	private String codeMission;
	private String date;
	@Index
	// 0 : ok | 1 : retard | 2 : annulé
	private Integer etat;

	/**
	 * nécessaire pour la conversion automatique JSON
	 */
	public Train() {
	}

	/**
	 * Construit un train
	 * 
	 * @param depart
	 *            codeUIC de la gare de départ
	 * @param terminus
	 *            codeUIC du terminus
	 * @param numero
	 *            son numéro
	 * @param codeMission
	 *            son code Mission
	 * @param date
	 *            la date du départ
	 * @param etat
	 *            l'état du train
	 */
	public Train(int depart, int terminus, String numero, String codeMission, String date, @AlsoLoad("etat") String etat) {
		super();
		this.depart = depart;
		this.terminus = terminus;
		this.numero = numero;
		this.codeMission = codeMission;
		this.date = date;
		this.etat = stringToEtat(etat);
	}

	/**
	 * @return l'id de l'entité
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @return le codeUIC de la gare de départ
	 */
	public int getDepart() {
		return depart;
	}

	/**
	 * @return le codeUIC du terminus
	 */
	public int getTerminus() {
		return terminus;
	}

	/**
	 * @return son numéro
	 */
	public String getNumero() {
		return numero;
	}

	/**
	 * @return son code mission
	 */
	public String getCodeMission() {
		return codeMission;
	}

	/**
	 * @return son horaire de départ
	 */
	public String getDate() {
		return date;
	}

	/**
	 * @return son état
	 */
	public Integer getEtat() {
		return etat;
	}

	// TODO nécessaire plus tard dans le cas ou l'on suive l'avancement du train
	// public void setDepart(int depart) {
	// this.depart = depart;
	// }
	//
	// public void setTerminus(int terminus) {
	// this.terminus = terminus;
	// }
	//
	// public void setDate(String date) {
	// this.date = date;
	// }
	//
	// public void setEtat(EtatTrain etat) {
	// this.etat = etat;
	// }

	private static Integer stringToEtat(String etat) {
		Integer retour = 0;
		if (etat != null)
			switch (etat) {
			case "R":
				retour = 1;
				break;
			case "S":
				retour = 2;
				break;
			}
		return retour;
	}
}
