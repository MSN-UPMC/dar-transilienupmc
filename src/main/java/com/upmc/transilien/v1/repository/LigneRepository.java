package com.upmc.transilien.v1.repository;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.objectify.ObjectifyService;
import com.upmc.transilien.v1.model.Gare;
import com.upmc.transilien.v1.model.Ligne;

/**
 * Répertorie les lignes.<br>
 * <b>Singleton</b>
 * 
 * @author Kevin Coquart &amp; Mag-Stellon Nadarajah
 *
 */
public class LigneRepository {
	private static LigneRepository lignes = null;

	static {
		ObjectifyService.register(Ligne.class);
		ObjectifyService.register(Gare.class);
	}

	private LigneRepository() {
	}

	/**
	 * @return le singleton
	 */
	public static synchronized LigneRepository getInstance() {
		if (null == lignes) {
			lignes = new LigneRepository();
		}
		return lignes;
	}

	/**
	 * @return les lignes existantes
	 */
	public List<Ligne> findLigne() {
		return ofy().load().type(Ligne.class).list();
	}

	/**
	 * Recherche les gares de la ligne
	 * 
	 * @param ligne
	 *            le nom de la ligne.<br>
	 *            <ul>
	 *            <li><i>Train :</i>H / J / K / L / N / P / R / U</li>
	 *            <li><i>RER :</i>A / B / C / D / E</li>
	 *            </ul>
	 * @return les gares de la ligne choisie
	 */
	public List<Gare> findGarePerLigne(String ligne) {
		List<Gare> gares;
		List<Ligne> lignes = ofy().load().type(Ligne.class).filter("nom = ", ligne).list();
		if (ligne.isEmpty())
			gares = null;
		else {
			gares = new ArrayList<Gare>();
			for (Integer codeUIC : lignes.get(0).getGares()) {
				Gare gare = GareRepository.getInstance().findGareByCode(codeUIC);
				if (gare != null)
					gares.add(gare);
				else
					throw new Error("La gare de code " + codeUIC + " n'existe pas ...");
			}
		}
		return gares;
	}

	/**
	 * Recherche les lignes qui passent par la gare
	 * 
	 * @param codeUIC
	 *            le codeUIC de la gare
	 * @return la liste des lignes qui passent par la gare
	 */
	public List<Ligne> findLignePerGare(int codeUIC) {
		List<Ligne> lignes = ofy().load().type(Ligne.class).list();
		List<Ligne> returnList = new ArrayList<Ligne>();
		for (Ligne l : lignes) {
			if (l.getGares().contains(codeUIC)) {
				returnList.add(l);
			}
		}
		return returnList;
	}

	/**
	 * Créer la ligne dans le système de persistance
	 * 
	 * @param ligne
	 *            la ligne à sauvegarder
	 * @return la ligne
	 */
	public Ligne create(Ligne ligne) {
		if (ofy().load().type(Ligne.class).filter("codeUIC =", ligne.getNom()).list().isEmpty())
			ofy().save().entity(ligne).now();
		else
			throw new Error("Une ligne possède déjà ce nom.");
		return ligne;
	}
}
