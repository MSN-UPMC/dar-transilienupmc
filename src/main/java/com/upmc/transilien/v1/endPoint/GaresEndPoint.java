package com.upmc.transilien.v1.endPoint;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.json.simple.parser.ParseException;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.Named;
import com.google.appengine.api.datastore.Text;
import com.upmc.transilien.parse.JsonToObject;
import com.upmc.transilien.v1.model.Gare;
import com.upmc.transilien.v1.repository.GareRepository;

/**
 * EndPoint des Gares
 * 
 * @author Kevin Coquart &amp; Mag-Stellon Nadarajah
 *
 */
@Api(name = "gares", version = "v1")
public class GaresEndPoint {
	/**
	 * @return toutes les gares existantes
	 */
	@ApiMethod(name = "getGares", httpMethod = ApiMethod.HttpMethod.GET, path = "getGares")
	public Collection<Gare> getGares() {
		return GareRepository.getInstance().findGares();
	}

	/**
	 * Recherche une gare par son codeUIC
	 * 
	 * @param codeUIC
	 *            le codeUIC d'une gare
	 * @return la gare ou null
	 */
	@ApiMethod(name = "getGareByCode", httpMethod = ApiMethod.HttpMethod.GET, path = "getGareByCode")
	public Gare getGareByCode(@Named("codeUIC") Integer codeUIC) {
		return GareRepository.getInstance().findGareByCode(codeUIC);
	}

	/**
	 * Recherche une gare par son nom
	 * 
	 * @param nom
	 *            le nom de la gare
	 * @return la gare ou null
	 */
	@ApiMethod(name = "getGareByName", httpMethod = ApiMethod.HttpMethod.GET, path = "getGareByName")
	public Gare getGareByName(@Named("nom") String nom) {
		return GareRepository.getInstance().findGareByName(nom);
	}

	/**
	 * @return le nom de toutes les gares disponibles
	 */
	@ApiMethod(name = "getGareName", httpMethod = ApiMethod.HttpMethod.GET, path = "getGareName")
	public List<String> getGareName() {
		return GareRepository.getInstance().findGaresName();
	}

	// /**
	// * @return les gares en double
	// */
	// @ApiMethod(name = "doublon", httpMethod = ApiMethod.HttpMethod.GET, path
	// = "doublon")
	// public Collection<Gare> doublon() {
	// Collection<Gare> gares = GareRepository.getInstance().findGares();
	// List<Gare> retour = new ArrayList<Gare>();
	// List<String> vu = new ArrayList<String>();
	// for (Gare g : gares) {
	// if (vu.contains(g.getNom()))
	// retour.add(g);
	// else
	// vu.add(g.getNom());
	// }
	// return retour;
	// }

	/**
	 * Charge les gares depuis un fichier statique présent sur le serveur.<br>
	 * La fonction est voué à disparaître pour fonctionner de manière
	 * transparente à l'initialisation du serveur.
	 * 
	 * @return OK si cela c'est bien passé, le message d'erreur sinon
	 * @throws Exception
	 */
	@ApiMethod(name = "loadGare", httpMethod = ApiMethod.HttpMethod.POST, path = "loadGare")
	public Text loadGare() {
		if (GareRepository.getInstance().findGares().isEmpty())
			try {
				JsonToObject
						.loadGare("ressources/sncf-gares-et-arrets-transilien-ile-de-france.json");
				return new Text("OK");
			} catch (IOException | ParseException e) {
				return new Text(System.getProperties().get("user.dir") + "\n"
						+ e.getMessage());
			} catch (Exception e) {
				return new Text(System.getProperties().get("user.dir") + "\n"
						+ e.getMessage());
			}
		else
			return new Text("Deja fait.");
	}
}