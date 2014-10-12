package com.upmc.transilien.v1.repository;

import java.util.Collection;
import java.util.List;

import com.googlecode.objectify.ObjectifyService;
import com.upmc.transilien.algo.ItineraireDijkstra;
import com.upmc.transilien.parse.XMLToObject;
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
		return XMLToObject.parseTrain(TransilienRequest.prochainDepart(codeUIC));
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
		return XMLToObject.parseTrain(TransilienRequest.prochainDepart(departUIC, destinationUIC));
	}

	/**
	 * Calcule l'itinéraire entre deux gare
	 * 
	 * @param departUIC
	 *            le codeUIC de la gare de départ
	 * @param destinationUIC
	 *            le codeUIC de la gare de destination
	 * @return TODO à faire
	 */
	public List<Gare> itineraire(int departUIC, int destinationUIC) {
		ItineraireDijkstra itiDijk = new ItineraireDijkstra(departUIC, destinationUIC);
		itiDijk.execute();
		return itiDijk.getPredecesseur();
	}
}
