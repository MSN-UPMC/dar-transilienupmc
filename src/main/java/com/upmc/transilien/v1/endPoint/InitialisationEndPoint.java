package com.upmc.transilien.v1.endPoint;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.json.simple.parser.ParseException;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Text;
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
	public void init() {
		// TODO intégré le comportement directement une fois uniformisé et fini
		loadGare();
		loadLigne();
		filtreGareNonTransilien();

		for (Ligne ligne : LigneRepository.getInstance().findLigne()) {
			try {
				List<List<Gare>> gares;
				gares = LigneOriente.execute(ligne);
				List<List<Integer>> llCode = new ArrayList<List<Integer>>();
				for (List<Gare> lGare : gares) {
					List<Integer> lCodeUIC = new ArrayList<Integer>();
					for (Gare gare : lGare)
						lCodeUIC.add(gare.getCodeUIC());
					llCode.add(lCodeUIC);
				}
				ligne.setGares(llCode);
				LigneRepository.getInstance().update(ligne);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
			}
		}
	}

	/**
	 * Nettoie la dataStore
	 */
	@ApiMethod(name = "clean", httpMethod = ApiMethod.HttpMethod.GET, path = "clean")
	public void clean() {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

		Query mydeleteq = new Query();
		PreparedQuery pq = datastore.prepare(mydeleteq);
		for (Entity result : pq.asIterable()) {
			datastore.delete(result.getKey());
		}
	}

	/**
	 * Charge les gares depuis un fichier statique présent sur le serveur.<br>
	 * La fonction est voué à disparaître pour fonctionner de manière transparente à l'initialisation du serveur.
	 * 
	 * @return OK si cela c'est bien passé, le message d'erreur sinon
	 * @throws Exception
	 */
	@ApiMethod(name = "loadGare", httpMethod = ApiMethod.HttpMethod.GET, path = "loadGare")
	public Text loadGare() {
		if (GareRepository.getInstance().findGares().isEmpty())
			try {
				JsonToObject.loadGare("ressources/sncf-gares-et-arrets-transilien-ile-de-france.json");
				return new Text("OK");
			} catch (Exception e) {
				return new Text(System.getProperties().get("user.dir") + "     ->     " + e);
			}
		else
			return new Text("Deja fait.");
	}

	/**
	 * Charge les lignes existantes depuis un fichier JSON statique présent sur le serveur
	 * 
	 * @return OK ou un message d'erreur
	 */
	@ApiMethod(name = "loadLigne", httpMethod = ApiMethod.HttpMethod.GET, path = "loadLigne")
	public Text loadLigne() {
		if (LigneRepository.getInstance().findLigne().isEmpty())
			try {
				JsonToObject.loadLigne("ressources/sncf-lignes-par-gares-idf.json");
				return new Text("OK");
			} catch (IOException | ParseException e) {
				return new Text(System.getProperties().get("user.dir") + "\n" + e.getMessage());
			} catch (Exception e) {
				return new Text("Erreur surement dut à l'orientation des lignes.");
			}
		else
			return new Text("Deja fait.");
	}

	/**
	 * Charge les lignes existantes depuis un fichier JSON statique présent sur le serveur
	 * 
	 * @return OK ou un message d'erreur
	 */
	@ApiMethod(name = "filtreGareNonTransilien", httpMethod = ApiMethod.HttpMethod.GET, path = "filtreGareNonTransilien")
	public void filtreGareNonTransilien() {
		Initialisation.filtreGareNonTransilien();
	}
}