package com.upmc.transilien.algo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import com.upmc.transilien.v1.model.Gare;
import com.upmc.transilien.v1.model.Ligne;
import com.upmc.transilien.v1.repository.GareRepository;
import com.upmc.transilien.v1.repository.LigneRepository;

/**
 * Algorithme Dijkstra qui calcule les itinéraires pour se rendre d'une gare A à une gare B.<br>
 * L'API transilien ce limite au gare sur la même ligne, nous allons plus loin et proposons un itinéraire optimisé multi-ligne.<br>
 * <br>
 * Le manque d'info sur la cartographie des lignes ne nous permet pas pour le moment de créer un algorithme optimisé, on ne se limite pas au gare voisine mais
 * on essai avec chaque gare de la ligne.
 * 
 * @author Kevin Coquart &amp; Mag-Stellon Nadarajah
 *
 */
public class ItineraireDijkstra {
	/**
	 * Classe annexe qui permet de mettre une surcouche à une gare.<br>
	 * La surcouche contient un Double qui est la distance de la gare par rapport à celle de départ
	 * 
	 * @author Kevin Coquart &amp; Mag-Stellon Nadarajah
	 *
	 */
	class GareDijkstra implements Comparable<GareDijkstra> {
		private Double distance;
		private Gare gare;

		/**
		 * Constructeur
		 * 
		 * @param gare
		 *            la gare
		 * @param distance
		 *            la distance par rapport à la gare de départ
		 */
		public GareDijkstra(Gare gare, Double distance) {
			this.gare = gare;
			this.distance = distance;
		}

		@Override
		public int compareTo(GareDijkstra o) {
			return distance.compareTo(o.distance);
		}

		/**
		 * less than
		 * 
		 * @param otherDistance
		 *            une autre distance
		 * @return vrai si la valeur actuelle est plus petite que la nouvelle
		 */
		public boolean lt(Double otherDistance) {
			return distance <= otherDistance;
		}
	}

	private Map<GareDijkstra, GareDijkstra> predecesseur;
	private Map<Integer, GareDijkstra> mapGare;
	private java.util.Queue<GareDijkstra> tas;
	private GareDijkstra depart;
	private GareDijkstra destination;

	/**
	 * Constructeur tout simple
	 * 
	 * @param departUIC
	 *            le codeUIC de la gare de départ
	 * @param destinationUIC
	 *            le codeUIC de la gare de destination
	 */
	public ItineraireDijkstra(int departUIC, int destinationUIC) {
		super();
		depart = new GareDijkstra(GareRepository.getInstance().findGareByCode(departUIC), 0.);
		destination = new GareDijkstra(GareRepository.getInstance().findGareByCode(destinationUIC), Double.MAX_VALUE);

		tas = new PriorityQueue<GareDijkstra>(5, new Comparator<GareDijkstra>() {
			public int compare(GareDijkstra gare1, GareDijkstra gare2) {
				return Double.compare(gare1.distance, gare2.distance);
			}
		});
		tas.add(depart);

		predecesseur = new HashMap<GareDijkstra, GareDijkstra>();
		predecesseur.put(depart, null);

		mapGare = new HashMap<Integer, GareDijkstra>();
		mapGare.put(departUIC, depart);
		mapGare.put(destinationUIC, destination);
	}

	/**
	 * Execute l'algorithme Dijkstra.
	 */
	public void execute() {
		/** 1) Recherche d'un directe */
		List<Ligne> testInitial = LigneRepository.getInstance().findLignePerGare(depart.gare.getCodeUIC());
		for (Ligne ligne : testInitial) {
			List<Gare> lGare = LigneRepository.getInstance().findGarePerLigne(ligne.getNom());
			if (lGare.contains(destination.gare)) {
				predecesseur.put(destination, depart);
				destination.distance = depart.gare.calcule(destination.gare);
				return;
			}
		}

		/** 2) Parcours de toutes les gares */
		// Tant qu'il y a des éléments donc la distance est inférieur à celle de la destination
		while (!tas.isEmpty() && !(destination.lt(tas.peek().distance))) {
			GareDijkstra gareDijk = tas.poll();

			/** 2.1) On itère sur les lignes qui passent par la gare */
			List<Ligne> lignes = LigneRepository.getInstance().findLignePerGare(gareDijk.gare.getCodeUIC());
			for (Ligne ligne : lignes) {

				/** 2.2) On itère sur les gares de chaque lignes */
				List<Gare> lGare = LigneRepository.getInstance().findGarePerLigne(ligne.getNom());
				for (Gare g : lGare) {
					GareDijkstra tmp = mapGare.get(g.getCodeUIC());
					if (tmp == null && g.getCodeUIC2() != null)
						tmp = mapGare.get(g.getCodeUIC2());
					double newDist = gareDijk.gare.calcule(g) + gareDijk.distance;

					/**
					 * 3 cas possibles :
					 * <ul>
					 * <li>La gare n'existe pas dans la map, c'est la premiere fois qu'on la traite. On créer une nouvelle gareDijkstra et l'ajoute à la map.</li>
					 * <li>La gare existe dans la map et la distance est inférieur à celle calculé, on conserve la gare en l'état et on passe à la suivante.</li>
					 * <li>La gare existe mais la distance est amélioré via le nouveau chemin, on met à jour la distance.</li>
					 * </ul>
					 * <p>
					 * Par la suite, en dehors du cas n°2 ou l'on passe à la gare suivante, on ajoute / modifie le prédécesseur pour coller au chemin courant,
					 * on actualise aussi le tas pour y ajouter la gare
					 * </p>
					 */
					if (tmp == null) {
						// 1ere fois qu'on traite la gare
						tmp = new GareDijkstra(g, newDist);
						mapGare.put(g.getCodeUIC(), tmp);
					} else if (tmp.lt(newDist)) {
						// La gare a déjà été parcouru et sa distance est meilleurs.
						continue;
					} else {
						// Sinon
						tmp.distance = newDist;
					}

					predecesseur.put(tmp, gareDijk);

					tas.remove(tmp);
					tas.add(tmp);
				}
			}
		}
	}

	/**
	 * @return la distance minimal entre la gare de départ et la destination
	 */
	public double getDistance() {
		return destination.distance;
	}

	/**
	 * @return la liste des gares de passage du départ vers l'arrivée.
	 */
	public List<Gare> getPredecesseur() {
		List<Gare> pred = new ArrayList<Gare>();
		GareDijkstra tmp = destination;

		while (tmp != null) {
			pred.add(tmp.gare);
			tmp = predecesseur.get(tmp);
		}

		Collections.reverse(pred);
		return pred;
	}
}
