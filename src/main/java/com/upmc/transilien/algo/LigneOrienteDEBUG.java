//package com.upmc.transilien.algo;
//
//import java.io.FileReader;
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.Comparator;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.PriorityQueue;
//
//import org.json.simple.JSONArray;
//import org.json.simple.JSONObject;
//import org.json.simple.parser.JSONParser;
//
//import com.upmc.transilien.parse.JsonToObject;
//import com.upmc.transilien.parse.XMLToObject;
//import com.upmc.transilien.request.TransilienRequest;
//import com.upmc.transilien.tools.math.Vecteur;
//import com.upmc.transilien.v1.model.Gare;
//import com.upmc.transilien.v1.model.Ligne;
//import com.upmc.transilien.v1.repository.GareRepository;
//
//// TODO attention au textes des messages d'erreurs
///**
// * Calcule les lignes et les gares sous forme de graphe orienté dans le but de tracer les lignes sur la carte et d'optimisé notre calcule d'itinéaire.
// * 
// * @author Kevin Coquart &amp; Mag-Stellon Nadarajah
// *
// */
//public class LigneOrienteDEBUG {
//	private static int angleMinimumOpposition = 45;
//
//	/**
//	 * Couple pour representer le codeUIC de la gare et la distance par rapport à la gare de référence.
//	 * 
//	 * @author Kevin Coquart &amp; Mag-Stellon Nadarajah
//	 *
//	 */
//	static class Couple {
//		String nom;
//		double distance;
//
//		/**
//		 * Constructeur
//		 * 
//		 * @param nom
//		 *            le nom de la gare
//		 * @param distance
//		 *            la distance par rapport à la gare de référence
//		 */
//		Couple(String nom, double distance) {
//			this.nom = nom;
//			this.distance = distance;
//		}
//	}
//
//	/**
//	 * Sous classe pour representer les voisins lors de l'algo
//	 * 
//	 * @author Kevin Coquart &amp; Mag-Stellon Nadarajah
//	 *
//	 */
//	public static class Voisin {
//		private String voisin1;
//		private String voisin2;
//
//		public Voisin() {
//			voisin1 = null;
//			voisin2 = null;
//		}
//
//		@Override
//		public String toString() {
//			return "Voisin [voisin1=" + voisin1 + ", voisin2=" + voisin2 + "]";
//		}
//	}
//
//	/**
//	 * ajoute un voisin en commencent par le voisin1.
//	 * 
//	 * @param voisine
//	 *            la gare voisine ajoutée
//	 * @return vrai si les 2 gares sont remplis
//	 * @throws Exception
//	 *             si les 2 gares sont rempli avant l'appel à cette fonction
//	 */
//	private static boolean ajoute(Map<String, Voisin> voisins, String courante, String voisine, boolean recursion) throws Exception {
//		boolean result = false;
//		Voisin gC = voisins.get(courante);
//
//		if (gC.voisin1 == null)
//			gC.voisin1 = voisine;
//		else if (gC.voisin2 == null) {
//			gC.voisin2 = voisine;
//			result = true;
//		} else
//			throw new Exception("Les 2 voisins sont déjà rempli, qu'est ce que t'as foutu sur l'algo ...");
//
//		if (recursion) {
//			result = ajoute(voisins, voisine, courante, false);
//		}
//		System.out.println(result);
//		return result;
//	}
//
//	/**
//	 * Execute l'algorithme qui calcule des gares orientées.
//	 * 
//	 * @param ligne
//	 * @throws Exception
//	 */
//	public static Map<String, Voisin> execute(Map<Integer, Gare> gares, Ligne ligne) {
//		Map<String, Gare> gos = new HashMap<String, Gare>();
//		/**
//		 * 1) On ajoute à une map toutes les gares de la ligne
//		 */
//		List<Integer> garesLigne = new ArrayList<Integer>(ligne.getGares());
//		List<String> aTraiter = new ArrayList<String>();
//		for (Integer ite : garesLigne) {
//			// Gare gare = GareRepository.getInstance().findGareByCode(ite);
//			Gare gare = gares.get(ite);
//			aTraiter.add(gare.getNom());
//			gos.put(gare.getNom(), gare);
//		}
//
//		/**
//		 * 2) On associe a chaque gare un Voisin
//		 */
//		Map<String, Voisin> voisins = new HashMap<String, Voisin>();
//		for (String nomGare : gos.keySet())
//			voisins.put(nomGare, new Voisin());
//
//		/**
//		 * On traite chaque gare de la ligne
//		 */
//		try {
//			int i = 100;
//			while (!aTraiter.isEmpty() && i-- > 0) {
//				Gare gareElt = gos.get(aTraiter.remove(0));
//				System.out.println("########## Traitement de : " + gareElt.getNom() + "\t" + voisins.get(gareElt.getNom()));
//
//				/**
//				 * 3) Création d'une file de priorité basé sur des couples <codeUIC, distance / la gare traité>
//				 */
//				PriorityQueue<Couple> distances = new PriorityQueue<Couple>(5, new Comparator<Couple>() {
//					public int compare(Couple c1, Couple c2) {
//						return Double.compare(c1.distance, c2.distance);
//					}
//				});
//
//				/**
//				 * 4) Calcule des distances de toutes les gares non traité / à la gare de réf
//				 */
//				for (String nom : aTraiter)
//					distances.add(new Couple(nom, gareElt.calcule(gos.get(nom))));
//
//				/**
//				 * 5) Tant que la file n'est pas vide, on recherche les voisins pour les ajouter.<br>
//				 * Il ne faut pas non plus que la gare la plus proche soit à plus de 20 km, sinon c'est sûrement qu'on traite le bout d'une ligne.<br>
//				 * De même, si les voisins sont remplis, on arrête le traitement de celle-ci.
//				 */
//				Voisin voisin = voisins.get(gareElt.getNom());
//				while (!distances.isEmpty() && (distances.peek().distance < 20000) && (voisin.voisin1 == null || voisin.voisin2 == null)) {
//					System.out.println("Rappel : " + gareElt.getNom() + "\t" + voisins.get(gareElt.getNom()));
//					Couple c = distances.poll();
//					Gare voisinPotentielle = gos.get(c.nom);
//					System.out.println("Voisins poten : " + voisinPotentielle.getNom() + " à " + c.distance + "\t" + voisins.get(voisinPotentielle.getNom()));
//
//					/**
//					 * 5.1) Le voisin1 est null, le voisin2 aussi par construction.<br>
//					 * On indique aux gares qu'elles sont voisines.
//					 */
//					if (voisin.voisin1 == null) {
//						System.out.println("voisin 1");
//						if (ajoute(voisins, gareElt.getNom(), voisinPotentielle.getNom(), true))
//							aTraiter.remove(voisinPotentielle.getNom());
//					} else {
//						if (voisin.voisin2 != null)
//							throw new Exception("Il y a une couille dans l'algo.");
//						/**
//						 * 5.2) Le voisin2 est null, sinon c'est qu'il y a un soucis de construction.<br>
//						 * On calcul l'angle entre le voisin1 et le potentiel voisin2.<br>
//						 * Si la valeur absolu de l'angle est > à 45° alors le voisin doit bien appartenir à le côté opposé de la ligne et on indique aux gares
//						 * qu'elles sont voisines.
//						 */
//						System.out.println("voisin 2");
//						Gare gareVoisine = gos.get(voisin.voisin1);
//						Vecteur vect1 = new Vecteur(gareElt.getLatitude(), gareElt.getLongitude(), gareVoisine.getLatitude(), gareVoisine.getLongitude()), vect2 = new Vecteur(
//								gareElt.getLatitude(), gareElt.getLongitude(), voisinPotentielle.getLatitude(), voisinPotentielle.getLongitude());
//						if (Math.abs(vect1.angle(vect2)) > angleMinimumOpposition) {
//							System.out.println("angle ok");
//							if (ajoute(voisins, gareElt.getNom(), voisinPotentielle.getNom(), true))
//								aTraiter.remove(voisinPotentielle.getNom());
//						} else {
//							System.out.println("angle KO");
//						}
//					}
//				}
//			}
//			return voisins;
//			// return goodOrder(gos, voisins);
//		} catch (Exception e) {
//			System.out.println("\n\n################ ERREUR ####################\n\n");
//			return voisins;
//		}
//	}
//
//	private static Collection<Gare> goodOrder(Map<String, Gare> gos, Map<String, Voisin> voisins) throws Exception {
//		List<Gare> gares = new ArrayList<Gare>();
//
//		Gare debut = null;
//		for (Gare go : gos.values())
//			if (voisins.get(go.getNom()).voisin2 == null) {
//				debut = go;
//				break;
//			}
//		if (debut == null)
//			throw new Exception("Impossible de trouver un début de ligne");
//
//		while (debut != null) {
//			gares.add(debut);
//			if (gares.contains(gos.get(voisins.get(debut.getNom()).voisin1)))
//				debut = gos.get(voisins.get(debut.getNom()).voisin2);
//			else
//				debut = gos.get(voisins.get(debut.getNom()).voisin1);
//		}
//		return gares;
//	}
//
//	public static void main(String[] args) throws Exception {
//		/* Test loadGare */
//		Map<String, Gare> gares = new HashMap<String, Gare>();
//
//		JSONParser parser = new JSONParser();
//		JSONArray array = (JSONArray) parser.parse(new FileReader("src/main/webapp/ressources/sncf-gares-et-arrets-transilien-ile-de-france.json"));
//		for (int i = 0; i < array.size(); i++) {
//			JSONObject jsonGare = (JSONObject) ((JSONObject) array.get(i)).get("fields");
//			int codeUIC = Integer.parseInt((String) jsonGare.get("code_uic"));
//
//			String nom = (String) jsonGare.get("libelle");
//			JSONArray coordGPS = (JSONArray) jsonGare.get("coord_gps_wgs84");
//			Double latitude = (Double) coordGPS.get(0), longitude = (Double) coordGPS.get(1);
//
//			gares.put(nom, new Gare(nom, codeUIC, longitude, latitude));
//		}
//		/* FIN Test loadGare */
//
//		/* Test loadLigne */
//		String[] lTrain = { "h", "j", "k", "l", "n", "p", "r", "u" }, lRER = { "a", "b", "c", "d", "e" };
//		Map<String, Ligne> lignes = new HashMap<String, Ligne>();
//
//		JSONArray array2 = (JSONArray) parser.parse(new FileReader("src/main/webapp/ressources/sncf-lignes-par-gares-idf.json"));
//
//		for (int i = 0; i < array2.size(); i++) {
//			JSONObject jsonLigne = (JSONObject) ((JSONObject) array2.get(i)).get("fields");
//
//			if (jsonLigne.get("train") != null)
//				JsonToObject.auxLoadLigne(lTrain, jsonLigne, lignes);
//			if (jsonLigne.get("rer") != null)
//				JsonToObject.auxLoadLigne(lRER, jsonLigne, lignes);
//		}
//		/* FIN Test loadLigne */
//
//		//
//		//
//		//
//		//
//
//		Map<Integer, Gare> mapGare = new HashMap<Integer, Gare>();
//		for (Gare g : gares.values())
//			for (Integer i : g.getCodesUIC())
//				mapGare.put(i, g);
//
//		Map<String, Voisin> voisins = execute(mapGare, lignes.get("b"));
//		String s = "";
//		for (String i : voisins.keySet())
//			s += i + "\t" + voisins.get(i) + "\n";
//		System.out.println(s);
//	}
// }
