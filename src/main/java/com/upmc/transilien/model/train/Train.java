package com.upmc.transilien.model.train;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.upmc.transilien.model.gare.Gare;

@Entity
public class Train {
	@Id
	private Long id; // @Id sur Long => si null un identifiant unique sera auto
						// généré lors d'une insertion
	private Gare depart, terminus;
	private int numero;
	private String codeMission;
	private String date;
	private EtatTrain etat;

	public Train(Gare depart, Gare terminus, int numero, String codeMission,
			String date, EtatTrain etat) {
		super();
		this.depart = depart;
		this.terminus = terminus;
		this.numero = numero;
		this.codeMission = codeMission;
		this.date = date;
		this.etat = etat;
	}

	public Gare getDepart() {
		return depart;
	}

	public Gare getTerminus() {
		return terminus;
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
			result = "Le train " + codeMission + "n�" + numero + " terminus : "
					+ terminus.getNom() + " partira de " + depart.getNom()
					+ " � " + date + ".";
			break;
		case RETARD:
			result = "Le train " + codeMission + "n�" + numero + " terminus : "
					+ terminus.getNom() + " et d�part " + depart.getNom()
					+ " � " + date + " est retard�.";
			break;
		case SUP:
			result = "Le train " + codeMission + "n�" + numero + " terminus : "
					+ terminus.getNom() + " et d�part " + depart.getNom()
					+ " � " + date + " est supprim�.";
			break;
		}
		return result;
	}

}
