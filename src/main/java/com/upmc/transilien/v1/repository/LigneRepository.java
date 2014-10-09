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
 * @author Kevin Coquart && Mag-Stellon Nadarajah
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

	// public Ligne update(Ligne editedLigne) {
	// if (editedLigne.getId() == null) {
	// return null;
	// }
	//
	// Ligne ligne = ofy().load().key(Key.create(Ligne.class, editedLigne.getId())).now();
	//
	// for (Integer i : editedLigne.getGares())
	// ligne.addGares(i);
	//
	// ofy().save().entity(ligne).now();
	// return ligne;
	// }

	// TODO pareil, pourquoi supprimer une ligne ???
	// public void remove(Long id) {
	// if (id == null) {
	// return;
	// }
	// ofy().delete().type(Ligne.class).id(id).now();
	// }
}
