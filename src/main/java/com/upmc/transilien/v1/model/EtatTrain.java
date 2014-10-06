package com.upmc.transilien.v1.model;

public enum EtatTrain {
	RAS, RETARD, SUP;

	public static EtatTrain stringToEtat(String etat) {
		EtatTrain retour = RAS;
		if (etat != null)
			switch (etat) {
			case "R":
				retour = RETARD;
				break;
			case "S":
				retour = SUP;
				break;
			}
		return retour;
	}
}
