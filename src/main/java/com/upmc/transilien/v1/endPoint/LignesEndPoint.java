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
import com.upmc.transilien.v1.model.Ligne;
import com.upmc.transilien.v1.repository.LigneRepository;

/**
 * EndPoint des Lignes
 * 
 * @author Kevin Coquart &amp; Mag-Stellon Nadarajah
 *
 */
@Api(name = "lignes", version = "v1")
public class LignesEndPoint {

	/**
	 * @return les lignes existantes
	 */
	@ApiMethod(name = "getLignes", httpMethod = ApiMethod.HttpMethod.GET, path = "getLignes")
	public Collection<Ligne> getLignes() {
		return LigneRepository.getInstance().findLigne();
	}

	/**
	 * Recherche les gares d'une ligne
	 * 
	 * @param ligne
	 *            le nom de la ligne.<br>
	 *            <ul>
	 *            <li><i>Train :</i>H / J / K / L / N / P / R / U</li>
	 *            <li><i>RER :</i>A / B / C / D / E</li>
	 *            </ul>
	 * @return les gares de la ligne choisie
	 */
	@ApiMethod(name = "getGareByLigne", httpMethod = ApiMethod.HttpMethod.GET, path = "getGareByLigne")
	public List<Gare> getGareByLigne(@Named("name") String ligne) {
		return LigneRepository.getInstance().findGarePerLigne(ligne);
	}

	/**
	 * Recherche les lignes qui passent par la gare
	 * 
	 * @param codeUIC
	 *            le codeUIC de la gare
	 * @return la liste des lignes qui passent par la gare
	 */
	@ApiMethod(name = "getLigneByGare", httpMethod = ApiMethod.HttpMethod.GET, path = "getLigneByGare")
	public List<Ligne> getLigneByGare(@Named("codeUIC") int codeUIC) {
		return LigneRepository.getInstance().findLignePerGare(codeUIC);
	}

	/**
	 * Charge les lignes existantes depuis un fichier JSON statique présent sur le serveur
	 * 
	 * @return OK ou un message d'erreur
	 */
	@ApiMethod(name = "loadLigne", httpMethod = ApiMethod.HttpMethod.POST, path = "loadLigne")
	public Text loadLigne() {
		if (LigneRepository.getInstance().findLigne().isEmpty())
			try {
				JsonToObject.loadLigne("ressources/sncf-lignes-par-gares-idf.json");
				return new Text("OK");
			} catch (IOException | ParseException e) {
				return new Text(System.getProperties().get("user.dir") + "\n" + e.getMessage());
			}
		else
			return new Text("Deja fait.");
	}
}
