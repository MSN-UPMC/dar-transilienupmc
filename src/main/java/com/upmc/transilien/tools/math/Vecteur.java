package com.upmc.transilien.tools.math;

/**
 * Vecteur mathématiques classiques
 * 
 * @author Kevin Coquart &amp; Mag-Stellon Nadarajah
 *
 */
public class Vecteur {
	private double x, y;

	/**
	 * Constructeur par coordonnées dans un rèpere 2D
	 * 
	 * @param xa
	 * @param ya
	 * @param xb
	 * @param yb
	 */
	public Vecteur(double xa, double ya, double xb, double yb) {
		x = xb - xa;
		y = yb - ya;
	}

	/**
	 * Calcul l'angle en degrè entre 2 vecteurs.
	 * 
	 * @param v2
	 *            le 2 ème vecteur
	 * @return l'angle en degrè qui separé le vecteur courant de celui donné en
	 *         argument
	 */
	public double angle(Vecteur v2) {
		double div = (norme() * v2.norme());
		return div == 0 ? 0 : Math.acos(produitScalaire(v2) / div) * 180
				/ Math.PI;
	}

	/**
	 * Calcul le produit scalaire entre les 2 vecteurs
	 * 
	 * @param v2
	 *            le 2ème vecteur
	 * @return le produit scalaire du vecteur courant . le vecteur argument
	 */
	public double produitScalaire(Vecteur v2) {
		return x * v2.x + y * v2.y;
	}

	/**
	 * Calcul la norme du vecteur
	 * 
	 * @return la norme du vecteur
	 */
	public double norme() {
		return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
	}

	// TODO effacer test
	// public static void main(String[] args) {
	// Vecteur v1 = new Vecteur(0, 0, 1, 0), v2 = new Vecteur(1, 1, 2, 2);
	// System.out.println(v1.norme());
	// System.out.println(v2.norme());
	// System.out.println(v1.angle(v1));
	// System.out.println(v1.angle(v2));
	// }
}
