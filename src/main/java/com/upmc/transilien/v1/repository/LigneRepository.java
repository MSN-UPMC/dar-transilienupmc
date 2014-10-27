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
	 * recherche la ligne via son nom
	 * 
	 * @param nom
	 *            le nom de la ligne
	 * @return la ligne qui correspond au nom donné
	 */
	public Ligne findLigneByName(String nom) {
		List<Ligne> tmp = ofy().load().type(Ligne.class).filter("nom", nom).list();
		return tmp == null ? null : tmp.get(0);
	}

	/**
	 * Recherche les gares de la ligne
	 * 
	 * @param nomLigne
	 *            le nom de la ligne.<br>
	 *            <ul>
	 *            <li><i>Train :</i>H / J / K / L / N / P / R / U</li>
	 *            <li><i>RER :</i>A / B / C / D / E</li>
	 *            </ul>
	 * @return les gares de la ligne choisie
	 */
	public List<Gare> findGarePerLigne(String nomLigne) {
		List<Gare> gares;
		List<Ligne> lignes = ofy().load().type(Ligne.class).filter("nom = ", nomLigne).list();
		Ligne ligne = (lignes.isEmpty() ? null : lignes.get(0));
		if (ligne == null)
			gares = null;
		else {
			gares = new ArrayList<Gare>();
			for (Integer codeUIC : ligne.getGares()) {
				if (codeUIC == null)
					continue;
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
		List<Integer> tCode = GareRepository.getInstance().findGareByCode(codeUIC).getCodesUIC();

		List<Ligne> lignes = ofy().load().type(Ligne.class).list();
		List<Ligne> returnList = new ArrayList<Ligne>();
		for (Ligne l : lignes) {
			List<Integer> lGares = l.getGares();
			for (int i : tCode)
				if (lGares.contains(i)) {
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
		if (ofy().load().type(Ligne.class).filter("nom =", ligne.getNom()).list().isEmpty())
			ofy().save().entity(ligne).now();
		else
			throw new Error("Une ligne possède déjà ce nom.");
		return ligne;
	}

	/**
	 * Modifie la ligne dans le système de persistance.
	 * 
	 * @param editedLigne
	 *            la ligne modifié
	 * @return la ligne original modifié
	 */
	public Ligne updateGare(Ligne ligne, List<Integer> gares) {
		ligne.setGares(gares);
		ofy().save().entity(ligne).now();
		return ligne;
	}
}
