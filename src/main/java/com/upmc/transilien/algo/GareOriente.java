package com.upmc.transilien.algo;

import com.upmc.transilien.v1.model.Gare;

/**
 * Une gare orienté, viendra surement remplacer le modèle des gares initiales
 * 
 * @author Kevin Coquart &amp; Mag-Stellon Nadarajah
 *
 */
public class GareOriente extends Gare {
	private Integer voisin1, voisin2;

	/**
	 * Constructeur à partir d'une gare
	 * 
	 * @param g
	 *            une gare classique
	 */
	public GareOriente(Gare g) {
		super(g.getNom(), g.getCodeUIC(), g.getLongitude(), g.getLatitude());
		voisin1 = voisin2 = null;
	}

	/**
	 * @return simplification pour les vecteurs
	 */
	public Double x() {
		return getLatitude();
	}

	/**
	 * @return simplification pour les vecteurs
	 */
	public Double y() {
		return getLongitude();
	}

	/**
	 * ajoute un voisin en commencant par le voisin1.
	 * 
	 * @param go
	 *            la gare voisine ajoutée
	 * @return vrai si les 2 gares sont remplis
	 * @throws Exception
	 *             si les 2 gares sont rempli avant l'appel à cette fonction
	 */
	public boolean ajoute(GareOriente go, boolean recursion) throws Exception {
		boolean result = false;

		if (voisin1 == null)
			voisin1 = go.getCodeUIC();
		else if (voisin2 == null) {
			voisin2 = go.getCodeUIC();
			result = true;
		} else
			throw new Exception(
					"Les 2 gares sont déjà rempli, qu'est ce que t'as foutu sur l'algo ...");

		if (recursion) {
			result = go.ajoute(this, false);
		}
		return result;
	}

	public Integer getVoisin1() {
		return voisin1;
	}

	public Integer getVoisin2() {
		return voisin2;
	}
}
