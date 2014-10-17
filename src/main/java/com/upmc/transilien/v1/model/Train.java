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
	private EtatTrain etat;

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
		this.etat = EtatTrain.stringToEtat(etat);
	}

	/**
	 * @return le codeUIC de la gare de départ
	 */
	public int depart() {
		return depart;
	}

	/**
	 * @return le codeUIC du terminus
	 */
	public int terminus() {
		return terminus;
	}

	/**
	 * @return son numéro
	 */
	public String numero() {
		return numero;
	}

	/**
	 * @return son code mission
	 */
	public String codeMission() {
		return codeMission;
	}

	/**
	 * @return son horaire de départ
	 */
	public String date() {
		return date;
	}

	/**
	 * @return son état
	 */
	public EtatTrain etat() {
		return etat;
	}

	@Override
	public String toString() {
		String result = "";
		// switch (etat) {
		// case RAS:
		// result = "Le train " + codeMission + "n°" + numero + " terminus : " + getTerminus().getNom() + " partira de "
		// + getDepart().getNom() + " à " + date
		// + ".";
		// break;
		// case RETARD:
		// result = "Le train " + codeMission + "n°" + numero + " terminus : " + getTerminus().getNom() + " et départ "
		// + getDepart().getNom() + " à " + date
		// + " est retardé.";
		// break;
		// case SUP:
		// result = "Le train " + codeMission + "n°" + numero + " terminus : " + getTerminus().getNom() + " et départ "
		// + getDepart().getNom() + " à " + date
		// + " est supprimé.";
		// break;
		// }
		result = "Le train " + codeMission + "n°" + numero + " terminus : " + terminus + " partira de " + depart
				+ " à " + date + ".";
		return result + "\n";
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
}
