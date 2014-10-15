package com.upmc.transilien.algo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import com.upmc.transilien.tools.math.Vecteur;
import com.upmc.transilien.v1.model.Ligne;
import com.upmc.transilien.v1.repository.GareRepository;

// TODO attention au textes des messages d'erreurs
/**
 * Calcule les lignes et les gares sous forme de graphe orienté dans le but de
 * tracer les lignes sur la carte et d'optimisé notre calcule d'itinéaire.
 * 
 * @author Kevin Coquart &amp; Mag-Stellon Nadarajah
 *
 */
public class LigneOriente {
	private static int angleMinimumOpposition = 45;

	/**
	 * Couple pour répresenter le codeUIC de la gare et la distance par rapport
	 * à la gare de référence.
	 * 
	 * @author Kevin Coquart &amp; Mag-Stellon Nadarajah
	 *
	 */
	static class Couple {
		int codeUIC;
		double distance;

		/**
		 * Constructeur
		 * 
		 * @param codeUIC
		 *            le codeUIC de la gare
		 * @param distance
		 *            la distance par rapport à la gare de référence
		 */
		Couple(int codeUIC, double distance) {
			this.codeUIC = codeUIC;
			this.distance = distance;
		}
	}

	/**
	 * Execute l'algorithme qui calcule des gares orientées.
	 * 
	 * @param ligne
	 * @throws Exception
	 */
	public static Collection<GareOriente> execute(Ligne ligne) throws Exception {
		/**
		 * 1) On ajoute à une map toutes les gares de la ligne
		 */
		Map<Integer, GareOriente> gos = new HashMap<Integer, GareOriente>();

		List<Integer> aTraiter = new ArrayList<Integer>(ligne.getGares());
		for (Integer ite : aTraiter)
			gos.put(ite, new GareOriente(GareRepository.getInstance()
					.findGareByCode(ite)));

		/**
		 * On traite chaque gare de la ligne
		 */
		int i = 1000;
		while (!aTraiter.isEmpty() && i-- > 0) {
			Integer elt = aTraiter.remove(0);
			GareOriente gareElt = gos.get(elt);
			/*
			 * Petit test pour s'assurer que la gare existe bien dans la base
			 * des gares.
			 */
			if (gareElt == null)
				throw new Exception("La gare " + elt
						+ " n'existe pas ou plus 'voir les doublons.");

			/**
			 * 3) Création d'une file de priorité basé sur des couples <codeUIC,
			 * distance / la gare traité>
			 */
			PriorityQueue<Couple> distances = new PriorityQueue<Couple>(5,
					new Comparator<Couple>() {
						public int compare(Couple c1, Couple c2) {
							return Double.compare(c1.distance, c2.distance);
						}
					});

			/**
			 * 4) Calcule des distances de toutes les gares non traité / à la
			 * gare de réf
			 */
			for (Integer ite : aTraiter)
				distances.add(new Couple(ite, gareElt.calcule(gos.get(ite))));

			/**
			 * 5) Tant que la file n'est pas vide, on recherche les voisins pour
			 * les ajouters.
			 */
			while (!distances.isEmpty()
					&& (gareElt.getVoisin1() == null || gareElt.getVoisin2() == null)) {
				Couple c = distances.poll();
				GareOriente tmp = gos.get(c.codeUIC);

				/**
				 * 5.1) Le voisin1 est null, le voisin2 aussi par construction.<br>
				 * On indique aux gares qu'elles sont voisines.
				 */
				if (gareElt.getVoisin1() == null) {
					gareElt.setVoisin1(tmp);
					if (tmp.ajoute(gareElt))
						aTraiter.remove((Object) tmp.getCodeUIC());
				} else {
					if (gareElt.getVoisin2() != null)
						throw new Exception("Il y a une couille dans l'algo.");
					/**
					 * 5.2) Le voisin2 est null, sinon c'est qu'il y a un soucis
					 * de construction.<br>
					 * On calcul l'angle entre le voisin1 et le potentiel
					 * voisin2.<br>
					 * Si la valeur absolu de l'angle est > à 45° alors le
					 * voisin doit bien appartenir à le côté opposé de la ligne
					 * et on indique aux gares qu'elles sont voisines.
					 */
					Vecteur v1 = new Vecteur(gareElt.x(), gareElt.y(), gareElt
							.getVoisin1().x(), gareElt.getVoisin1().y()), v2 = new Vecteur(
							gareElt.x(), gareElt.y(), tmp.x(), tmp.y());
					if (Math.abs(v1.angle(v2)) > angleMinimumOpposition) {
						gareElt.setVoisin2(tmp);
						if (tmp.ajoute(gareElt))
							aTraiter.remove((Object) tmp.getCodeUIC());
					}
				}
			}
		}
		return gos.values();
	}
}
