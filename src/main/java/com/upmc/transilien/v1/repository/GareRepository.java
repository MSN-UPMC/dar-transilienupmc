package com.upmc.transilien.v1.repository;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.googlecode.objectify.ObjectifyService;
import com.upmc.transilien.v1.model.Gare;

/**
 * Répertorie les gares<br>
 * <b>Singleton</b>
 * 
 * @author Kevin Coquart &amp; Mag-Stellon Nadarajah
 *
 */
public class GareRepository {
	private static GareRepository gares = null;

	static {
		ObjectifyService.register(Gare.class);
	}

	private GareRepository() {
	}

	/**
	 * 
	 * @return le singleton
	 */
	public static synchronized GareRepository getInstance() {
		if (null == gares) {
			gares = new GareRepository();
		}
		return gares;
	}

	/**
	 * 
	 * @return toutes les gares disponibles
	 */
	public Collection<Gare> findGares() {
		return ofy().load().type(Gare.class).list();
	}

	/**
	 * Recherche une gare par son codeUIC
	 * 
	 * @param codeUIC
	 *            le codeUIC de la gare
	 * @return la gare
	 */
	public Gare findGareByCode(int codeUIC) {
		List<Gare> gares = ofy().load().type(Gare.class).filter("codeUIC =", codeUIC).list();
		return (gares.isEmpty() ? null : gares.get(0));
	}

	/**
	 * Recherche une gare par son nom
	 * 
	 * @param nom
	 *            son nom
	 * @return la gare
	 */
	public Gare findGareByName(String nom) {
		List<Gare> gares = ofy().load().type(Gare.class).filter("nom =", nom).list();
		return (gares.isEmpty() ? null : gares.get(0));
	}

	/**
	 * Recherche le nom de toutes les gares.<br>
	 * Facile la recherche d'une gare précise.
	 * 
	 * @return le noms de toutes les gares
	 */
	public List<String> findGaresName() {
		List<Gare> gares = ofy().load().type(Gare.class).order("nom").list();
		List<String> nomGares = new ArrayList<String>();

		for (Gare gare : gares) {
			nomGares.add(gare.getNom());
		}
		return nomGares;
	}

	/**
	 * Créer une gare dans le système de persistance
	 * 
	 * @param gare
	 *            la gare à sauvegardé
	 * @return la gare
	 */
	public Gare create(Gare gare) {
		if (ofy().load().type(Gare.class).filter("codeUIC =", gare.getCodeUIC()).list().isEmpty())
			ofy().save().entity(gare).now();
		else
			throw new Error("Une gare possède déjà ce numéro UIC.");
		return gare;
	}
}
