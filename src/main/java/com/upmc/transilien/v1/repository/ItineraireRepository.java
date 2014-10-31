package com.upmc.transilien.v1.repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.googlecode.objectify.ObjectifyService;
import com.upmc.transilien.algo.ItineraireDijkstra;
import com.upmc.transilien.request.TransilienRequest;
import com.upmc.transilien.v1.model.Gare;
import com.upmc.transilien.v1.model.Ligne;
import com.upmc.transilien.v1.model.Train;

/**
 * Gère la création d'itinéraire<br>
 * <b>Singleton</b>
 * 
 * @author Kevin Coquart &amp; Mag-Stellon Nadarajah
 *
 */
public class ItineraireRepository {
	private static ItineraireRepository itineraire = null;

	static {
		ObjectifyService.register(Ligne.class);
		ObjectifyService.register(Train.class);
		ObjectifyService.register(Gare.class);
	}

	private ItineraireRepository() {
	}

	/**
	 * 
	 * @return le singleton
	 */
	public static synchronized ItineraireRepository getInstance() {
		if (null == itineraire) {
			itineraire = new ItineraireRepository();
		}
		return itineraire;
	}

	/**
	 * Recherche les prochains départ depuis une gare
	 * 
	 * @param codeUIC
	 *            le codeUIC de la gare
	 * @return la liste des trains qui passent prochainement par la gare
	 * @throws Exception
	 */
	public Collection<Train> prochainDepart(int codeUIC) throws Exception {
		Gare g = GareRepository.getInstance().findGareByCode(codeUIC);
		List<Integer> tabCode = g != null ? g.getCodesUIC() : null;

		List<Train> trains = new ArrayList<Train>();
		for (int i : tabCode) {
			List<Train> tmp = TransilienRequest.prochainDepart(i);
			if (tmp != null && !tmp.isEmpty())
				trains.addAll(tmp);
		}

		return trains;
	}

	/**
	 * Recherche les prochains départ depuis une gare vers une autre.<br>
	 * L'API SNCF ne gère pas les changements, c'est à nous de réaliser un algorithme sous une autre fonction capable de trouver un itinéraire à partir du plan
	 * des lignes.
	 * 
	 * @param departUIC
	 *            le codeUIC de la gare de départ
	 * @param destinationUIC
	 *            le codeUIC de la gare de destination
	 * @return la liste des trains qui partent du départ en direction de la destination donné.
	 * @throws Exception
	 */
	public Collection<Train> prochainDepart(int departUIC, int destinationUIC) throws Exception {
		Gare gD = GareRepository.getInstance().findGareByCode(departUIC);
		Gare gA = GareRepository.getInstance().findGareByCode(destinationUIC);
		List<Integer> tDepartCode = gD != null ? gD.getCodesUIC() : null;
		List<Integer> tDestCode = gA != null ? gA.getCodesUIC() : null;

		List<Train> trains = new ArrayList<Train>();
		for (int i : tDepartCode)
			for (int j : tDestCode) {
				List<Train> tmp = TransilienRequest.prochainDepart(i, j);
				if (tmp != null && !tmp.isEmpty())
					trains.addAll(tmp);
			}
		return trains;
	}

	/**
	 * Calcule l'itinéraire entre deux gare
	 * 
	 * @param departUIC
	 *            le codeUIC de la gare de départ
	 * @param destinationUIC
	 *            le codeUIC de la gare de destination
	 * @return la liste des gares à parcourir pour arriver à bon port
	 */
	public Map<String, Object> itineraire(int departUIC, int destinationUIC) {
		/* Init */
		Map<String, Object> retour = new HashMap<String, Object>();
		List<Gare> gares = null;
		List<String> lignes = null;

		/* Recuperation des gares */
		Gare gD = GareRepository.getInstance().findGareByCode(departUIC);
		Gare gA = GareRepository.getInstance().findGareByCode(destinationUIC);
		if (gD != null && gA != null) {
			/* On calcul puis recupere les gares */
			ItineraireDijkstra itiDijk = new ItineraireDijkstra(departUIC, destinationUIC);
			itiDijk.execute();
			gares = itiDijk.getPredecesseur();

			/* On associe chaque correspondance à une ligne */
			lignes = new ArrayList<String>();
			for (int i = 0; i < gares.size() - 1; i++) {
				List<Ligne> tmp = LigneRepository.getInstance().findLignePerGare(gares.get(i).getCodeUIC());
				for (Ligne l : tmp) {
					Gare g2 = gares.get(i + 1);
					List<Integer> lCodes = l.getGares();
					if (lCodes.contains(g2.getCodeUIC()) || lCodes.contains(g2.getCodeUIC2())) {
						lignes.add(l.getNom());
						break;
					}
				}
			}
		}

		retour.put("gares", gares);
		retour.put("lignes", lignes);
		return retour;
	}
}
