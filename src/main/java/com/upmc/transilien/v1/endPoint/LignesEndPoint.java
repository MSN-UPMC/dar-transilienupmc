package com.upmc.transilien.v1.endPoint;

import java.util.Collection;
import java.util.List;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.Named;
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

	// TODO supprimer inclus a init
	// @ApiMethod(name = "ligneOriente", httpMethod = ApiMethod.HttpMethod.GET, path = "ligneOriente")
	// public List<Gare> ligneOriente(@Named("nom de la ligne") String nomLigne) throws Exception {
	// Ligne ligne = LigneRepository.getInstance().findLigneByName(nomLigne);
	// return LigneOriente.execute(ligne);
	// }

	// TODO voir / tester / supprimer
	// @ApiMethod(name = "lO", httpMethod = ApiMethod.HttpMethod.GET, path = "lO")
	// public Text ligneO(@Named("nom de la ligne") String nomLigne) throws Exception {
	// Ligne ligne = LigneRepository.getInstance().findLigneByName(nomLigne);
	// List<Gare> lo = LigneOriente.execute(ligne);
	// String s = "";
	// for (Integer code : ligne.getGares()) {
	// Gare g = GareRepository.getInstance().findGareByCode(code);
	//
	// if (!lo.contains(g))
	// s += g.getNom();
	// }
	// return new Text("origine : " + ligne.getGares().size() + "         modif : " + lo.size() + (s.isEmpty() ? "" : "                          " + s));
	// }
}
