package com.upmc.transilien.v1.model;

/**
 * Enumeration des différents états possible d'un train : à l'heure, en retard ou supprimé.
 * 
 * @author Kevin Coquart &amp; Mag-Stellon Nadarajah
 *
 */
public enum EtatTrain {
	RAS, RETARD, SUP;

	/**
	 * Convertir une chaine de caractere en état
	 * 
	 * @param etat
	 *            l'état écrit
	 * @return l'objet état
	 */
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
