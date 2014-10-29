package com.upmc.transilien.v1.endPoint;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.parser.ParseException;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.Named;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.upmc.transilien.algo.Initialisation;
import com.upmc.transilien.algo.LigneOriente;
import com.upmc.transilien.parse.JsonToObject;
import com.upmc.transilien.v1.model.Gare;
import com.upmc.transilien.v1.model.Ligne;
import com.upmc.transilien.v1.repository.GareRepository;
import com.upmc.transilien.v1.repository.LigneRepository;

/**
 * EndPoint d'Initialisation
 * 
 * @author Kevin Coquart &amp; Mag-Stellon Nadarajah
 *
 */
@Api(name = "initialisation", version = "v1")
public class InitialisationEndPoint {
	/**
	 * Initialise le systèmes
	 */
	@ApiMethod(name = "init", httpMethod = ApiMethod.HttpMethod.GET, path = "init")
	public Map<String, Object> init(@Named("Mot de passe") String password) {
		Map<String, Object> result = new HashMap<String, Object>();
		if (password != null && password.equals("kms@transilien-upmc")) {
			try {
				if (GareRepository.getInstance().findGares().isEmpty()) {
					// Load Gare
					JsonToObject.loadGare("ressources/sncf-gares-et-arrets-transilien-ile-de-france.json");

					// Load ligne
					JsonToObject.loadLigne("ressources/sncf-lignes-par-gares-idf.json");

					// Filtre gare
					Initialisation.filtreGareNonTransilien();

					// Oriente les gares
					for (Ligne ligne : LigneRepository.getInstance().findLigne()) {
						try {
							List<Gare> gares;
							gares = LigneOriente.execute(ligne);
							List<Integer> lCodeUIC = new ArrayList<Integer>();
							for (Gare gare : gares)
								lCodeUIC.add(gare != null ? gare.getCodeUIC() : null);

							LigneRepository.getInstance().updateGare(ligne, lCodeUIC);
						} catch (Exception e) {
							e.printStackTrace();
							result.put("error", e.getMessage());
							return result;
						}
					}
					result.put("text", "OK");
				} else {
					result.put("text", "Déjà fait.");
				}
			} catch (IOException | ParseException e) {
				result.put("error", System.getProperties().get("user.dir") + "\n" + e.getMessage());
			} catch (Exception e) {
				result.put("error", System.getProperties().get("user.dir") + "\n" + e.getMessage());
			}
		} else {
			result.put("error", "Mot de passe incorecte.");
		}
		return result;
	}

	/**
	 * Nettoie la dataStore
	 */
	@ApiMethod(name = "clean", httpMethod = ApiMethod.HttpMethod.GET, path = "clean")
	public Map<String, Object> clean(@Named("Mot de passe") String password) {
		Map<String, Object> retour = new HashMap<String, Object>();
		if (password != null && password.equals("kms@transilien-upmc")) {
			DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

			Query mydeleteq = new Query();
			PreparedQuery pq = datastore.prepare(mydeleteq);
			for (Entity result : pq.asIterable()) {
				datastore.delete(result.getKey());
			}
			retour.put("text", "OK");
		} else {
			retour.put("error", "Mot de passe incorecte.");
		}
		return retour;
	}
}