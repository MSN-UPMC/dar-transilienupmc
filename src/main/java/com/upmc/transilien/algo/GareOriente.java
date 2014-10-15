package com.upmc.transilien.algo;

import com.upmc.transilien.v1.model.Gare;

/**
 * Une gare orienté, viendra surement remplacer le modèle des gares initiales
 * 
 * @author Kevin Coquart &amp; Mag-Stellon Nadarajah
 *
 */
public class GareOriente extends Gare {
	private GareOriente voisin1, voisin2;

	/**
	 * Constructeur à partir d'une gare
	 * 
	 * @param g
	 *            une gare classique
	 */
	public GareOriente(Gare g) {
		super(g.getNom(), g.getCodeUIC(), g.getLongitude(), g.getLatitude());
		setVoisin1(setVoisin2(null));
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
	public boolean ajoute(GareOriente go) throws Exception {
		boolean result = false;

		if (getVoisin1() == null)
			setVoisin1(go);
		else if (getVoisin2() == null) {
			setVoisin2(go);
			result = true;
		} else
			throw new Exception(
					"Les 2 gares sont déjà rempli, qu'est ce que t'as foutu sur l'algo ...");

		return result;
	}

	public GareOriente getVoisin1() {
		return voisin1;
	}

	public void setVoisin1(GareOriente voisin1) {
		this.voisin1 = voisin1;
	}

	public GareOriente getVoisin2() {
		return voisin2;
	}

	public GareOriente setVoisin2(GareOriente voisin2) {
		this.voisin2 = voisin2;
		return voisin2;
	}
}
