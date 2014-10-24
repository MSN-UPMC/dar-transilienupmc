package com.upmc.transilien.v1.endPoint;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.Named;
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
		List<Gare> listeGares = new ArrayList<Gare>(GareRepository.getInstance().findGares());
		Collections.sort(listeGares);
		return listeGares;
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
}