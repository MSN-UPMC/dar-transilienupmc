package com.upmc.transilien.algo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import com.upmc.transilien.tools.math.Vecteur;
import com.upmc.transilien.v1.model.Gare;
import com.upmc.transilien.v1.model.Ligne;
import com.upmc.transilien.v1.repository.GareRepository;

// TODO attention au textes des messages d'erreurs
/**
 * Calcule les lignes et les gares sous forme de graphe orienté dans le but de tracer les lignes sur la carte et
 * d'optimisé notre calcule d'itinéaire.
 * 
 * @author Kevin Coquart &amp; Mag-Stellon Nadarajah
 *
 */
public class LigneOriente {
	private static int angleMinimumOpposition = 45;

	/**
	 * Couple pour répresenter le codeUIC de la gare et la distance par rapport à la gare de référence.
	 * 
	 * @author Kevin Coquart &amp; Mag-Stellon Nadarajah
	 *
	 */
	static class Couple {
		String nom;
		double distance;

		/**
		 * Constructeur
		 * 
		 * @param nom
		 *            le nom de la gare
		 * @param distance
		 *            la distance par rapport à la gare de référence
		 */
		Couple(String nom, double distance) {
			this.nom = nom;
			this.distance = distance;
		}
	}

	/**
	 * Sous classe pour répresenter les voisins lors de l'algo
	 * 
	 * @author Kevin Coquart &amp; Mag-Stellon Nadarajah
	 *
	 */
	static class Voisin {
		private String voisin1;
		private String voisin2;
	}

	/**
	 * ajoute un voisin en commencant par le voisin1.
	 * 
	 * @param voisine
	 *            la gare voisine ajoutée
	 * @return vrai si les 2 gares sont remplis
	 * @throws Exception
	 *             si les 2 gares sont rempli avant l'appel à cette fonction
	 */
	private static boolean ajoute(Map<String, Voisin> voisins, String courante, String voisine, boolean recursion)
			throws Exception {
		boolean result = false;
		String voisin1 = voisins.get(courante).voisin1;
		String voisin2 = voisins.get(courante).voisin2;

		if (voisin1 == null)
			voisin1 = voisine;
		else if (voisin2 == null) {
			voisin2 = voisine;
			result = true;
		} else
			throw new Exception("Les 2 voisins sont déjà rempli, qu'est ce que t'as foutu sur l'algo ...");

		if (recursion) {
			result = ajoute(voisins, voisine, courante, false);
		}
		return result;
	}

	/**
	 * Execute l'algorithme qui calcule des gares orientées.
	 * 
	 * @param ligne
	 * @throws Exception
	 */
	public static Collection<Gare> execute(Ligne ligne) throws Exception {
		Map<String, Gare> gos = new HashMap<String, Gare>();
		/**
		 * 1) On ajoute à une map toutes les gares de la ligne
		 */
		List<Integer> garesLigne = new ArrayList<Integer>(ligne.getGares());
		List<String> aTraiter = new ArrayList<String>();
		for (Integer ite : garesLigne) {
			Gare gare = GareRepository.getInstance().findGareByCode(ite);
			aTraiter.add(gare.getNom());
			gos.put(gare.getNom(), gare);
		}

		/**
		 * 2) On associe a chaque gare un Voisin
		 */
		Map<String, Voisin> voisins = new HashMap<String, Voisin>();
		for (String nomGare : gos.keySet())
			voisins.put(nomGare, new Voisin());

		/**
		 * On traite chaque gare de la ligne
		 */
		int i = 100;
		while (!aTraiter.isEmpty() && i-- > 0) {
			Gare gareElt = gos.get(aTraiter.remove(0));

			/**
			 * 3) Création d'une file de priorité basé sur des couples <codeUIC, distance / la gare traité>
			 */
			PriorityQueue<Couple> distances = new PriorityQueue<Couple>(5, new Comparator<Couple>() {
				public int compare(Couple c1, Couple c2) {
					return Double.compare(c1.distance, c2.distance);
				}
			});

			/**
			 * 4) Calcule des distances de toutes les gares non traité / à la gare de réf
			 */
			for (String nom : aTraiter)
				distances.add(new Couple(nom, gareElt.calcule(gos.get(nom))));

			/**
			 * 5) Tant que la file n'est pas vide, on recherche les voisins pour les ajouters.
			 */
			String voisin1 = voisins.get(gareElt.getNom()).voisin1;
			String voisin2 = voisins.get(gareElt.getNom()).voisin2;
			while (!distances.isEmpty() && (voisin1 == null || voisin2 == null)) {
				Couple c = distances.poll();
				Gare voisinPotentielle = gos.get(c.nom);

				/**
				 * 5.1) Le voisin1 est null, le voisin2 aussi par construction.<br>
				 * On indique aux gares qu'elles sont voisines.
				 */
				if (voisin1 == null) {
					if (ajoute(voisins, gareElt.getNom(), voisinPotentielle.getNom(), true))
						aTraiter.remove((Object) voisinPotentielle.getNom());
				} else {
					if (voisin2 != null)
						throw new Exception("Il y a une couille dans l'algo.");
					/**
					 * 5.2) Le voisin2 est null, sinon c'est qu'il y a un soucis de construction.<br>
					 * On calcul l'angle entre le voisin1 et le potentiel voisin2.<br>
					 * Si la valeur absolu de l'angle est > à 45° alors le voisin doit bien appartenir à le côté opposé
					 * de la ligne et on indique aux gares qu'elles sont voisines.
					 */
					Gare gareVoisine = gos.get(voisin1);
					Vecteur vect1 = new Vecteur(gareElt.getLatitude(), gareElt.getLongitude(),
							gareVoisine.getLatitude(), gareVoisine.getLongitude()), vect2 = new Vecteur(
							gareElt.getLatitude(), gareElt.getLongitude(), voisinPotentielle.getLatitude(),
							voisinPotentielle.getLongitude());
					if (Math.abs(vect1.angle(vect2)) > angleMinimumOpposition) {
						if (ajoute(voisins, gareElt.getNom(), voisinPotentielle.getNom(), true))
							aTraiter.remove((Object) voisinPotentielle.getNom());
					}
				}
			}
		}
		return goodOrder(gos, voisins);
	}

	private static Collection<Gare> goodOrder(Map<String, Gare> gos, Map<String, Voisin> voisins) throws Exception {
		List<Gare> gares = new ArrayList<Gare>();

		Gare debut = null;
		for (Gare go : gos.values())
			if (voisins.get(go.getNom()).voisin2 == null) {
				debut = go;
				break;
			}
		if (debut == null)
			throw new Exception("Impossible de trouver un début de ligne");

		while (debut != null) {
			gares.add(debut);
			if (gares.contains(gos.get(voisins.get(debut.getNom()).voisin1)))
				debut = gos.get(voisins.get(debut.getNom()).voisin2);
			else
				debut = gos.get(voisins.get(debut.getNom()).voisin1);
		}
		return gares;
	}
}
