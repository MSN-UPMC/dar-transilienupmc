package com.upmc.transilien.v1.model;

import static com.googlecode.objectify.ObjectifyService.ofy;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class Train {
	@Id
	private Long id; // @Id sur Long => si null un identifiant unique sera auto
						// généré lors d'une insertion
	@Index
	private long depart, terminus;
	@Index
	private int numero;
	@Index
	private String codeMission;
	private String date;
	@Index
	private EtatTrain etat;

	// nécessaire pour la conversion automatique JSON
	public Train() {
	}

	public Train(long depart, long terminus, int numero, String codeMission, String date, EtatTrain etat) {
		super();
		this.depart = depart;
		this.terminus = terminus;
		this.numero = numero;
		this.codeMission = codeMission;
		this.date = date;
		this.etat = etat;
	}

	public Train(long depart, String terminus, String numero, String codeMision, String date, String etat) {
		this(depart, Long.parseLong(terminus), Integer.parseInt(numero), codeMision, date, EtatTrain.stringToEtat(etat));
	}

	public Gare getDepart() {
		return ofy().load().type(Gare.class).filter("id =", depart).list().get(0);
	}

	public Gare getTerminus() {
		return ofy().load().type(Gare.class).filter("id =", terminus).list().get(0);
	}

	public int getNumero() {
		return numero;
	}

	public String getCodeMission() {
		return codeMission;
	}

	public String getDate() {
		return date;
	}

	public EtatTrain getEtat() {
		return etat;
	}

	@Override
	public String toString() {
		String result = "";
		switch (etat) {
		case RAS:
			result = "Le train " + codeMission + "n°" + numero + " terminus : " + ofy().load().type(Gare.class).filter("id =", terminus).list().get(0).getNom()
					+ " partira de " + ofy().load().type(Gare.class).filter("id =", depart).list().get(0).getNom() + " à " + date + ".";
			break;
		case RETARD:
			result = "Le train " + codeMission + "n°" + numero + " terminus : " + ofy().load().type(Gare.class).filter("id =", terminus).list().get(0).getNom()
					+ " et départ " + ofy().load().type(Gare.class).filter("id =", depart).list().get(0).getNom() + " à " + date + " est retardé.";
			break;
		case SUP:
			result = "Le train " + codeMission + "n°" + numero + " terminus : " + ofy().load().type(Gare.class).filter("id =", terminus).list().get(0).getNom()
					+ " et départ " + ofy().load().type(Gare.class).filter("id =", depart).list().get(0).getNom() + " à " + date + " est supprimé.";
			break;
		}
		return result;
	}

	public void setDepart(long depart) {
		this.depart = depart;
	}

	public void setTerminus(long terminus) {
		this.terminus = terminus;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	public void setCodeMission(String codeMission) {
		this.codeMission = codeMission;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public void setEtat(EtatTrain etat) {
		this.etat = etat;
	}
}
